package shoppingmall.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.hanaro.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
}
