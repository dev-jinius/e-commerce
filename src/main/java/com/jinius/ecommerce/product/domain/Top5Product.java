package com.jinius.ecommerce.product.domain;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Top5Product {
    private Long id;
    private String name;
    private BigInteger price;
    private Long totalQuantity;
}
