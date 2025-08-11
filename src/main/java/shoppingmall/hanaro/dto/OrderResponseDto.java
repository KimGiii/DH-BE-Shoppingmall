package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.hanaro.domain.Order;
import shoppingmall.hanaro.domain.OrderStatus;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private int totalPrice;
    private String representativeImage; // 대표 이미지
    private String representativeProductName; // 대표 상품명

    @Builder
    public OrderResponseDto(Long orderId, LocalDateTime orderDate, OrderStatus orderStatus, int totalPrice, String representativeImage, String representativeProductName) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.representativeImage = representativeImage;
        this.representativeProductName = representativeProductName;
    }

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .representativeImage(order.getOrderItems().get(0).getProduct().getImageUrl())
                .representativeProductName(order.getOrderItems().get(0).getProduct().getName())
                .build();
    }
}
