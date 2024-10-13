package com.jinius.ecommerce.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * 유저 API
 */
@RestController
@RequestMapping("/api/ecommerce/user")
@Tag(name = "User", description = "유저 API")
public class UserController {

    /*
        MOCK API
     */
    @GetMapping("/point/{userId}")
    @Operation(summary = "유저 포인트 조회 API", description = "유저 포인트 조회하기")
    @Schema(description = "유저 포인트 조회 응답")
    public ResponseEntity<UserPointResponse> userPoint(@PathVariable(value = "userId") Long userId) {

        UserPointResponse response = new UserPointResponse(userId, "Iris", BigInteger.valueOf(10000));
        return ResponseEntity.ok().body(response);
    }

    /*
        MOCK API
     */
    @PatchMapping("/point/charge")
    @Operation(summary = "유저 포인트 충전 API", description = "유저 포인트 충전하기")
    @Schema(description = "유저 포인트 충전 응답")
    public ResponseEntity<UserPointResponse> chargePoint(@RequestBody UserPointRequest request) {

        UserPointResponse response = new UserPointResponse(request.getUserId(), request.getUserName(), BigInteger.valueOf(10000).add(request.getPoint()));
        return ResponseEntity.ok().body(response);
    }
}
