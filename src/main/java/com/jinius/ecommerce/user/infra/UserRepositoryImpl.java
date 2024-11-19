package com.jinius.ecommerce.user.infra;

import com.jinius.ecommerce.user.domain.model.User;
import com.jinius.ecommerce.user.domain.UserRepository;
import com.jinius.ecommerce.user.infra.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(userEntity -> userEntity.toUser());
    }

    @Override
    public User save(User chargeUser) {
        return userJpaRepository.saveAndFlush(UserEntity.fromUser(chargeUser)).toUser();
    }
}
