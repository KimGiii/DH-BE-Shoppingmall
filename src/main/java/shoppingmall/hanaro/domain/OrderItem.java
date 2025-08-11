package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 당시 가격

    private int quantity; // 주문 수량

    //== 생성 메서드 ==//
    public static OrderItem createOrderItem(Product product, int orderPrice, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.product = product;
        orderItem.orderPrice = orderPrice;
        orderItem.quantity = quantity;

        product.removeStock(quantity); // 주문 수량만큼 재고 감소
        return orderItem;
    }

    //== 비즈니스 로직 ==//
    /**
     * 주문 취소
     */
    public void cancel() {
        getProduct().addStock(quantity); // 재고 수량 원복
    }

    //== 조회 로직 ==//
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getQuantity();
    }

    //== 연관관계 편의 메서드 ==//
    public void setOrder(Order order) {
        this.order = order;
    }
}
