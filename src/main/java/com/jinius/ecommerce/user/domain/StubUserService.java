package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.jinius.ecommerce.common.ErrorCode.NOT_FOUND_USER;

@Service
public class StubUserService {

    public User validateUserByUserId(Long userId) {
        if (userId == 1L) {
            return new User(userId, "유저1", BigInteger.valueOf(10000));
        }
        throw new EcommerceException(NOT_FOUND_USER);
    }

    public void comparePoint(User user, BigInteger orderPrice) {
        user.setPoint(BigInteger.valueOf(50000));
        if (user.getPoint().compareTo(orderPrice) < 0)
            throw new EcommerceException(ErrorCode.NOT_ENOUGH_POINT);
    }
}
