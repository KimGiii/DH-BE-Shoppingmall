package shoppingmall.hanaro.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;
import shoppingmall.hanaro.config.SecurityConfig;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.service.ProductService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 성공")
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
    @DisplayName("상품 전체 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void getAllProducts_success() throws Exception {
        // given
        Page<ProductResponseDto> productPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        given(productService.findAllProducts(any(PageRequest.class))).willReturn(productPage);

        // when & then
        mockMvc.perform(get("/api/admin/products")
                        .param("page", "0")
                        .param("size", "10")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
