package shoppingmall.hanaro.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.hanaro.domain.Product;
import shoppingmall.hanaro.dto.ProductCreateRequestDto;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.dto.ProductSearchCondition;
import shoppingmall.hanaro.dto.ProductStockUpdateRequestDto;
import shoppingmall.hanaro.dto.ProductUpdateRequestDto;
import shoppingmall.hanaro.exception.BusinessException;
import shoppingmall.hanaro.exception.ErrorCode;
import shoppingmall.hanaro.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private static final Logger productLog = LoggerFactory.getLogger("business.product");

    private final ProductRepository productRepository;
    private final FileService fileService;

    @Transactional
    public void createProduct(ProductCreateRequestDto requestDto) {
        productLog.info("[Product Start] Start creating new product. Name: {}", requestDto.name());
        String imageUrl = null;
        try {
            if (requestDto.image() != null && !requestDto.image().isEmpty()) {
                productLog.info("[Product Step] Attempting to upload image for product: {}", requestDto.name());
                imageUrl = fileService.uploadFile(requestDto.image());
                productLog.info("[Product Step] Image uploaded successfully. Image URL: {}", imageUrl);
            }
        } catch (IOException e) {
            productLog.error("[Product Error] Failed to upload image for product: {}", requestDto.name(), e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        Product product = Product.createProduct(requestDto.name(), requestDto.price(), requestDto.stockQuantity(), requestDto.description(), imageUrl);
        productRepository.save(product);
        productLog.info("[Product Step] Product entity created and saved. Product ID: {}", product.getProductId());
        productLog.info("[Product Success] New product created successfully. ID: {}, Name: {}", product.getProductId(), product.getName());
    }

    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }

    public Page<ProductResponseDto> searchProducts(ProductSearchCondition condition, Pageable pageable) {
        return productRepository.search(condition, pageable);
    }

    public ProductResponseDto findProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return ProductResponseDto.from(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto requestDto) {
        productLog.info("[Product Start] Start updating product. ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        productLog.info("[Product Step] Found product to update. ID: {}", productId);

        try {
            if (requestDto.image() != null && !requestDto.image().isEmpty()) {
                productLog.info("[Product Step] Attempting to upload new image for product: {}", productId);
                String imageUrl = fileService.uploadFile(requestDto.image());
                product.setImageUrl(imageUrl);
                productLog.info("[Product Step] New image uploaded successfully. Image URL: {}", imageUrl);
            }
        } catch (IOException e) {
            productLog.error("[Product Error] Failed to upload image for product update. ID: {}", productId, e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        
        product.update(requestDto.name(), requestDto.price(), requestDto.stockQuantity(), requestDto.description());
        productLog.info("[Product Step] Product entity updated.");
        productLog.info("[Product Success] Product updated successfully. ID: {}, Name: {}", product.getProductId(), product.getName());
    }

    @Transactional
    public void updateStock(Long productId, ProductStockUpdateRequestDto requestDto) {
        productLog.info("[Product Start] Start updating stock for product. ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        productLog.info("[Product Step] Found product to update stock. ID: {}", productId);

        product.updateStock(requestDto.getStockQuantity());
        productLog.info("[Product Step] Product stock updated in entity.");
        productLog.info("[Product Success] Stock updated successfully. ID: {}, New Stock: {}", productId, requestDto.getStockQuantity());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productLog.info("[Product Start] Start deleting product. ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        productLog.info("[Product Step] Found product to delete. ID: {}", productId);
        productRepository.delete(product);
        productLog.info("[Product Success] Product deleted successfully. ID: {}", productId);
    }
}
