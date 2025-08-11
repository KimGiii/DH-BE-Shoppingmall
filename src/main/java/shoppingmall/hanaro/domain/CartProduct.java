package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_product_id")
    private Long cartProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity; // 담은 수량

    public static CartProduct createCartProduct(Product product, int quantity) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.product = product;
        cartProduct.quantity = quantity;
        return cartProduct;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    //== 연관관계 편의 메서드 ==//
    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
