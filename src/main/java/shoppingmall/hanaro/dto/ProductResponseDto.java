package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.hanaro.domain.Product;

@Getter
public class ProductResponseDto {

    private Long productId;
    private String name;
    private int price;
    private int stockQuantity;
    private String description;
    private String imageUrl;

    @Builder
    public ProductResponseDto(Long productId, String name, int price, int stockQuantity, String description, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static ProductResponseDto from(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .build();
    }
}
