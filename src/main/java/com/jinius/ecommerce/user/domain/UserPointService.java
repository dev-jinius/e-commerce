package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.common.LockException;
import com.jinius.ecommerce.user.domain.model.UpdateUser;
import com.jinius.ecommerce.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPointService {

    private final UserRepository userRepository;

    /**
     * 포인트 충전
     * @param user
     * @param chargePoint
     * @return
     */
    @Transactional
    public void chargePoint(User user, BigInteger chargePoint) {
        if (userRepository.updateUserPoint(UpdateUser.toChargeUser(user, chargePoint)) == 0)
            throw new OptimisticLockingFailureException("Optimistic locking failed");

        user.addPoint(chargePoint);
        log.info("charge success");
    }

    /**
     * 포인트 사용
     *
     * @param user
     * @param usePoint
     */
    @Transactional
    public void usePoint(User user, BigInteger usePoint) {
        if (userRepository.updateUserPoint(UpdateUser.toUseUser(user, usePoint)) == 0)
            throw new OptimisticLockingFailureException("Optimistic locking failed");

        user.subtractPoint(usePoint);
        log.info("use success");
    }
}
