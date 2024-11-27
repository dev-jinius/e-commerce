package com.jinius.ecommerce.user.infra;

import com.jinius.ecommerce.user.domain.model.UpdateUser;
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
        return userJpaRepository.findById(userId).map(UserEntity::toUser);
    }

    @Override
    public int updateUserPoint(UpdateUser user) {
        return userJpaRepository.updateUserPoint(user.getUserId(), user.getPoint(), user.getOriginVersion(), user.getNewVersion());
    }
}
