package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.Cart;
import shoppingmall.hanaro.domain.CartProduct;
import shoppingmall.hanaro.domain.Product;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.dto.CartProductRequestDto;
import shoppingmall.hanaro.dto.CartProductResponseDto;
import shoppingmall.hanaro.dto.CartResponseDto;
import shoppingmall.hanaro.exception.BusinessException;
import shoppingmall.hanaro.exception.ErrorCode;
import shoppingmall.hanaro.repository.CartProductRepository;
import shoppingmall.hanaro.repository.ProductRepository;
import shoppingmall.hanaro.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public void addProductToCart(String loginId, CartProductRequestDto requestDto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        Cart cart = user.getCart();

        // 장바구니에 이미 상품이 있는지 확인
        cartProductRepository.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), product.getProductId())
                .ifPresentOrElse(
                        cartProduct -> cartProduct.addQuantity(requestDto.getQuantity()), // 있으면 수량 추가
                        () -> { // 없으면 새로 추가
                            CartProduct newCartProduct = CartProduct.createCartProduct(product, requestDto.getQuantity());
                            cart.addCartProduct(newCartProduct);
                            cartProductRepository.save(newCartProduct);
                        }
                );
    }

    public CartResponseDto getCart(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Cart cart = user.getCart();

        List<CartProductResponseDto> cartProductDtos = cart.getCartProducts().stream()
                .map(CartProductResponseDto::from)
                .collect(Collectors.toList());

        return CartResponseDto.builder()
                .cartId(cart.getCartId())
                .cartProducts(cartProductDtos)
                .build();
    }

    @Transactional
    public void updateCartProductCount(Long cartProductId, int quantity) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        cartProduct.updateQuantity(quantity); // addCount -> updateCount로 변경
    }

    @Transactional
    public void deleteCartProduct(Long cartProductId) {
        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        
        // 부모 엔티티의 컬렉션에서 제거하여 orphanRemoval을 유도
        cartProduct.getCart().getCartProducts().remove(cartProduct);
    }
}
