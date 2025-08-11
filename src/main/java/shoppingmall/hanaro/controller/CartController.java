package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppingmall.hanaro.dto.CartProductRequestDto;
import shoppingmall.hanaro.dto.CartResponseDto;
import shoppingmall.hanaro.service.CartService;

import java.security.Principal;

@Tag(name = "장바구니 관리", description = "사용자 장바구니 관리 API (인증 필요)")
@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 상품을 추가합니다. 이미 상품이 있으면 수량을 더합니다.")
    @PostMapping("/products")
    public ResponseEntity<Void> addProductToCart(Principal principal, @Valid @RequestBody CartProductRequestDto requestDto) {
        cartService.addProductToCart(principal.getName(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "내 장바구니 조회", description = "현재 로그인된 사용자의 장바구니 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(Principal principal) {
        CartResponseDto cart = cartService.getCart(principal.getName());
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "장바구니 상품 수량 변경", description = "장바구니에 담긴 특정 상품의 수량을 변경합니다.")
    @PutMapping("/products/{cartProductId}")
    public ResponseEntity<Void> updateCartProductCount(@Parameter(description = "장바구니 상품 ID") @PathVariable Long cartProductId,
                                                       @Parameter(description = "변경할 수량") @RequestParam int quantity) {
        cartService.updateCartProductCount(cartProductId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 특정 상품을 삭제합니다.")
    @DeleteMapping("/products/{cartProductId}")
    public ResponseEntity<Void> deleteCartProduct(@Parameter(description = "장바구니 상품 ID") @PathVariable Long cartProductId) {
        cartService.deleteCartProduct(cartProductId);
        return ResponseEntity.noContent().build();
    }
}
