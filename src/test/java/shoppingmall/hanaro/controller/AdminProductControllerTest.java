package shoppingmall.hanaro.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shoppingmall.hanaro.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AdminProductController.class)
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 성공")
    @WithMockUser(roles = "ADMIN")
    void createProduct_success() throws Exception {
        mockMvc.perform(multipart("/api/admin/products")
                        .param("name", "관리자 테스트 상품")
                        .param("price", "20000")
                        .param("stockQuantity", "50")
                        .param("description", "설명입니다.")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 전체 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void getAllProducts_success() throws Exception {
        mockMvc.perform(get("/api/admin/products"))
                .andExpect(status().isOk());
    }
}
