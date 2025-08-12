package shoppingmall.hanaro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record ProductCreateRequestDto(
        @NotBlank(message = "상품명은 필수 입력 값입니다.")
        String name,
        @Min(value = 1, message = "가격은 0보다 커야 합니다.")
        int price,
        @Min(value = 1, message = "재고는 0보다 커야 합니다.")
        int stockQuantity,
        @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
        String description,
        MultipartFile image
) {
}
