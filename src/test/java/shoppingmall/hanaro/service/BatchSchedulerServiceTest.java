package shoppingmall.hanaro.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shoppingmall.hanaro.domain.*;
import shoppingmall.hanaro.repository.DailySalesRepository;
import shoppingmall.hanaro.repository.OrderRepository;
import shoppingmall.hanaro.repository.ProductRepository;
import shoppingmall.hanaro.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class BatchSchedulerServiceTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DailySalesRepository dailySalesRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
        orderRepository.deleteAll();
        dailySalesRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @DisplayName("매일 자정 매출 통계를 집계하여 저장한다.")
    @Test
    void runDailySalesJob() throws Exception {
        // given : 어제 날짜에 해당하는 주문 데이터 생성
        User user = userRepository.save(User.createUser("testuser", "pw", "user", new Address("c", "s", "z")));
        Product product = productRepository.save(Product.builder().name("prod").price(1000).stockQuantity(10).build());
        
        // 성공 케이스 2개
        createTestOrder(user, product, LocalDateTime.now().minusDays(1), 20000); // 20000원
        createTestOrder(user, product, LocalDateTime.now().minusDays(1), 30000); // 30000원

        // 실패 케이스 (취소된 주문)
        Order canceledOrder = createTestOrder(user, product, LocalDateTime.now().minusDays(1), 50000);
        canceledOrder.cancel();
        orderRepository.save(canceledOrder);

        // 실패 케이스 (오늘 주문)
        createTestOrder(user, product, LocalDateTime.now(), 100000);

        // when : 잡 실행
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());

        // then : 잡 성공 여부 및 결과 검증
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        DailySales dailySales = dailySalesRepository.findBySalesDate(LocalDate.now().minusDays(1)).orElse(null);
        assertThat(dailySales).isNotNull();
        assertThat(dailySales.getTotalSales()).isEqualTo(50000); // 20000 + 30000
    }

    private Order createTestOrder(User user, Product product, LocalDateTime orderDate, int totalPrice) {
        OrderItem orderItem = OrderItem.createOrderItem(product, totalPrice, 1);
        Delivery delivery = Delivery.createDelivery(user.getAddress());
        Order order = Order.createOrder(user, delivery, orderItem);
        
        // 테스트를 위해 주문 날짜와 총액을 강제로 설정
        order.setOrderDate(orderDate);
        order.setTotalPrice(totalPrice); // Order 엔티티에 setTotalPrice가 필요합니다.

        return orderRepository.save(order);
    }
}