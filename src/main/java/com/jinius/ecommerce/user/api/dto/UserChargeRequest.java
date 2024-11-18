package com.jinius.ecommerce.user.api.dto;

import com.jinius.ecommerce.common.validation.ValidNumber;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserChargeRequest {

    @NotNull(message = "유저 ID는 필수입니다.")
    @ValidNumber
    private Long userId;

    @NotNull(message = "충전 포인트는 필수입니다.")
    @ValidNumber
    private BigInteger chargePoint;
}
