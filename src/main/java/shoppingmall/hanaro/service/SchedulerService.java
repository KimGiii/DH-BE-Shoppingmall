package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.Order;
import shoppingmall.hanaro.domain.OrderStatus;
import shoppingmall.hanaro.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final OrderRepository orderRepository;

    /**
     * 주문 상태 자동 업데이트 스케줄러
     * - 결제완료 -> 배송준비중 (5분 후)
     * - 배송준비중 -> 배송중 (15분 후)
     * - 배송중 -> 배송완료 (1시간 후)
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void autoUpdateOrderStatus() {
        LocalDateTime now = LocalDateTime.now();

        // 결제 완료 상태가 된 후 5분이 지난 주문 -> 배송 준비중으로 변경
        List<Order> paymentCompletedOrders = orderRepository.findAllByStatusAndStatusUpdateTimeBefore(
                OrderStatus.PAYMENT_COMPLETED, now.minusMinutes(5));
        paymentCompletedOrders.forEach(order -> {
            if (order.getDelivery() != null) {
                order.updateStatus(OrderStatus.PREPARING_FOR_SHIPMENT);
            } else {
                log.warn("Order ID {} has no delivery info.", order.getOrderId());
            }
        });

        // 배송 준비중 상태가 된 후 15분이 지난 주문 -> 배송중으로 변경
        List<Order> preparingOrders = orderRepository.findAllByStatusAndStatusUpdateTimeBefore(
                OrderStatus.PREPARING_FOR_SHIPMENT, now.minusMinutes(15));
        preparingOrders.forEach(order -> {
            if (order.getDelivery() != null) {
                order.updateStatus(OrderStatus.SHIPPED);
            } else {
                log.warn("Order ID {} has no delivery info.", order.getOrderId());
            }
        });

        // 배송중 상태가 된 후 1시간이 지난 주문 -> 배송 완료로 변경
        List<Order> shippedOrders = orderRepository.findAllByStatusAndStatusUpdateTimeBefore(
                OrderStatus.SHIPPED, now.minusHours(1));
        shippedOrders.forEach(order -> {
            if (order.getDelivery() != null) {
                order.updateStatus(OrderStatus.DELIVERED);
            } else {
                log.warn("Order ID {} has no delivery info.", order.getOrderId());
            }
        });
    }
}
