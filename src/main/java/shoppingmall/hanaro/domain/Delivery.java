package shoppingmall.hanaro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public void setOrder(Order order) {
        this.order = order;
    }

    public static Delivery createDelivery(Address address) {
        Delivery delivery = new Delivery();
        delivery.address = address;
        delivery.status = DeliveryStatus.PREPARING; // 초기 상태: 배송 준비
        return delivery;
    }

    public void updateStatus(DeliveryStatus status) {
        this.status = status;
    }
}
