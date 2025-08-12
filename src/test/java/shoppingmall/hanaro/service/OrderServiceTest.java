package shoppingmall.hanaro.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import shoppingmall.hanaro.domain.*;
import shoppingmall.hanaro.dto.OrderDetailResponseDto;
import shoppingmall.hanaro.repository.OrderRepository;
import shoppingmall.hanaro.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자의 장바구니 기반으로 주문을 생성한다.")
    void createOrderFromCartTest() {
        // given
        User testUser = User.createUser("testuser", "password", "테스트", new Address("city", "street", "zipcode"));
        Product testProduct = Product.createProduct("테스트 상품", 10000, 10, "설명", "url");
        Cart cart = testUser.getCart();
        cart.addCartProduct(CartProduct.createCartProduct(testProduct, 2));

        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.of(testUser));
        when(orderRepository.findById(any())).thenReturn(Optional.of(
                Order.createOrder(testUser, Delivery.createDelivery(testUser.getAddress()), OrderItem.createOrderItem(testProduct, 10000, 2))
        ));

        // when
        Long orderId = orderService.createOrderFromCart("testuser");
        OrderDetailResponseDto orderDetails = orderService.findOrderDetails(orderId);

        // then
        assertEquals(1, orderDetails.orderItems().size());
        assertEquals(20000, orderDetails.totalPrice());
        assertEquals("테스트 상품", orderDetails.orderItems().get(0).name());
    }
}
