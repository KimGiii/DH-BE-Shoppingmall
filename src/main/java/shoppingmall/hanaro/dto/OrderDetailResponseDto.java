package shoppingmall.hanaro.dto;

import lombok.Builder;
import shoppingmall.hanaro.domain.Address;
import shoppingmall.hanaro.domain.Order;
import shoppingmall.hanaro.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record OrderDetailResponseDto(
        Long orderId,
        LocalDateTime orderDate,
        OrderStatus orderStatus,
        int totalPrice,
        Address address,
        List<OrderItemDto> orderItems
) {
    public static OrderDetailResponseDto from(Order order) {
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());

        return new OrderDetailResponseDto(
                order.getOrderId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getDelivery().getAddress(),
                orderItemDtos
        );
    }
}
