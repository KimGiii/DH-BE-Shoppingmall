package shoppingmall.hanaro.dto;

import lombok.Builder;
import shoppingmall.hanaro.domain.Product;

@Builder
public record ProductResponseDto(
        Long productId,
        String name,
        int price,
        int stockQuantity,
        String description,
        String imageUrl
) {
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getDescription(),
                product.getImageUrl()
        );
    }
}
