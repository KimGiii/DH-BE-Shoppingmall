package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppingmall.hanaro.dto.OrderDetailResponseDto;
import shoppingmall.hanaro.dto.OrderResponseDto;
import shoppingmall.hanaro.dto.PageResponseDto;
import shoppingmall.hanaro.service.OrderService;

import java.security.Principal;

@Tag(name = "주문", description = "사용자 주문 관리 API")
@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "장바구니 상품으로 주문 생성", description = "현재 장바구니에 있는 모든 상품으로 새로운 주문을 생성합니다.")
    @PostMapping("/from-cart")
    public ResponseEntity<Long> createOrderFromCart(Principal principal) {
        Long orderId = orderService.createOrderFromCart(principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @Operation(summary = "내 주문 목록 조회", description = "현재 로그인된 사용자의 모든 주문 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponseDto<OrderResponseDto>> getMyOrders(Principal principal, @ParameterObject Pageable pageable) {
        Page<OrderResponseDto> myOrdersPage = orderService.findMyOrders(principal.getName(), pageable);
        PageResponseDto<OrderResponseDto> response = new PageResponseDto<>(myOrdersPage);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetails(@Parameter(description = "주문 ID") @PathVariable Long orderId) {
        OrderDetailResponseDto orderDetails = orderService.findOrderDetails(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @Operation(summary = "주문 취소", description = "특정 주문을 취소합니다. 배송이 시작된 주문은 취소할 수 없습니다.")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@Parameter(description = "주문 ID") @PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
