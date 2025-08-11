package shoppingmall.hanaro.dto;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.hanaro.domain.DailySales;

import java.time.LocalDate;

@Getter
@Builder
public class DailySalesResponseDto {
    private LocalDate salesDate;
    private int totalSales;

    public static DailySalesResponseDto from(DailySales dailySales) {
        return DailySalesResponseDto.builder()
                .salesDate(dailySales.getSalesDate())
                .totalSales(dailySales.getTotalSales())
                .build();
    }
}
