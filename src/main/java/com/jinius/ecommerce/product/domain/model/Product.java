package com.jinius.ecommerce.product.domain.model;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product {
    private Long productId;
    private String productName;
    private BigInteger productPrice;
    private Long quantity;
}
