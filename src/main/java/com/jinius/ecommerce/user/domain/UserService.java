package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.jinius.ecommerce.common.ErrorCode.NOT_FOUND_USER;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 유저 ID로 유저 조회
     * @param userId
     * @return
     */
    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EcommerceException(NOT_FOUND_USER));
    }
}
