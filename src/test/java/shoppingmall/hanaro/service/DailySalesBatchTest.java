package shoppingmall.hanaro.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shoppingmall.hanaro.domain.Order;
import shoppingmall.hanaro.domain.OrderStatus;
import shoppingmall.hanaro.repository.OrderRepository;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.repository.UserRepository;
import shoppingmall.hanaro.domain.Product;
import shoppingmall.hanaro.repository.ProductRepository;
import shoppingmall.hanaro.domain.OrderItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
public class DailySalesBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Job dailySalesJob;

    private final String LOG_FILE_PATH = "./logs/business_sales.log";

    @BeforeEach
    void setUp() throws Exception {
        jobLauncherTestUtils.setJob(dailySalesJob);
        // 테스트 데이터 생성
        createTestData();
    }

    @AfterEach
    void tearDown() throws IOException {
        orderRepository.deleteAll();
        productRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        Files.deleteIfExists(Paths.get(LOG_FILE_PATH));
        Path dailyLogFile = Paths.get("./logs/business_sales." + LocalDate.now().toString() + ".log");
        Files.deleteIfExists(dailyLogFile);
    }

    @Test
    void dailySalesJob() throws Exception {
        // Given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("runDate", LocalDateTime.now().toString())
                .toJobParameters();

        // When
        jobLauncherTestUtils.launchJob(jobParameters);

        // Then
        Path logFile = Paths.get(LOG_FILE_PATH);
        assertThat(logFile).exists();

        List<String> lines = Files.readAllLines(logFile);
        long yesterdayTotalSales = 30000;

        assertThat(lines).anyMatch(line -> line.contains("일일 총 매출: " + yesterdayTotalSales));
    }

    private void createTestData() {
        User testUser = User.builder().build();
        userRepository.save(testUser);

        Product product1 = Product.createProduct("Test Product 1", 10000, 10, "Desc 1", "url1");
        Product product2 = Product.createProduct("Test Product 2", 20000, 10, "Desc 2", "url2");
        Product product3 = Product.createProduct("Test Product 3", 5000, 10, "Desc 3", "url3");
        productRepository.saveAll(List.of(product1, product2, product3));

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        OrderItem orderItem1 = OrderItem.createOrderItem(product1, 10000, 1);
        Order order1 = Order.createOrder(testUser, null, orderItem1);
        order1.updateStatus(OrderStatus.PAYMENT_COMPLETED);
        order1.setOrderDate(yesterday);


        OrderItem orderItem2 = OrderItem.createOrderItem(product2, 20000, 1);
        Order order2 = Order.createOrder(testUser, null, orderItem2);
        order2.updateStatus(OrderStatus.PAYMENT_COMPLETED);
        order2.setOrderDate(yesterday);

        OrderItem orderItem3 = OrderItem.createOrderItem(product3, 5000, 1);
        Order cancelledOrder = Order.createOrder(testUser, null, orderItem3);
        cancelledOrder.updateStatus(OrderStatus.CANCEL);
        cancelledOrder.setOrderDate(yesterday);

        orderRepository.saveAll(List.of(order1, order2, cancelledOrder));
    }
}
