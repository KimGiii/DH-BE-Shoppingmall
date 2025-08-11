package shoppingmall.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.hanaro.domain.CartProduct;

import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Optional<CartProduct> findByCart_CartIdAndProduct_ProductId(Long cartId, Long productId);
}
