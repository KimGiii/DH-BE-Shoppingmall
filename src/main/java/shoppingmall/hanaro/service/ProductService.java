package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shoppingmall.hanaro.domain.Product;
import shoppingmall.hanaro.dto.ProductCreateRequestDto;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.dto.ProductUpdateRequestDto;
import shoppingmall.hanaro.exception.BusinessException;
import shoppingmall.hanaro.exception.ErrorCode;
import shoppingmall.hanaro.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    @Transactional
    public void createProduct(ProductCreateRequestDto requestDto) {
        String imageUrl = null;
        try {
            if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
                imageUrl = fileService.uploadFile(requestDto.getImage());
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        Product product = Product.createProduct(requestDto.getName(), requestDto.getPrice(), requestDto.getStockQuantity(), requestDto.getDescription(), imageUrl);
        productRepository.save(product);
        log.info("[Product Log] New Product Created. ID: {}, Name: {}", product.getProductId(), product.getName());
    }

    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }

    public ProductResponseDto findProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return ProductResponseDto.from(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        try {
            if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
                String imageUrl = fileService.uploadFile(requestDto.getImage());
                product.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        
        product.update(requestDto);
        log.info("[Product Log] Product Updated. ID: {}, Name: {}", product.getProductId(), product.getName());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        productRepository.delete(product);
        log.info("[Product Log] Product Deleted. ID: {}", productId);
    }
}
