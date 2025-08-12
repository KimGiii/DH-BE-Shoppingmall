package shoppingmall.hanaro.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.service.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("관리자는 상품 등록이 가능합니다.")
    @WithMockUser(roles = "ADMIN")
    @Disabled("multipart/form-data 테스트 환경 구성 문제로 임시 비활성화")
    void createProduct_success() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());
        doNothing().when(productService).createProduct(any());

        // when & then
        mockMvc.perform(multipart("/api/admin/products")
                        .file(image)
                        .param("name", "관리자 테스트 상품")
                        .param("price", "20000")
                        .param("stockQuantity", "50")
                        .param("description", "설명입니다.")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("관리자는 전체 상품 조회가 가능합니다.")
    @WithMockUser(roles = "ADMIN")
    void getAllProducts() throws Exception {
        // given
        ProductResponseDto product1 = new ProductResponseDto(1L, "상품1", 10000, 10, "설명1", "이미지1");
        ProductResponseDto product2 = new ProductResponseDto(2L, "상품2", 20000, 20, "설명2", "이미지2");
        List<ProductResponseDto> productList = Arrays.asList(product1, product2);

        given(productService.findAllProducts()).willReturn(productList);

        // when & then
        mockMvc.perform(get("/api/admin/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("상품1"))
                .andExpect(jsonPath("$[1].name").value("상품2"));
    }

    @Test
    void createProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void updateStock() {
    }

    @Test
    void deleteProduct() {
    }
}