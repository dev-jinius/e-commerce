package com.jinius.ecommerce.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * 트랜잭션 종료 이후에 락 해제를 하기 위한 트랜잭션 분리용
 */
@Slf4j
@Component
public class RLockTransactionHandler {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T execute(Supplier<T> supplier) {
        log.info("execute");
        return supplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(Runnable runnable) {
        log.info("execute");
        runnable.run();
    }
}
