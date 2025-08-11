package shoppingmall.hanaro.domain;

public enum OrderStatus {
    PAYMENT_COMPLETED,      // 결제완료
    PREPARING_FOR_SHIPMENT, // 배송준비중
    SHIPPED,                // 배송중
    DELIVERED,              // 배송완료
    ORDER,                  // 주문
    CANCEL                  // 주문취소
}
