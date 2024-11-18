package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.user.api.UserChargeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService sut;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("유저 ID 없으면, NOT_FOUND_USER 예외 발생")
    void validateUser_NotFoundUserId_Exception() {
        //given
        User notExistUser = Fixture.negativeUser();
        notExistUser.setUserId(100000L);

        //when
        Throwable exception = null;
        try {
            sut.validateUserByUserId(notExistUser.getUserId());
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
    @DisplayName("유저 ID가 음수면, INVALID_PARAMETER 예외 발생")
    void validateUser_INVALID_PARAMETER_Exception() {
        //given
        User notExistUser = Fixture.negativeUser();

        //when
        Throwable exception = null;
        try {
            sut.validateUserByUserId(notExistUser.getUserId());
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert exception instanceof EcommerceException;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.INVALID_PARAMETER;
        assert ((EcommerceException) exception).getErrorCode().getMessage().equals("파라미터를 확인해주세요.");
    }

    @Test
    @DisplayName("유저 ID가 DB에 있다면, User 반환")
    void validateUser_Success() {
        //given
        User user = Fixture.user(1L, 50000);
        BigInteger point = user.getPoint();

        //when
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        User dbUser = sut.validateUserByUserId(user.getUserId());

        //then
        assert dbUser != null;
        assert dbUser.getUserId() == user.getUserId();
        assert dbUser.getPoint() == point;
    }

    @Test
    @DisplayName("유저가 요청한 충전 금액이 0이거나 음수면, INVALID_PARAMETER 예외를 던진다.")
    void chargePoint_requestPointNegative_failed() {
        //given
        UserChargeRequest request = new UserChargeRequest(1L, BigInteger.valueOf(-100000));

        //when
        Throwable exception = null;
        try {
            sut.chargePoint(request);
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException)exception).getErrorCode() == ErrorCode.INVALID_PARAMETER;
    }

    @Test
    @DisplayName("유저가 충전 요청 시 유저 잔액 포인트에 충전 포인트를 더해서 저장하고 저장된 포인트를 반환한다.")
    void chargePoint_success() {
        //given
        UserChargeRequest request = new UserChargeRequest(1L, BigInteger.valueOf(30000));
        User dbUser = Fixture.user(1L, 50000);  //id: 1, chargePoint:50000
        User expectUser = Fixture.user(1L, 80000);

        System.out.println("dbUser.getPoint() = " + dbUser.getPoint());
        System.out.println("expectUser.getPoint() = " + expectUser.getPoint());

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(dbUser));
        when(userRepository.save(any(User.class))).thenReturn(expectUser);

        User chargedUser = sut.chargePoint(request);

        //then
        assert chargedUser.getUserId() == request.getUserId();
        assert chargedUser.getPoint().equals(BigInteger.valueOf(80000));
        assert chargedUser.getPoint().equals(expectUser.getPoint());
    }
}