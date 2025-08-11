package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
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

        // 결제 완료 후 5분이 지난 주문 -> 배송 준비중으로 변경
        List<Order> paymentCompletedOrders = orderRepository.findAllByStatusAndOrderDateBefore(
                OrderStatus.PAYMENT_COMPLETED, now.minusMinutes(5));
        paymentCompletedOrders.forEach(order -> order.updateStatus(OrderStatus.PREPARING_FOR_SHIPMENT));

        // 배송 준비중 후 15분이 지난 주문 -> 배송중으로 변경
        List<Order> preparingOrders = orderRepository.findAllByStatusAndOrderDateBefore(
                OrderStatus.PREPARING_FOR_SHIPMENT, now.minusMinutes(15));
        preparingOrders.forEach(order -> order.updateStatus(OrderStatus.SHIPPED));

        // 배송중 후 1시간이 지난 주문 -> 배송 완료로 변경
        List<Order> shippedOrders = orderRepository.findAllByStatusAndOrderDateBefore(
                OrderStatus.SHIPPED, now.minusHours(1));
        shippedOrders.forEach(order -> order.updateStatus(OrderStatus.DELIVERED));
    }
}
