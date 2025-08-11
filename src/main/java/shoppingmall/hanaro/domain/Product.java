package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.hanaro.dto.ProductUpdateRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    private String name;

    private int price;

    private int stockQuantity;

    private String description;

    @Column(name = "image_url")
    private String imageUrl; // 상품 이미지 경로

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Product createProduct(String name, int price, int stockQuantity, String description, String imageUrl) {
        Product product = new Product();
        product.name = name;
        product.price = price;
        product.stockQuantity = stockQuantity;
        product.description = description;
        product.imageUrl = imageUrl;
        return product;
    }

    public void update(ProductUpdateRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.stockQuantity = requestDto.getStockQuantity();
        this.description = requestDto.getDescription();
    }

    //== 비즈니스 로직 ==//
    /**
     * 재고 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            // TODO: 재고 부족 예외 처리
            throw new IllegalStateException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
