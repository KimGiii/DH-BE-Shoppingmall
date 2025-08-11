package shoppingmall.hanaro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shoppingmall.hanaro.dto.ProductCreateRequestDto;
import shoppingmall.hanaro.dto.ProductResponseDto;
import shoppingmall.hanaro.dto.ProductStockUpdateRequestDto;
import shoppingmall.hanaro.dto.ProductUpdateRequestDto;
import shoppingmall.hanaro.service.ProductService;

@Tag(name = "관리자 상품 관리", description = "관리자 상품 관리 API (인증 필요)")
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @ModelAttribute ProductCreateRequestDto requestDto) {
        productService.createProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "전체 상품 조회 (페이징 적용)", description = "모든 상품 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = productService.findAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "특정 상품 조회", description = "ID로 특정 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@Parameter(description = "상품 ID") @PathVariable Long productId) {
        ProductResponseDto product = productService.findProductById(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "상품 수정", description = "기존 상품 정보를 수정합니다.")
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId,
                                              @Valid @ModelAttribute ProductUpdateRequestDto requestDto) {
        productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품 재고 수정", description = "특정 상품의 재고 수량을 수정합니다.")
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Void> updateStock(@Parameter(description = "상품 ID") @PathVariable Long productId,
                                              @Valid @RequestBody ProductStockUpdateRequestDto requestDto) {
        productService.updateStock(productId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "상품 ID") @PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
