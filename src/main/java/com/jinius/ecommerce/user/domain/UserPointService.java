package com.jinius.ecommerce.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class UserPointService {

    private final UserRepository userRepository;

    /**
     * 포인트 충전
     * @param user
     * @param point
     * @return
     */
    @Transactional
    public User chargePoint(User user, BigInteger point) {
        user.addPoint(point);
        return userRepository.save(user);
    }
}
