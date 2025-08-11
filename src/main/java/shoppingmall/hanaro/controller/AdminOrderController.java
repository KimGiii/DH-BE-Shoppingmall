package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.hanaro.dto.OrderResponseDto;
import shoppingmall.hanaro.dto.OrderSearchCondition;
import shoppingmall.hanaro.service.OrderService;

import java.util.List;

@Tag(name = "관리자 주문 관리", description = "관리자 주문 관리 API (인증 필요)")
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 내역 검색 및 조회", description = "다양한 조건으로 주문을 검색하고 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> searchOrders(OrderSearchCondition condition, Pageable pageable) {
        Page<OrderResponseDto> results = orderService.searchOrders(condition, pageable);
        return ResponseEntity.ok(results);
    }
}
