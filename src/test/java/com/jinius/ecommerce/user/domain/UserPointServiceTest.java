package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.user.domain.model.Charge;
import com.jinius.ecommerce.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

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
        User dbUser = Fixture.user(userId, 100000);
        Charge requestCharge = new Charge(dbUser, BigInteger.valueOf(30000));
        User expectUser = Fixture.user(userId, 130000);

        //when
        when(userRepository.save(any())).thenReturn(expectUser);
        User chargedUser = sut.chargePoint(requestCharge);

        //then
        assert chargedUser.getUserId() == dbUser.getUserId();
        assert chargedUser.getPoint().equals(expectUser.getPoint());
        assert (dbUser.getPoint().add(requestCharge.getChargePoint())).equals(chargedUser.getPoint());
    }
}