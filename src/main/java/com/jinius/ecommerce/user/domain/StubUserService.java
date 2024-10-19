package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;

import java.math.BigInteger;

import static com.jinius.ecommerce.common.ErrorCode.NOT_FOUND_USER;

public class StubUserService {

    public User validateUserByUserId(Long userId) {
        if (userId == 1L) {
            return new User(userId, "유저1", BigInteger.valueOf(10000));
        }
        throw new EcommerceException(NOT_FOUND_USER);
    }

    public void comparePoint(BigInteger point) {
        BigInteger dbPoint = BigInteger.valueOf(50000);
        if (dbPoint.compareTo(point) < 0)
            throw new EcommerceException(ErrorCode.NOT_ENOUGH_POINT);
    }
}
