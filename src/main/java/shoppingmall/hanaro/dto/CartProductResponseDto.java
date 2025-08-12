package shoppingmall.hanaro.dto;

import lombok.Builder;
import shoppingmall.hanaro.domain.CartProduct;

@Builder
public record CartProductResponseDto(
        Long cartProductId,
        Long productId,
        String name,
        int price,
        int quantity,
        String imageUrl
) {
    public static CartProductResponseDto from(CartProduct cartProduct) {
        return new CartProductResponseDto(
                cartProduct.getCartProductId(),
                cartProduct.getProduct().getProductId(),
                cartProduct.getProduct().getName(),
                cartProduct.getProduct().getPrice(),
                cartProduct.getQuantity(),
                cartProduct.getProduct().getImageUrl()
        );
    }
}
