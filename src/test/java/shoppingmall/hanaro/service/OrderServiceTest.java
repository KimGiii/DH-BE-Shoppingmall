package shoppingmall.hanaro.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.Address;
import shoppingmall.hanaro.domain.Product;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.dto.CartProductRequestDto;
import shoppingmall.hanaro.repository.ProductRepository;
import shoppingmall.hanaro.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 및 상품 데이터 생성
        Address address = new Address("서울", "테헤란로", "12345");
        testUser = User.createUser("testuser", "password", "테스트유저", address);
        userRepository.saveAndFlush(testUser);

        testProduct = Product.createProduct("테스트 상품", 10000, 100, "설명", "image.jpg");
        productRepository.saveAndFlush(testProduct);
    }

    @Test
    @DisplayName("장바구니 기반 주문 생성 테스트")
    void createOrderFromCart() {
        // given
        // 1. 장바구니에 상품 추가
        CartProductRequestDto cartRequest = new CartProductRequestDto();
        cartRequest.setProductId(testProduct.getProductId());
        cartRequest.setQuantity(2);
        cartService.addProductToCart(testUser.getLoginId(), cartRequest);

        // when
        // 2. 주문 생성
        Long orderId = orderService.createOrderFromCart(testUser.getLoginId());

        // then
        // 3. 주문 검증
        var order = orderService.findOrderDetails(orderId);
        assertEquals(1, order.getOrderItems().size());
        assertEquals(20000, order.getTotalPrice());
        assertEquals("테스트 상품", order.getOrderItems().get(0).getName());

        // 4. 장바구니가 비워졌는지 검증
        var cart = cartService.getCart(testUser.getLoginId());
        assertEquals(0, cart.getCartProducts().size());
    }
}
