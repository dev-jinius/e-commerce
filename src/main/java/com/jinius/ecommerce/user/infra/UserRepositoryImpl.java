package com.jinius.ecommerce.user.infra;

import com.jinius.ecommerce.user.domain.User;
import com.jinius.ecommerce.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(userEntity -> userEntity.toDomain());
    }

    @Override
    public User save(User user) {
        return userJpaRepository.saveAndFlush(UserEntity.fromDomain(user)).toDomain();
    }
}
