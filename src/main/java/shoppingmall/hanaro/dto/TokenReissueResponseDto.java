package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenReissueResponseDto {
    private String accessToken;
    private String refreshToken;
}
