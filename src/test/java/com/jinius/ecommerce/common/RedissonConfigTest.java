package com.jinius.ecommerce.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RedissonConfigTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    @DisplayName("Spring Context가 RedissonClient 빈을 주입하는지 확인하는 테스트s")
    void beanInject() {
        assertNotNull(redissonClient);
    }

    @Test
    @DisplayName("RLock으로 분산 락이 정상적으로 작동하는지 확인")
    public void testDistributedLock() throws InterruptedException {
        String lockKey = "testLock";
        RLock lock = redissonClient.getLock(lockKey);

        // 스레드 1: 락을 획득
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 acquired lock");
                Thread.sleep(3000); // 락 유지
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("Thread 1 released lock");
            }
        }).start();

        // 스레드 2: 락을 기다렸다가 획득
        Thread thread2 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 2 acquired lock");
            } finally {
                lock.unlock();
            }
        });
        thread2.start();
        thread2.join(); // 스레드 종료 대기
    }
}