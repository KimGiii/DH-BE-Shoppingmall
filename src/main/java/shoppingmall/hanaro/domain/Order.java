package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
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
    private OrderStatus status; // ORDER, CANCEL

    //== 연관관계 편의 메서드 ==//
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
        order.setUser(user);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.status = OrderStatus.PAYMENT_COMPLETED; // 초기 상태 '결제완료'로 변경
        order.orderDate = LocalDateTime.now();
        return order;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    //== 비즈니스 로직 ==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.SHIPPED) {
            throw new IllegalStateException("이미 배송이 시작된 상품은 취소가 불가능합니다.");
        }

        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //== 조회 로직 ==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
