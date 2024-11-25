package com.jinius.ecommerce.product.application;

import com.jinius.ecommerce.product.application.dto.ProductDto;
import com.jinius.ecommerce.product.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;

    /**
     * 전체 상품 조회
     * @return
     */
    @Transactional
    public List<ProductDto> getProducts() {
        return productService.getProducts().stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
    }

    /**
     * 인기 TOP 5 상품 조회
     *
     * @return
     */
    @Transactional
    public List<ProductDto> getTop5Products() {
        return productService.getTop5Products().stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
    }

}
