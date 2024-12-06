package com.jinius.ecommerce.product.api;

import com.jinius.ecommerce.product.api.dto.ProductResponse;
import com.jinius.ecommerce.product.application.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ecommerce/product")
@Tag(name = "Product", description = "상품 API")
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacade productFacade;

    @GetMapping("")
    @Operation(summary = "전체 상품 목록 조회 API", description = "전체 상품 목록 조회하기")
    @Schema(description = "전체 상품 목록 응답")
    public ResponseEntity<List<ProductResponse>> productList() {
        return ResponseEntity.ok().body(
                productFacade.getProducts().stream().map(ProductResponse::from).collect(Collectors.toList())
        );
    }

    @GetMapping("/top5")
    @Operation(summary = "TOP 5 상품 목록 조회 API", description = "인기 상품 상위 5개 목록 조회하기")
    @Schema(description = "인기 상품 상위 5개 목록 응답")
    public ResponseEntity<List<ProductResponse>> topProductList() {
        return ResponseEntity.ok().body(
                productFacade.getTop5Products().stream().map(ProductResponse::from).collect(Collectors.toList())
        );
    }
    @GetMapping("/top5/noCache")
    @Operation(summary = "캐시 없이 TOP 5 상품 목록 조회 API", description = "캐시 없이 인기 상품 상위 5개 목록 조회하기")
    @Schema(description = "캐시 없이 인기 상품 상위 5개 목록 응답")
    public ResponseEntity<List<ProductResponse>> topProductListNoCache() {
        return ResponseEntity.ok().body(
                productFacade.getTop5ProductsNoCache().stream().map(ProductResponse::from).collect(Collectors.toList())
        );
    }
}
