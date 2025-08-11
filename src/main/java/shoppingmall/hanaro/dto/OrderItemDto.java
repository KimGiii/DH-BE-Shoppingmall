package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.hanaro.domain.OrderItem;

@Getter
public class OrderItemDto {
    private String name;
    private int quantity;
    private int orderPrice;
    private String imageUrl;

    @Builder
    public OrderItemDto(String name, int quantity, int orderPrice, String imageUrl) {
        this.name = name;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.imageUrl = imageUrl;
    }

    public static OrderItemDto from(OrderItem orderItem) {
        return OrderItemDto.builder()
                .name(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .orderPrice(orderItem.getOrderPrice())
                .imageUrl(orderItem.getProduct().getImageUrl())
                .build();
    }
}
