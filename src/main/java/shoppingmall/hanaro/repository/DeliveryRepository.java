package shoppingmall.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.hanaro.domain.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
