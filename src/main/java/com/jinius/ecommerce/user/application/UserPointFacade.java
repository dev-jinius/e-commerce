package com.jinius.ecommerce.user.application;

import com.jinius.ecommerce.user.application.dto.UserPointDto;
import com.jinius.ecommerce.user.domain.User;
import com.jinius.ecommerce.user.domain.UserPointService;
import com.jinius.ecommerce.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * 유저 포인트 서비스
 */
@Service
@RequiredArgsConstructor
public class UserPointFacade {

    private final UserService userService;              //유저 공통 서비스
    private final UserPointService userPointService;    //유저 포인트 서비스

    /**
     * 포인트 조회
     * @param userId
     * @return
     */
    @Transactional
    public UserPointDto getUserPoint(Long userId) {
        return UserPointDto.from(userService.getUser(userId));
    }

    /**
     * 포인트 충전
     * @param userId
     * @param point
     * @return
     */
    @Transactional
    public UserPointDto charge(Long userId, BigInteger point) {
        UserPointDto userPoint = getUserPoint(userId);
        return UserPointDto.from(userPointService.chargePoint(userPoint.toUser(), point));
    }
}
