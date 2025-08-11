package shoppingmall.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.hanaro.domain.DailySales;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailySalesRepository extends JpaRepository<DailySales, Long> {
    Optional<DailySales> findBySalesDate(LocalDate salesDate);
    List<DailySales> findBySalesDateBetween(LocalDate startDate, LocalDate endDate);
}
