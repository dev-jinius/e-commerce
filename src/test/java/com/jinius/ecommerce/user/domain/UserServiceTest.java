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
        User notExistUser = Fixture.notExistUser();

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
        User notExistUser = Fixture.negativeIdUser();

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
        User user = Fixture.existUser();
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
    @DisplayName("유저가 주문 금액보다 잔액 포인트가 부족한 경우 NOT_ENOUGH_POINT 예외 발생")
    void comparePoint_notEnoughPoint_failed() {
        //given
        User user = Fixture.existUser();
        BigInteger userPoint = user.getPoint();

        //when
        Throwable exception = null;
        try {
            sut.comparePoint(user, userPoint.add(BigInteger.valueOf(10000)));
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception != null;
        assert ((EcommerceException) exception).getErrorCode() == ErrorCode.NOT_ENOUGH_POINT;
    }

    @Test
    @DisplayName("유저의 잔액 포인트가 주문 금액과 같거나 주문금액보다 많으면, 예외가 발생하지 않는다.")
    void comparePoint_success() {
        //given
        User user = Fixture.existUser();
        BigInteger userPoint = user.getPoint();

        //when
        Throwable exception = null;
        try {
            sut.comparePoint(user, userPoint.subtract(BigInteger.valueOf(10000)));
        } catch (EcommerceException e) {
            exception = e;
        }

        //then
        assert exception == null;
    }

    @Test
    @DisplayName("유저가 요청한 충전 금액이 0이거나 음수면, INVALID_PARAMETER 예외를 던진다.")
    void chargePoint_requestPointNegative_failed() {
        //given
        UserChargeRequest request = Fixture.chargeRequestId1WithNegativePoint();

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
        UserChargeRequest request = Fixture.chargeRequestId1(); //id: 1, chargePoint:30000
        User dbUser = Fixture.existUser();  //id: 1, chargePoint:50000
        User expectUser = Fixture.chargedExistUser(request.getChargePoint());

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

    @Test
    void test() {
        User sampleUser = Fixture.existUser();
        System.out.println("sampleUser.getUserId() = " + sampleUser.getUserId());
        System.out.println("sampleUser.getName() = " + sampleUser.getName());
        System.out.println("sampleUser.getPoint() = " + sampleUser.getPoint());
    }
}