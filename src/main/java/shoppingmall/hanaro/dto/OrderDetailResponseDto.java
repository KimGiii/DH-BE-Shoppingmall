package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.hanaro.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDetailResponseDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private int totalPrice;
    private Address address;
    private List<OrderItemDto> orderItems;

    @Builder
    public OrderDetailResponseDto(Long orderId, LocalDateTime orderDate, OrderStatus orderStatus, int totalPrice, Address address, List<OrderItemDto> orderItems) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderItems = orderItems;
    }

    public static OrderDetailResponseDto from(Order order) {
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());

        return OrderDetailResponseDto.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .address(order.getDelivery().getAddress())
                .orderItems(orderItemDtos)
                .build();
    }
}
