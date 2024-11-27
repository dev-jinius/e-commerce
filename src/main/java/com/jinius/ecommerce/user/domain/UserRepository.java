package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.user.domain.model.UpdateUser;
import com.jinius.ecommerce.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long userId);

    int updateUserPoint(UpdateUser user);
}
