package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public UserLoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
