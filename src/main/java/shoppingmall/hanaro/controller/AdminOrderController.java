package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.hanaro.dto.OrderResponseDto;
import shoppingmall.hanaro.service.OrderService;

import java.util.List;

@Tag(name = "관리자 주문 관리", description = "관리자 주문 관리 API (인증 필요)")
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "전체 주문 목록 조회", description = "모든 사용자의 주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> allOrders = orderService.findAllOrders();
        return ResponseEntity.ok(allOrders);
    }
}
