package com.jinius.ecommerce.user.api;

import com.jinius.ecommerce.user.domain.User;
import lombok.*;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserPointResponse {
    private Long userId;
    private String userName;
    private BigInteger point;

    public static UserPointResponse from(User user) {
        return UserPointResponse.builder()
                .userId(user.getUserId())
                .userName(user.getName())
                .point(user.getPoint())
                .build();
    }
}
