package shoppingmall.hanaro.dto;

import lombok.Builder;
import shoppingmall.hanaro.domain.DailySales;

import java.time.LocalDate;

@Builder
public record DailySalesResponseDto(
        LocalDate salesDate,
        int totalSales
) {
    public static DailySalesResponseDto from(DailySales dailySales) {
        return new DailySalesResponseDto(
                dailySales.getSalesDate(),
                dailySales.getTotalSales()
        );
    }
}
