package shoppingmall.hanaro.dto;

import lombok.Builder;
import shoppingmall.hanaro.domain.OrderItem;

@Builder
public record OrderItemDto(
        String name,
        int quantity,
        int orderPrice,
        String imageUrl
) {
    public static OrderItemDto from(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getOrderPrice(),
                orderItem.getProduct().getImageUrl()
        );
    }
}
