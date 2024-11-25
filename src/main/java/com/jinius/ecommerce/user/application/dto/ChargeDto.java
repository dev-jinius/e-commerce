package com.jinius.ecommerce.user.application.dto;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChargeDto {
    private Long userId;
    private BigInteger chargePoint;
}
