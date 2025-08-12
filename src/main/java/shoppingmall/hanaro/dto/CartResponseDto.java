package shoppingmall.hanaro.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CartResponseDto(
        Long cartId,
        List<CartProductResponseDto> cartProducts,
        int totalPrice
) {
    public CartResponseDto(Long cartId, List<CartProductResponseDto> cartProducts) {
        this(cartId, cartProducts, cartProducts.stream()
                .mapToInt(p -> p.price() * p.quantity())
                .sum());
    }
}
