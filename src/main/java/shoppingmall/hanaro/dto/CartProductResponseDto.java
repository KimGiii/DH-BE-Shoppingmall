package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.hanaro.domain.CartProduct;

@Getter
public class CartProductResponseDto {
    private Long cartProductId;
    private Long productId;
    private String name;
    private int price;
    private int quantity;
    private String imageUrl;

    @Builder
    public CartProductResponseDto(Long cartProductId, Long productId, String name, int price, int quantity, String imageUrl) {
        this.cartProductId = cartProductId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public static CartProductResponseDto from(CartProduct cartProduct) {
        return CartProductResponseDto.builder()
                .cartProductId(cartProduct.getCartProductId())
                .productId(cartProduct.getProduct().getProductId())
                .name(cartProduct.getProduct().getName())
                .price(cartProduct.getProduct().getPrice())
                .quantity(cartProduct.getQuantity())
                .imageUrl(cartProduct.getProduct().getImageUrl())
                .build();
    }
}
