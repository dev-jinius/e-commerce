package com.jinius.ecommerce.product.api;

import com.jinius.ecommerce.product.domain.Product;
import com.jinius.ecommerce.product.domain.Top5Product;
import lombok.*;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long productId;
    private String productName;
    private BigInteger productPrice;
    private Long stockQuantity;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .stockQuantity(product.getQuantity())
                .build();
    }
}
