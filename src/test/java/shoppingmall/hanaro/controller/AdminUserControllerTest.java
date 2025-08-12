package shoppingmall.hanaro.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.Address;
import shoppingmall.hanaro.domain.User;
import shoppingmall.hanaro.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 2명 생성
        userRepository.save(User.createUser("user1", "pw1", "유저1", new Address("c1", "s1", "z1")));
        userRepository.save(User.createUser("user2", "pw2", "유저2", new Address("c2", "s2", "z2")));
    }

    @DisplayName("관리자는 모든 사용자의 목록을 조회할 수 있다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAllUsers() throws Exception {
        // when & then
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("유저1"))
                .andExpect(jsonPath("$[1].name").value("유저2"))
                .andDo(print());
    }
}