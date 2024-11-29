package com.jinius.ecommerce.common.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 락 해제 이벤트 리스너
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RLockEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void subscribeUnlock(RLockEvent event) {
        try {
            event.unlock();
            log.info("unlock success. key={}", event.getKey());
        } catch (IllegalMonitorStateException e) {
            log.info("already unlocked. key={}", event.getLock());
        }
    }
}
