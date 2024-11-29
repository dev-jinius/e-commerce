package com.jinius.ecommerce.user.infra.entity;

import com.jinius.ecommerce.user.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(nullable = false, columnDefinition = "BIGINT")
    private BigInteger point;

    /**
     * 동시성 제어를 위한 낙관적 락 구현에 사용할 버전 추가
     */
    @Version
    private int version;

    public static UserEntity fromUser(User user) {
        return UserEntity.builder()
                .id(user.getUserId())
                .name(user.getName())
                .point(user.getPoint())
                .version(user.getVersion())
                .build();
    }

    public User toUser() {
        return User.builder()
                .userId(this.id)
                .name(this.name)
                .point(this.point)
                .version(this.version)
                .build();
    }
}
