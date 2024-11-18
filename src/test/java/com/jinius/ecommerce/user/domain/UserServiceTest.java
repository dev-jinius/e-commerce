package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
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
            sut.getUser(notExistUser.getUserId());
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
    @DisplayName("유저 ID가 DB에 있다면, User 반환")
    void validateUser_Success() {
        //given
        User user = Fixture.user(1L, 50000);
        BigInteger point = user.getPoint();

        //when
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        User dbUser = sut.getUser(user.getUserId());

        //then
        assert dbUser != null;
        assert dbUser.getUserId() == user.getUserId();
        assert dbUser.getPoint() == point;
    }
}