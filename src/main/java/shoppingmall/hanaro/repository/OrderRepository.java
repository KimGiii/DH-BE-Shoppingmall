package shoppingmall.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.hanaro.domain.Order;
import shoppingmall.hanaro.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findByUser_UserId(Long userId);
    List<Order> findAllByStatusAndOrderDateBefore(OrderStatus status, LocalDateTime orderDate);
    List<Order> findAllByOrderDateBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, OrderStatus status);
}
