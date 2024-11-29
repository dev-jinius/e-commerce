package com.jinius.ecommerce.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}