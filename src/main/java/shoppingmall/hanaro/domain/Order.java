package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime statusUpdateTime;

    public void setUser(User user) {
        this.user = user;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(User user, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.user = user;
        order.delivery = delivery;
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.status = OrderStatus.PAYMENT_COMPLETED; // 주문 생성 시 초기 상태
        order.orderDate = LocalDateTime.now();
        order.statusUpdateTime = LocalDateTime.now(); // 주문 생성 시 상태 시간 초기화
        return order;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        this.statusUpdateTime = LocalDateTime.now();

        // 주문 상태에 따라 배송 상태도 함께 변경
        if (newStatus == OrderStatus.PREPARING_FOR_SHIPMENT) {
            this.delivery.updateStatus(DeliveryStatus.PREPARING);
        } else if (newStatus == OrderStatus.SHIPPED) {
            this.delivery.updateStatus(DeliveryStatus.SHIPPED);
        } else if (newStatus == OrderStatus.DELIVERED) {
            this.delivery.updateStatus(DeliveryStatus.DELIVERED);
        }
    }

    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.SHIPPED || delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("이미 배송이 시작된 상품은 취소가 불가능합니다.");
        }

        this.updateStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    public void setTotalPrice(int totalPrice) {
        // 테스트용 메서드
    }
}
