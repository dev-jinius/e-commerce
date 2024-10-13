package com.jinius.ecommerce.user.api;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class UserPointRequest {
    private Long userId;
    private String userName;
    private BigInteger point;
}
