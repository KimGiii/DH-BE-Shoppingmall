package shoppingmall.hanaro.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import shoppingmall.hanaro.domain.Product;
import shoppingmall.hanaro.dto.ProductCreateRequestDto;
import shoppingmall.hanaro.repository.ProductRepository;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FileService fileService;

    @Test
    @DisplayName("상품 등록 성공")
    void createProduct_success() throws IOException {
        // given
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto(
                "테스트 상품", 10000, 100, "테스트 상품 설명입니다.",
                new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0])
        );

        when(fileService.uploadFile(any())).thenReturn("test_image_url.jpg");

        // when
        productService.createProduct(requestDto);

        // then
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
