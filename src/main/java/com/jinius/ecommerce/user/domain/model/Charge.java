package com.jinius.ecommerce.user.domain.model;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Charge {
    private User user;
    private BigInteger chargePoint;

    public Long getUserId() {
        return user.getUserId();
    }

    public User toChargeUser() {
        return user.addPoint(chargePoint);
    }
}
