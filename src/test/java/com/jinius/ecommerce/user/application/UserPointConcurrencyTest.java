package com.jinius.ecommerce.user.application;

import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.common.exception.LockException;
import com.jinius.ecommerce.user.application.dto.ChargeDto;
import com.jinius.ecommerce.user.domain.UserService;
import com.jinius.ecommerce.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 따닥 방지용 포인트 충전, 사용 동시성 제어 테스트
 */
@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserPointConcurrencyTest {

    @Autowired
    UserPointFacade sut;

    @Autowired
    UserService userService;

    @Container
    private static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379)
            .withEnv("REDIS_PASSWORD", "redis")
            .withReuse(true)
            .withCommand("redis-server --requirepass redis");

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
        registry.add("spring.data.redis.password", () -> "redis");
    }

    @BeforeEach
    void setUp() {
        REDIS_CONTAINER.start();
    }

    @Test
    @DirtiesContext // 스프링 컨텍스트 공유 방지
    @DisplayName("동일한 사용자의 포인트를 충전하려는 3개의 독립적인 트랜잭션이 동시에 실행되는 경우, 선점된 1개 트랜잭션 외의 요청은 LockException 예외를 던지며 충전에 실패한다.")
    public void chargePoint_concurrency_LockException() {
        //given
        Long userId = 1L;
        BigInteger chargePoint = BigInteger.valueOf(50000);
        User requestUser = userService.getUser(userId);

        //비동기 작업 생성 (독립된 CompletableFuture, User 객체 사용, runAsync()로 실행 순서 관계 없이 비동기 작업 실행)
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> sut.charge(ChargeDto.builder().userId(userId).chargePoint(chargePoint).build()));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> sut.charge(ChargeDto.builder().userId(userId).chargePoint(chargePoint).build()));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> sut.charge(ChargeDto.builder().userId(userId).chargePoint(chargePoint).build()));

        List<CompletableFuture<Void>> futures = List.of(future1, future2, future3); //비동기 작업 목록

        Throwable exception = null;
        try {
            futures.forEach(CompletableFuture::join);
        } catch (CompletionException e) {   //CompletableFuture 의 비동기 작업 처리(join(), get())할 때 발생한 예외는 통합적으로 CompletionException로 감싼다.
            exception = e.getCause();       //실제 로직에서 발생한 예외를 가져오는 코드.
        } catch (Exception e) {
            exception = e;
        }

        //then
        User result = userService.getUser(userId);

        assert exception != null;
        assert exception instanceof LockException;
        assert ((LockException) exception).getErrorCode() == ErrorCode.FAILED_LOCK;
        assertThat(result.getPoint()).isEqualTo(requestUser.getPoint().add(chargePoint));
    }

    @Test
    @DirtiesContext // 스프링 컨텍스트 공유 방지
    @DisplayName("동일한 사용자의 포인트를 충전하려는 3개의 독립적인 트랜잭션이 동시에 실행되는 경우, 선착순으로 1개의 트랜잭션만 성공한다.")
    public void chargePoint_concurrency_countingSuccess() {
        //given
        Long userId = 1L;
        BigInteger chargePoint = BigInteger.valueOf(50000);
        User requestUser = userService.getUser(userId);

        //비동기 작업 생성 (독립된 CompletableFuture, User 객체 사용, runAsync()로 실행 순서 관계 없이 비동기 작업 실행)
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> sut.charge(ChargeDto.builder().userId(userId).chargePoint(chargePoint).build()));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> sut.charge(ChargeDto.builder().userId(userId).chargePoint(chargePoint).build()));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> sut.charge(ChargeDto.builder().userId(userId).chargePoint(chargePoint).build()));

        List<CompletableFuture<Void>> futures = List.of(future1, future2, future3); //비동기 작업 목록

        long successCount = futures.stream()
                .filter(future -> {
                    try {
                        future.join();
                        return true;
                    } catch (CompletionException e) {
                        return false;
                    }
                })
                .count();

        //then
        User result = userService.getUser(userId);

        assert successCount == 1;
        assert result.getPoint().equals(requestUser.getPoint().add(chargePoint));
    }
}