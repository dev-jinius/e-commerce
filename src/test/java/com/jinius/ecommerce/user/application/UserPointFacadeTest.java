package com.jinius.ecommerce.user.application;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.user.application.dto.ChargeDto;
import com.jinius.ecommerce.user.application.dto.UserPointDto;
import com.jinius.ecommerce.user.domain.model.UpdateUser;
import com.jinius.ecommerce.user.domain.model.User;
import com.jinius.ecommerce.user.domain.UserPointService;
import com.jinius.ecommerce.user.domain.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static com.jinius.ecommerce.common.ErrorCode.NOT_FOUND_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserPointFacadeTest {

    @Autowired
    private UserPointFacade sut;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("충전 요청 시 유저가 DB에 없는 경우 NOT_FOUND_USER 예외 발생하며, 충전 실패")
    void charge_fail_NOT_FOUND_USER() {
        //given
        Long userId = 1L;
        ChargeDto requestCharge = new ChargeDto(userId, BigInteger.valueOf(50000));
        given(userService.getUser(any(Long.class))).willThrow(new EcommerceException(NOT_FOUND_USER));

        //when
        Throwable exception = null;
        try {
            sut.charge(requestCharge);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert exception instanceof EcommerceException;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_FOUND_USER;
        assert ((EcommerceException) exception).getErrorCode().getMessage().equals("존재하지 않는 유저입니다.");
    }

    @Test
    @DisplayName("충전 요청 시 유저가 DB에 있으면, 충전 성공")
    void charge_success() {
        //given
        Long userId = 1L;
        User dbUser = Fixture.user(userId, 0);
        ChargeDto requestCharge = new ChargeDto(userId, BigInteger.valueOf(50000));

        given(userService.getUser(any(Long.class))).willReturn(dbUser);

        //when
        UserPointDto charged = sut.charge(requestCharge);

        //then
        assert charged != null;
        assert charged.getUserId() == dbUser.getUserId();
        assert charged.getPoint().equals(dbUser.getPoint());
    }
}