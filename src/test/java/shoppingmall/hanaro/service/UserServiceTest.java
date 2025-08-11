package shoppingmall.hanaro.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.dto.UserSignupRequestDto;
import shoppingmall.hanaro.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "testuser", "password", "테스트유저", "서울", "테헤란로", "12345");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.empty());

        // when
        userService.signup(requestDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }
}
