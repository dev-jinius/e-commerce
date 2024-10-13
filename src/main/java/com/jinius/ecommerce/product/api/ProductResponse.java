package com.jinius.ecommerce.product.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String productName;
    private BigInteger productPrice;
    private Long stockQuantity;
}
