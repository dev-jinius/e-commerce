package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.Fixture;
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
class UserPointServiceTest {
    @InjectMocks
    UserPointService sut;
    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("유저가 충전 요청 시 유저 잔액 포인트에 충전 포인트를 더해서 저장하고 저장된 포인트를 반환한다.")
    void chargePoint_success() {
        //given
        Long userId = 1L;
        BigInteger chargePoint = BigInteger.valueOf(30000);
        User dbUser = Fixture.user(userId, 50000);  //id: 1, chargePoint:50000
        User expectUser = Fixture.user(userId, 80000);

        //when
        when(userRepository.save(any())).thenReturn(expectUser);
        User chargedUser = sut.chargePoint(dbUser, chargePoint);

        //then
        assert chargedUser.getUserId() == dbUser.getUserId();
        assert (chargedUser.getPoint()).equals(BigInteger.valueOf(80000));
        assert dbUser.getPoint().equals(chargedUser.getPoint());
    }
}