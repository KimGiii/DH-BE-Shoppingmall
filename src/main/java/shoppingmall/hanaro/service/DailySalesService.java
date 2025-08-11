package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.dto.DailySalesResponseDto;
import shoppingmall.hanaro.repository.DailySalesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailySalesService {

    private final DailySalesRepository dailySalesRepository;

    public List<DailySalesResponseDto> findDailySales(LocalDate startDate, LocalDate endDate) {
        return dailySalesRepository.findBySalesDateBetween(startDate, endDate).stream()
                .map(DailySalesResponseDto::from)
                .collect(Collectors.toList());
    }
}
