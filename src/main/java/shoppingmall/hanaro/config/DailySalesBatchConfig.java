package shoppingmall.hanaro.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import shoppingmall.hanaro.domain.DailySales;
import shoppingmall.hanaro.domain.Order;
import shoppingmall.hanaro.domain.OrderStatus;
import shoppingmall.hanaro.repository.DailySalesRepository;
import shoppingmall.hanaro.repository.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class DailySalesBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final OrderRepository orderRepository;
    private final DailySalesRepository dailySalesRepository;

    @Bean
    public Job dailySalesJob() {
        return new JobBuilder("dailySalesJob", jobRepository)
                .start(dailySalesStep())
                .build();
    }

    @Bean
    public Step dailySalesStep() {
        return new StepBuilder("dailySalesStep", jobRepository)
                .tasklet(dailySalesTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet dailySalesTasklet() {
        return (contribution, chunkContext) -> {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDateTime startOfDay = yesterday.atStartOfDay();
            LocalDateTime endOfDay = yesterday.atTime(23, 59, 59);

            List<Order> orders = orderRepository.findAllByOrderDateBetweenAndStatusNot(
                    startOfDay, endOfDay, OrderStatus.CANCEL);

            int totalSales = orders.stream()
                    .mapToInt(Order::getTotalPrice)
                    .sum();

            DailySales dailySales = DailySales.create(yesterday, totalSales);
            dailySalesRepository.save(dailySales);

            return RepeatStatus.FINISHED;
        };
    }
}
