package shoppingmall.hanaro.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailySales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate salesDate;

    private int totalSales;

    public static DailySales create(LocalDate salesDate, int totalSales) {
        DailySales sales = new DailySales();
        sales.salesDate = salesDate;
        sales.totalSales = totalSales;
        return sales;
    }
}
