package shoppingmall.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.hanaro.domain.UserToken;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByRefreshToken(String refreshToken);
}
