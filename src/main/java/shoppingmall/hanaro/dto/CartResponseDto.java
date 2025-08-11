package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CartResponseDto {
    private Long cartId;
    private List<CartProductResponseDto> cartProducts;
    private int totalPrice;

    @Builder
    public CartResponseDto(Long cartId, List<CartProductResponseDto> cartProducts) {
        this.cartId = cartId;
        this.cartProducts = cartProducts;
        this.totalPrice = cartProducts.stream()
                .mapToInt(p -> p.getPrice() * p.getQuantity())
                .sum();
    }
}
