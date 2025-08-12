package shoppingmall.hanaro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartProductRequestDto(
        @NotNull(message = "상품 ID를 입력해주세요.")
        Long productId,
        @NotNull(message = "수량을 입력해주세요.")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        int quantity
) {
}
