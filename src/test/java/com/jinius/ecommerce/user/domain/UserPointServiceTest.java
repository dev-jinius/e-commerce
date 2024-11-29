package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.user.domain.model.UpdateUser;
import com.jinius.ecommerce.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPointServiceTest {
    @InjectMocks
    UserPointService sut;
    @Mock
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = Fixture.user(1L, 100000);
    }

    @Test
    @DisplayName("유저가 충전 요청 시 유저 잔액 포인트에 충전 포인트를 더해서 예외 발생 없이 DB에 저장한다.")
    void chargePoint_success() {
        //given
        BigInteger chargePoint = BigInteger.valueOf(30000);

        //when
        when(userRepository.updateUserPoint(any(UpdateUser.class))).thenReturn(1);
        sut.chargePoint(user, chargePoint);

        //then
        assert user.getPoint().equals(BigInteger.valueOf(130000));
    }
}