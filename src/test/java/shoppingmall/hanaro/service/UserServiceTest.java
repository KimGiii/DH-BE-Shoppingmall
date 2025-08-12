package shoppingmall.hanaro.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import shoppingmall.hanaro.domain.Address;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.domain.UserToken;
import shoppingmall.hanaro.dto.UserLoginRequestDto;
import shoppingmall.hanaro.dto.UserSignupRequestDto;
import shoppingmall.hanaro.repository.UserRepository;
import shoppingmall.hanaro.repository.UserTokenRepository;
import shoppingmall.hanaro.util.JwtUtil;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserTokenRepository userTokenRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자가 회원가입을 한다.")
    void signup() {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "testuser", "password", "테스트유저", "서울", "테헤란로", "12345678");

        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(userRepository.findByLoginId(anyString())).willReturn(Optional.empty());

        // when
        userService.signup(requestDto);

        // then
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @DisplayName("사용자는 회원가입한 id와 비밀번호를 사용해 로그인한다.")
    @Test
    void login() {
        // given
        UserLoginRequestDto requestDto = new UserLoginRequestDto("testuser", "password");
        User user = User.builder()
                .userId(1L)
                .loginId("testuser")
                .password("encodedPassword")
                .name("테스트 유저")
                .address(new Address("서울", "테헤란로", "12345"))
                .build();

        given(userRepository.findByLoginId(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtUtil.createAccessToken(any(), any())).willReturn("access-token");
        given(jwtUtil.createRefreshToken(any(), any())).willReturn("refresh-token");
        given(userTokenRepository.findById(any())).willReturn(Optional.empty());

        // when
        userService.login(requestDto);

        // then
        then(userRepository).should(times(1)).findByLoginId(anyString());
        then(passwordEncoder).should(times(1)).matches(anyString(), anyString());
        then(jwtUtil).should(times(1)).createAccessToken(any(), any());
        then(jwtUtil).should(times(1)).createRefreshToken(any(), any());
        then(userTokenRepository).should(times(1)).save(any(UserToken.class));
    }

}
