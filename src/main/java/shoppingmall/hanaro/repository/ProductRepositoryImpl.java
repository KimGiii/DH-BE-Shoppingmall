package shoppingmall.hanaro.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import shoppingmall.hanaro.domain.Product;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.dto.ProductSearchCondition;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static shoppingmall.hanaro.domain.QProduct.product;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ProductResponseDto> search(ProductSearchCondition condition, Pageable pageable) {
        List<Product> content = queryFactory
                .selectFrom(product)
                .where(keywordContains(condition.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(keywordContains(condition.getKeyword()))
                .fetchOne();

        List<ProductResponseDto> dtoList = content.stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total != null ? total : 0L);
    }

    private BooleanExpression keywordContains(String keyword) {
        return hasText(keyword) ? product.name.containsIgnoreCase(keyword).or(product.description.containsIgnoreCase(keyword)) : null;
    }
}
