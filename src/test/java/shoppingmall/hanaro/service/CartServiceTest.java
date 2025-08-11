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
import shoppingmall.hanaro.dto.CartResponseDto;
import shoppingmall.hanaro.repository.ProductRepository;
import shoppingmall.hanaro.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CartServiceTest {

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
        Address address = new Address("서울", "테헤란로", "12345");
        testUser = User.createUser("cartUser", "password", "장바구니유저", address);
        userRepository.saveAndFlush(testUser);

        testProduct = Product.createProduct("장바구니 테스트 상품", 1000, 10, "설명", "image.jpg");
        productRepository.saveAndFlush(testProduct);
    }

    @Test
    @DisplayName("장바구니 기능 통합 테스트 (추가,조회,수정,삭제)")
    void cartServiceIntegrationTest() {
        // 1. 장바구니에 상품 추가
        CartProductRequestDto addRequest = new CartProductRequestDto();
        addRequest.setProductId(testProduct.getProductId());
        addRequest.setQuantity(2);
        cartService.addProductToCart(testUser.getLoginId(), addRequest);

        // 2. 장바구니 조회 및 검증
        CartResponseDto cart = cartService.getCart(testUser.getLoginId());
        assertEquals(1, cart.getCartProducts().size());
        assertEquals(2000, cart.getTotalPrice());
        assertEquals(2, cart.getCartProducts().get(0).getQuantity());

        // 3. 장바구니 상품 수량 변경
        Long cartProductId = cart.getCartProducts().get(0).getCartProductId();
        cartService.updateCartProductCount(cartProductId, 5); // 수량을 5로 변경

        // 4. 수량 변경 후 조회 및 검증
        CartResponseDto updatedCart = cartService.getCart(testUser.getLoginId());
        assertEquals(5000, updatedCart.getTotalPrice());
        assertEquals(5, updatedCart.getCartProducts().get(0).getQuantity());

        // 5. 장바구니 상품 삭제
        cartService.deleteCartProduct(cartProductId);

        // 6. 삭제 후 장바구니가 비었는지 검증
        CartResponseDto finalCart = cartService.getCart(testUser.getLoginId());
        assertTrue(finalCart.getCartProducts().isEmpty());
    }
}
