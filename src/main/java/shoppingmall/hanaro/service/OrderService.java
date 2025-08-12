package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.*;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.dto.OrderDetailResponseDto;
import shoppingmall.hanaro.dto.OrderResponseDto;
import shoppingmall.hanaro.dto.OrderSearchCondition;
import shoppingmall.hanaro.exception.BusinessException;
import shoppingmall.hanaro.exception.ErrorCode;
import shoppingmall.hanaro.repository.OrderRepository;
import shoppingmall.hanaro.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private static final Logger orderLog = LoggerFactory.getLogger("business.order");
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createOrderFromCart(String loginId) {
        orderLog.info("[Order Start] Start creating order from cart for user: {}", loginId);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        orderLog.info("[Order Step] User and cart validation complete for user: {}", loginId);

        Cart cart = user.getCart();
        if (cart.getCartProducts().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE); // TODO: 더 적절한 에러코드
        }

        // 배송정보 생성
        Delivery delivery = Delivery.createDelivery(user.getAddress());
        orderLog.info("[Order Step] Delivery info created for user: {}", loginId);

        // 주문상품 목록 생성
        List<OrderItem> orderItems = cart.getCartProducts().stream()
                .map(cartProduct -> OrderItem.createOrderItem(
                        cartProduct.getProduct(),
                        cartProduct.getProduct().getPrice(),
                        cartProduct.getQuantity()))
                .collect(Collectors.toList());
        orderLog.info("[Order Step] OrderItem list created. Item count: {}", orderItems.size());

        // 주문 생성
        Order order = Order.createOrder(user, delivery, orderItems.toArray(new OrderItem[0]));
        orderRepository.save(order);
        orderLog.info("[Order Step] Order entity created and saved. Order ID: {}", order.getOrderId());

        // 장바구니 비우기
        cart.getCartProducts().clear();
        orderLog.info("[Order Step] Cart cleared for user: {}", loginId);

        orderLog.info("[Order Success] New order created successfully. Order ID: {}, User: {}", order.getOrderId(), loginId);
        return order.getOrderId();
    }

    public Page<OrderResponseDto> findMyOrders(String loginId, Pageable pageable) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return orderRepository.findByUser_UserId(user.getUserId(), pageable)
                .map(OrderResponseDto::from);
    }

    public OrderDetailResponseDto findOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        order.cancel();
        orderLog.info("[Order Log] Order Canceled. Order ID: {}", orderId);
    }

    public List<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());
    }

    public Page<OrderResponseDto> searchOrders(OrderSearchCondition condition, Pageable pageable) {
        return orderRepository.search(condition, pageable);
    }
}
