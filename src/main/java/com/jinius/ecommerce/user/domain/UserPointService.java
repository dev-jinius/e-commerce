package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.user.domain.model.Charge;
import com.jinius.ecommerce.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserPointService {

    private final UserRepository userRepository;

    /**
     * 포인트 충전
     * @param charge
     * @return
     */
    @Transactional
    public User chargePoint(Charge charge) {
        return userRepository.save(charge.toChargeUser());
    }
}
