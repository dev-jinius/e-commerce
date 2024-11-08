package com.jinius.ecommerce.user.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long userId);

    User save(User user);
}
