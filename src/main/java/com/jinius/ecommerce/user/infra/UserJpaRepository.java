package com.jinius.ecommerce.user.infra;

import com.jinius.ecommerce.user.infra.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
