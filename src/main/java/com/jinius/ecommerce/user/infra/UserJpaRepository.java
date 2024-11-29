package com.jinius.ecommerce.user.infra;

import com.jinius.ecommerce.user.infra.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @Modifying
    @Query("UPDATE UserEntity u SET u.point = :point, u.version = :newVersion WHERE u.id = :id AND u.version = :originVersion")
    int updateUserPoint(@Param("id") Long id, @Param("point") BigInteger point, @Param("originVersion") int originVersion, @Param("newVersion") int newVersion);

//    @Lock(LockModeType.OPTIMISTIC)
//    @Query("SELECT u FROM UserEntity u WHERE u.id = :userId")
//    Optional<User> findByIdOptimisticLock(Long userId);
}
