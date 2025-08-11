package shoppingmall.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.hanaro.domain.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_UserId(Long userId);
}
