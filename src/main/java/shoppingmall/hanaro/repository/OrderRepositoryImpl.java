package shoppingmall.hanaro.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import shoppingmall.hanaro.domain.OrderStatus;
import shoppingmall.hanaro.dto.OrderSearchCondition;
import shoppingmall.hanaro.dto.OrderResponseDto;
import shoppingmall.hanaro.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static shoppingmall.hanaro.domain.QOrder.order;
import static shoppingmall.hanaro.domain.QUser.user;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<OrderResponseDto> search(OrderSearchCondition condition, Pageable pageable) {
        List<Order> content = queryFactory
                .selectFrom(order)
                .join(order.user, user)
                .where(
                        usernameEq(condition.getUsername()),
                        orderStatusEq(condition.getOrderStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // fetchCount() is deprecated
        Long total = queryFactory
                .select(order.count())
                .from(order)
                .join(order.user, user)
                .where(
                        usernameEq(condition.getUsername()),
                        orderStatusEq(condition.getOrderStatus())
                )
                .fetchOne();
        
        List<OrderResponseDto> dtoList = content.stream()
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total != null ? total : 0L);
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? user.name.eq(username) : null;
    }

    private BooleanExpression orderStatusEq(OrderStatus orderStatus) {
        return orderStatus != null ? order.status.eq(orderStatus) : null;
    }
}
