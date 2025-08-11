package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.Address;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.domain.UserToken;
import shoppingmall.hanaro.dto.TokenReissueRequestDto;
import shoppingmall.hanaro.dto.TokenReissueResponseDto;
import shoppingmall.hanaro.dto.UserLoginRequestDto;
import shoppingmall.hanaro.dto.UserLoginResponseDto;
import shoppingmall.hanaro.dto.UserSignupRequestDto;
import shoppingmall.hanaro.dto.UserResponseDto;
import shoppingmall.hanaro.exception.BusinessException;
import shoppingmall.hanaro.exception.ErrorCode;
import shoppingmall.hanaro.repository.UserRepository;
import shoppingmall.hanaro.repository.UserTokenRepository;
import shoppingmall.hanaro.util.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(UserSignupRequestDto requestDto) {
        // ID 중복 검사
        userRepository.findByLoginId(requestDto.getLoginId()).ifPresent(user -> {
            throw new BusinessException(ErrorCode.LOGIN_INPUT_INVALID); // TODO: 더 적절한 에러코드로 변경
        });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Address address = new Address(requestDto.getCity(), requestDto.getStreet(), requestDto.getZipcode());

        User user = User.createUser(requestDto.getLoginId(), encodedPassword, requestDto.getName(), address);
        userRepository.save(user);
    }

    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        String accessToken = jwtUtil.createAccessToken(user.getLoginId(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getLoginId(), user.getRole());

        // 토큰 저장 또는 업데이트
        userTokenRepository.findById(user.getUserId()).ifPresentOrElse(
                userToken -> userToken.updateTokens(accessToken, refreshToken),
                () -> userTokenRepository.save(UserToken.create(user, accessToken, refreshToken))
        );

        return UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public TokenReissueResponseDto reissueToken(TokenReissueRequestDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();

        // 1. Refresh Token 유효성 검증
        jwtUtil.validateToken(refreshToken);

        // 2. DB에서 Refresh Token 조회
        UserToken userToken = userTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        User user = userToken.getUser();

        // 3. 새로운 토큰 생성
        String newAccessToken = jwtUtil.createAccessToken(user.getLoginId(), user.getRole());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getLoginId(), user.getRole());

        // 4. DB에 토큰 업데이트
        userToken.updateTokens(newAccessToken, newRefreshToken);

        return TokenReissueResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional
    public void logout(TokenReissueRequestDto requestDto) {
        UserToken userToken = userTokenRepository.findByRefreshToken(requestDto.getRefreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        userTokenRepository.delete(userToken);
    }

    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
