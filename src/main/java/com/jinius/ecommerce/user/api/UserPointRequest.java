package com.jinius.ecommerce.user.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class UserPointRequest {
    private Long userId;
    private String userName;
    private BigInteger point;
}
