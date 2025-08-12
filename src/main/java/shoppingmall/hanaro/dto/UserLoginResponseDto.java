package shoppingmall.hanaro.dto;

import lombok.Builder;

@Builder
public record UserLoginResponseDto(
        String accessToken,
        String refreshToken
) {
}
