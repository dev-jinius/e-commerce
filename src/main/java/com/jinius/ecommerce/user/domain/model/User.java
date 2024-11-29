package com.jinius.ecommerce.user.domain.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Data
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    private Long userId;
    private String name;
    private BigInteger point;
    private int version;          //동시성 제어(낙관적 락)을 위한 버전 추가

    //포인트 충전
    public void addPoint(BigInteger point) {
        this.point = this.point.add(point);
        this.version++;
    }

    //포인트 차감
    public void subtractPoint(BigInteger point) {
        this.point = this.point.subtract(point);
        this.version++;
    }
}
