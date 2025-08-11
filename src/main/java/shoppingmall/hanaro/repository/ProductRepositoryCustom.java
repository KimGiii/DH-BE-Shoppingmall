package shoppingmall.hanaro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.dto.ProductSearchCondition;

public interface ProductRepositoryCustom {
    Page<ProductResponseDto> search(ProductSearchCondition condition, Pageable pageable);
}
