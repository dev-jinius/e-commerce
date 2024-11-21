package com.jinius.ecommerce.product.application.dto;

import com.jinius.ecommerce.product.domain.model.Product;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long productId;
    private String productName;
    private BigInteger productPrice;
    private Long quantity;

    public static ProductDto fromDomain(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
