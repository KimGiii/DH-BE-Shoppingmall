package shoppingmall.hanaro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shoppingmall.hanaro.dto.OrderSearchCondition;
import shoppingmall.hanaro.dto.OrderResponseDto;

public interface OrderRepositoryCustom {
    Page<OrderResponseDto> search(OrderSearchCondition condition, Pageable pageable);
}
