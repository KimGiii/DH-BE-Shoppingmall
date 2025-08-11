package shoppingmall.hanaro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

    @NotBlank(message = "로그인 아이디는 필수 입력 값입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "도시는 필수 입력 값입니다.")
    private String city;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String street;

    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String zipcode;
}
