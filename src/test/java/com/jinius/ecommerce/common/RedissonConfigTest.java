package com.jinius.ecommerce.common;

import com.jinius.ecommerce.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RedissonConfigTest extends IntegrationTestSupport {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    @DisplayName("Spring Context가 RedissonClient 빈을 주입하는지 확인하는 테스트")
    void beanInject() {
        assertNotNull(redissonClient);
    }

    @Test
    @DisplayName("Redisson 클라이언트에서 Redis 서버 연결 테스트")
    public void testRedissonClientConnection() {

        RMap<String, String> map = redissonClient.getMap("testMap");
        map.put("key", "value");

        // Redis에 데이터가 제대로 저장되었는지 확인
        String value = map.get("key");
        assertEquals("value", value);
    }
}