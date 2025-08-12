package shoppingmall.hanaro.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shoppingmall.hanaro.domain.*;
import shoppingmall.hanaro.dto.CartProductRequestDto;
import shoppingmall.hanaro.dto.CartResponseDto;
import shoppingmall.hanaro.repository.CartProductRepository;
import shoppingmall.hanaro.repository.ProductRepository;
import shoppingmall.hanaro.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartProductRepository cartProductRepository;

    @Test
    @DisplayName("사용자가 장바구니에 상품을 추가할 수 있다.")
    void cartServiceTest() {
        // given
        User testUser = User.createUser("testuser", "password", "테스트", new Address("city", "street", "zipcode"));
        Product testProduct = Product.createProduct("테스트 상품", 1000, 10, "설명", "url");

        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(testProduct));

        // when - 상품 추가
        CartProductRequestDto addRequest = new CartProductRequestDto(1L, 2);
        cartService.addProductToCart("testuser", addRequest);
        CartResponseDto cart = cartService.getCart("testuser");

        // then - 상품 추가 확인
        assertEquals(1, cart.cartProducts().size());
        assertEquals(2000, cart.totalPrice());
    }
}
