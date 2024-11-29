package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.Fixture;
import com.jinius.ecommerce.user.domain.model.UpdateUser;
import com.jinius.ecommerce.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 포인트 충전, 사용 동시성 제어 테스트
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ConcurrencyTest {

    @Autowired
    UserPointService userPointService;

    @Autowired
    UserService userService;

    @Test
    @DirtiesContext // 스프링 컨텍스트 공유 방지
    @DisplayName("동일한 사용자의 포인트를 충전하려는 3개의 독립적인 트랜잭션이 동시에 실행되는 경우, 선점된 1개 트랜잭션 외의 요청은 OptimisticLockingFailureException 예외를 던지며 충전에 실패한다.")
    public void chargePoint_concurrency_OptimisticLockingFailureException() {
        //given
        Long userId = 1L;
        BigInteger chargePoint = BigInteger.valueOf(50000);
        User requestUser = Fixture.user(userId, 50000);

        //독립적인 트랜잭션으로 실행해야 하므로 CompletableFuture를 공유하는 해당 코드는 독립적인 트랜잭션 테스트에 모순된다.
        //CompletableFuture<Void> createFuture = CompletableFuture.runAsync(() -> userPointService.chargePoint(requestUser, chargePoint));

        //비동기 작업 생성 (독립된 CompletableFuture, User 객체 사용, runAsync()로 실행 순서 관계 없이 비동기 작업 실행)
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(
                () -> userPointService.chargePoint(Fixture.user(userId, requestUser.getPoint().intValue()), chargePoint));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(
                () -> userPointService.chargePoint(Fixture.user(userId, requestUser.getPoint().intValue()), chargePoint));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(
                () -> userPointService.chargePoint(Fixture.user(userId, requestUser.getPoint().intValue()), chargePoint));

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
        System.out.println("result.getPoint() = " + result.getPoint());

        assert exception != null;
        assert exception instanceof OptimisticLockingFailureException;
        assertThat(result.getPoint()).isEqualTo(requestUser.getPoint().add(chargePoint));
    }

    @Test
    @DirtiesContext // 스프링 컨텍스트 공유 방지
    @DisplayName("동일한 사용자의 포인트를 충전하려는 3개의 독립적인 트랜잭션이 동시에 실행되는 경우, 선착순으로 1개의 트랜잭션만 성공한다.")
    public void chargePoint_concurrency_countingSuccess() {
        //given
        Long userId = 1L;
        BigInteger chargePoint = BigInteger.valueOf(50000);
        User requestUser = Fixture.user(userId, 50000);

        //비동기 작업 생성 (독립된 CompletableFuture, User 객체 사용, runAsync()로 실행 순서 관계 없이 비동기 작업 실행)
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(
                () -> userPointService.chargePoint(Fixture.user(userId, requestUser.getPoint().intValue()), chargePoint));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(
                () -> userPointService.chargePoint(Fixture.user(userId, requestUser.getPoint().intValue()), chargePoint));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(
                () -> userPointService.chargePoint(Fixture.user(userId, requestUser.getPoint().intValue()), chargePoint));

        List<CompletableFuture<Void>> futures = List.of(future1, future2, future3); //비동기 작업 목록

        long successCount = futures.stream()
                .filter(future -> {
                    try {
                        future.join();
                        return true;
                    } catch (CompletionException e) {
                        if (e.getCause() instanceof OptimisticLockingFailureException) {
                            return false;
                        }
                        throw e;
                    }
                })
                .count();

        //then
        User result = userService.getUser(userId);
        System.out.println("result.getPoint() = " + result.getPoint());
        System.out.println("successCount = " + successCount);

        assert successCount == 1;
        assert result.getPoint().equals(requestUser.getPoint().add(chargePoint));
    }

    @Test
    @DirtiesContext // 스프링 컨텍스트 공유 방지
    @DisplayName("동일한 사용자의 포인트를 충전하려는 1000개의 독립적인 트랜잭션이 동시에 실행되는 경우, 선착순으로 1개의 트랜잭션만 성공한다.")
    public void chargePoint_concurrency_countingSuccess_1000Request() {
        //given
        Long userId = 1L;
        BigInteger chargePoint = BigInteger.valueOf(50000);
        User requestUser = Fixture.user(userId, 50000);

        //비동기 작업 목록 (독립적인 CompletableFuture 객체와 User 객체 사용, runAysnc()로 실행 순서 관계 없이 비동기 작업 실행)
        List<CompletableFuture<Void>> futures = IntStream.range(0, 1000)
                .mapToObj(i -> CompletableFuture.runAsync(() -> userPointService.chargePoint(Fixture.user(userId, requestUser.getPoint().intValue()), chargePoint)))
                .toList();

        //충전 성공 횟수 카운팅
        long successCount = futures.stream()
                .filter(future -> {
                    try {
                        future.join();
                        return true;
                    } catch (CompletionException e) {
                        if (e.getCause() instanceof OptimisticLockingFailureException) {
                            return false;
                        }
                        throw e;
                    }
                })
                .count();

        //then
        User result = userService.getUser(userId);

        assert successCount == 1;
        assertThat(result.getPoint()).isEqualTo(requestUser.getPoint().add(chargePoint));
    }

    @Test
    @DirtiesContext // 스프링 컨텍스트 공유 방지
    @DisplayName("동일한 사용자의 포인트를 사용하려는 3개의 독립적인 트랜잭션이 동시에 실행되는 경우, 선점된 1개 트랜잭션 외의 요청은 OptimisticLockingFailureException 예외를 던지며 충전에 실패한다.")
    public void usePoint_concurrency_OptimisticLockingFailureException() {
        //given
        Long userId = 1L;
        BigInteger usePoint = BigInteger.valueOf(100);
        User requestUser = Fixture.user(userId, 100);

        //비동기 작업 생성 (독립된 CompletableFuture, User 객체 사용, runAsync()로 실행 순서 관계 없이 비동기 작업 실행)
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(
                () -> userPointService.usePoint(Fixture.user(userId, requestUser.getPoint().intValue()), usePoint));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(
                () -> userPointService.usePoint(Fixture.user(userId, requestUser.getPoint().intValue()), usePoint));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(
                () -> userPointService.usePoint(Fixture.user(userId, requestUser.getPoint().intValue()), usePoint));

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
        System.out.println("result.getPoint() = " + result.getPoint());

        assert exception != null;
        assert exception instanceof OptimisticLockingFailureException;
        assertThat(result.getPoint()).isEqualTo(requestUser.getPoint().subtract(usePoint));
    }
}
