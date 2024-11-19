package com.jinius.ecommerce.user.application;

import com.jinius.ecommerce.user.application.dto.ChargeDto;
import com.jinius.ecommerce.user.application.dto.UserPointDto;
import com.jinius.ecommerce.user.domain.UserPointService;
import com.jinius.ecommerce.user.domain.UserService;
import com.jinius.ecommerce.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 유저 포인트 서비스
 */
@Service
@RequiredArgsConstructor
public class UserPointFacade {

    private final UserService userService;              //유저 공통 서비스
    private final UserPointService userPointService;    //유저 포인트 서비스

    /**
     * 유저 포인트 조회
     * @param userId
     * @return
     */
    @Transactional
    public UserPointDto getUserPoint(Long userId) {
        return UserPointDto.from(userService.getUser(userId));
    }

    /**
     * 포인트 충전
     * @param chargeDto
     * @return
     */
    @Transactional
    public UserPointDto charge(ChargeDto chargeDto) {
        //유저 포인트 조회
        UserPointDto userPoint = getUserPoint(chargeDto.getUserId());
        //유저 포인트 충전
        User chargedUser = userPointService.chargePoint(userPoint.toCharge(chargeDto.getChargePoint()));
        return UserPointDto.from(chargedUser);
    }
}
