package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.common.exception.LockException;
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
        // 동시에 동일 유저에 대한 충전 요청 낙관적 락 예외 처리 (따닥 방지)
        try {
            if (userRepository.updateUserPoint(UpdateUser.toChargeUser(user, chargePoint)) == 0)
                throw new OptimisticLockingFailureException("Optimistic locking failed");

            user.addPoint(chargePoint);
            log.info("charge success");
        } catch (OptimisticLockingFailureException e) {
            log.info("[{}] 포인트 충전 낙관적 락 발생 - 동시 충전 요청으로 인한 실패", user.getUserId());
            throw new LockException(ErrorCode.FAILED_LOCK);
        }
    }



    /**
     * 포인트 사용
     *
     * @param user
     * @param usePoint
     */
    @Transactional
    public void usePoint(User user, BigInteger usePoint) {
        try {
            if (userRepository.updateUserPoint(UpdateUser.toUseUser(user, usePoint)) == 0)
                throw new OptimisticLockingFailureException("Optimistic locking failed");

            user.subtractPoint(usePoint);
            log.info("use success");
        } catch (OptimisticLockingFailureException e) {
            log.info("[{}] 포인트 사용 낙관적 락 발생 - 동시 결제 요청으로 인한 실패", user.getUserId());
            throw new LockException(ErrorCode.FAILED_LOCK);
        }
    }
}
