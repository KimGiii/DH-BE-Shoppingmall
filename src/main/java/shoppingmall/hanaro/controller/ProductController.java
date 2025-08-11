package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.dto.ProductSearchCondition;
import shoppingmall.hanaro.service.ProductService;


@Tag(name = "상품 조회", description = "상품 조회 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "전체 상품 조회 (페이징 적용)", description = "판매 중인 모든 상품 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = productService.findAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "상품 검색", description = "키워드로 상품을 검색하고 페이징하여 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(ProductSearchCondition condition, Pageable pageable) {
        Page<ProductResponseDto> results = productService.searchProducts(condition, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "특정 상품 조회", description = "ID로 특정 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@Parameter(description = "상품 ID") @PathVariable Long productId) {
        ProductResponseDto product = productService.findProductById(productId);
        return ResponseEntity.ok(product);
    }
}
