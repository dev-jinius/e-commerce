package com.jinius.ecommerce.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/product")
@Tag(name = "Product", description = "상품 API")
public class ProductController {

    @GetMapping("")
    @Operation(summary = "전체 상품 목록 조회 API", description = "전체 상품 목록 조회하기")
    @Schema(description = "전체 상품 목록 응답")
    public ResponseEntity<List<ProductResponse>> productList() {
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "바람막이", BigInteger.valueOf(49900), 30L),
                new ProductResponse(2L, "청자켓", BigInteger.valueOf(59900), 30L),
                new ProductResponse(3L, "아노락", BigInteger.valueOf(40000), 30L),
                new ProductResponse(4L, "블루종", BigInteger.valueOf(39900), 30L),
                new ProductResponse(5L, "후드티", BigInteger.valueOf(50000), 30L),
                new ProductResponse(6L, "반팔티", BigInteger.valueOf(20000), 30L),
                new ProductResponse(7L, "슬랙스", BigInteger.valueOf(45000), 30L),
                new ProductResponse(8L, "카고", BigInteger.valueOf(33000), 30L),
                new ProductResponse(9L, "조거팬츠", BigInteger.valueOf(48000), 30L),
                new ProductResponse(10L, "셋업", BigInteger.valueOf(100000), 30L),
                new ProductResponse(11L,    "양말", BigInteger.valueOf(3000), 100L)
        );
        return ResponseEntity.ok().body(products);
    }
}
