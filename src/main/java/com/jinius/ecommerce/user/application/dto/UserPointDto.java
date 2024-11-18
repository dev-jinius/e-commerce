package com.jinius.ecommerce.user.application.dto;

import com.jinius.ecommerce.user.domain.User;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserPointDto {
    private Long userId;
    private String userName;
    private BigInteger point;

    public static UserPointDto from(User user) {
        return UserPointDto.builder()
                .userId(user.getUserId())
                .userName(user.getName())
                .point(user.getPoint())
                .build();
    }
    public User toUser() {
        return User.builder()
                .userId(this.userId)
                .name(this.userName)
                .point(this.point)
                .build();
    }

}
