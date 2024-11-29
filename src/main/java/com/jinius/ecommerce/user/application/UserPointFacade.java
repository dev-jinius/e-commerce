package com.jinius.ecommerce.user.application;

import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.common.exception.LockException;
import com.jinius.ecommerce.user.application.dto.ChargeDto;
import com.jinius.ecommerce.user.application.dto.UserPointDto;
import com.jinius.ecommerce.user.domain.UserPointService;
import com.jinius.ecommerce.user.domain.UserService;
import com.jinius.ecommerce.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 유저 포인트 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPointFacade {

    private final UserService userService;              //유저 공통 서비스
    private final UserPointService userPointService;    //유저 포인트 서비스

    private static final int MAX_RETRY_COUNT = 3;

    /**
     * 유저 포인트 조회
     */
    @Transactional
    public UserPointDto getUserPoint(Long userId) {
        return UserPointDto.from(userService.getUser(userId));
    }

    /**
     * 포인트 충전
     */
    @Transactional
    public UserPointDto charge(ChargeDto chargeDto) {
        //유저 포인트 조회
        User user = userService.getUser(chargeDto.getUserId());
        //유저 포인트 충전
        userPointService.chargePoint(user, chargeDto.getChargePoint());
        return UserPointDto.from(user);
    }
}