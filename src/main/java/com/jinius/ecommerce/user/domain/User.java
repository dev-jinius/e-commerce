package com.jinius.ecommerce.user.domain;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    private Long userId;
    private String name;
    private BigInteger point;

    public void addPoint(BigInteger chargePoint) {
        setPoint(this.point.add(chargePoint));
    }
}
