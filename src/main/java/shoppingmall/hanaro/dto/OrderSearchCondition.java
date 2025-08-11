package shoppingmall.hanaro.dto;

import lombok.Data;
import shoppingmall.hanaro.domain.OrderStatus;

@Data
public class OrderSearchCondition {
    private String username;
    private OrderStatus orderStatus;
}
