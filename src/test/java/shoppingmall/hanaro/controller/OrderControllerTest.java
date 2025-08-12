package shoppingmall.hanaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.*;
import shoppingmall.hanaro.dto.CartProductRequestDto;
import shoppingmall.hanaro.repository.ProductRepository;
import shoppingmall.hanaro.repository.UserRepository;
import shoppingmall.hanaro.service.CartService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("주문 컨트롤러 통합 테스트")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;

    private User testUser;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        Address address = new Address("성남시", "분당구", "12345");
        testUser = User.createUser("testuser", "password123!", "테스트유저", address);
        userRepository.save(testUser);

        testProduct1 = Product.builder().name("테스트상품1").price(10000).stockQuantity(100).build();
        testProduct2 = Product.builder().name("테스트상품2").price(25000).stockQuantity(50).build();
        productRepository.save(testProduct1);
        productRepository.save(testProduct2);
    }

    @Test
    @DisplayName("사용자가 주문을 안하면 주문 조회시 조회되지 않는다.")
    @WithMockUser(username = "testuser", roles = "USER")
    void getMyOrders_WhenNoOrders() throws Exception {
        // when & then
        mockMvc.perform(get("/api/user/orders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andDo(print());
    }

    @Nested
    @DisplayName("사용자는 주문을 생성하거나, 조회, 취소할 수 있다.")
    @WithMockUser(username = "testuser", roles = "USER")
    class OrderProcessTests {

        private Long orderId;

        @BeforeEach
        void setupOrder() throws Exception {
            // given - 장바구니에 상품 추가
            cartService.addProductToCart(testUser.getLoginId(), new CartProductRequestDto(testProduct1.getProductId(), 2)); // 10000 * 2
            cartService.addProductToCart(testUser.getLoginId(), new CartProductRequestDto(testProduct2.getProductId(), 1)); // 25000 * 1

            // when - 주문 생성
            MvcResult result = mockMvc.perform(post("/api/user/orders/from-cart"))
                    .andExpect(status().isCreated())
                    .andReturn();
            
            String responseBody = result.getResponse().getContentAsString();
            orderId = objectMapper.readValue(responseBody, Long.class);
        }

        @Test
        @DisplayName("장바구니 상품으로 주문 생성 후 내 주문 목록 조회")
        void createOrderFromCartAndGetMyOrders() throws Exception {
            // then - 내 주문 목록 조회
            mockMvc.perform(get("/api/user/orders"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].orderId").value(orderId))
                    .andExpect(jsonPath("$.content[0].totalPrice").value(45000)) // (10000 * 2) + (25000 * 1)
                    .andDo(print());
        }
        
        @Test
        @DisplayName("주문 상세 조회")
        void getOrderDetails() throws Exception {
            // then - 주문 상세 조회
            mockMvc.perform(get("/api/user/orders/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderId").value(orderId))
                    .andExpect(jsonPath("$.orderStatus").value(OrderStatus.PAYMENT_COMPLETED.toString()))
                    .andExpect(jsonPath("$.orderItems[?(@.name == '테스트상품1')].quantity").value(2))
                    .andExpect(jsonPath("$.orderItems[?(@.name == '테스트상품2')].quantity").value(1))
                    .andDo(print());
        }

        @Test
        @DisplayName("주문 취소")
        void cancelOrder() throws Exception {
            // when - 주문 취소
            mockMvc.perform(post("/api/user/orders/{orderId}/cancel", orderId))
                    .andExpect(status().isOk());

            // then - 주문 상세 조회로 상태 확인
            mockMvc.perform(get("/api/user/orders/{orderId}", orderId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderStatus").value(OrderStatus.CANCEL.toString()))
                    .andDo(print());
        }
    }
}
