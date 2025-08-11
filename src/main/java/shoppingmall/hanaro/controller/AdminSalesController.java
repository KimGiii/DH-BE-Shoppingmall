package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.hanaro.dto.DailySalesResponseDto;
import shoppingmall.hanaro.service.DailySalesService;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "관리자 매출 통계", description = "관리자 매출 통계 관리 API (인증 필요)")
@RestController
@RequestMapping("/api/admin/sales")
@RequiredArgsConstructor
public class AdminSalesController {

    private final DailySalesService dailySalesService;

    @Operation(summary = "기간별 매출 통계 조회", description = "지정된 기간 동안의 일별 매출 통계를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<DailySalesResponseDto>> getDailySales(
            @Parameter(description = "조회 시작일 (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "조회 종료일 (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailySalesResponseDto> dailySales = dailySalesService.findDailySales(startDate, endDate);
        return ResponseEntity.ok(dailySales);
    }
}
