package com.jinius.ecommerce.common.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RLockEvent {

    private String key;
    private RLock lock;

    /**
     * 락 해제
     */
    void unlock() {
        if (lock.isLocked()) {
            lock.unlock();
        }
    }
}
