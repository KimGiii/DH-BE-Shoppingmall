package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    public void update(String name, int price, int stockQuantity, String description) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public void updateStock(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

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
            throw new IllegalStateException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
