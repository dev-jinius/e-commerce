package com.jinius.ecommerce.user.infra;

import com.jinius.ecommerce.user.domain.User;
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

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .id(user.getUserId())
                .name(user.getName())
                .point(user.getPoint())
                .build();
    }

    public User toDomain() {
        return User.builder()
                .userId(this.id)
                .name(this.name)
                .point(this.point)
                .build();
    }
}
