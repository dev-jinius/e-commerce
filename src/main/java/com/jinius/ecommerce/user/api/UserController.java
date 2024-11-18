package com.jinius.ecommerce.user.api;

import com.jinius.ecommerce.user.api.dto.UserChargeRequest;
import com.jinius.ecommerce.user.api.dto.UserPointResponse;
import com.jinius.ecommerce.user.application.UserPointFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 유저 API
 */
@RestController
@RequestMapping("/api/ecommerce/user")
@Tag(name = "User", description = "유저 API")
@RequiredArgsConstructor
public class UserController {

    private final UserPointFacade userPointFacade;

    @GetMapping("/point/{userId}")
    @Operation(summary = "유저 포인트 조회 API", description = "유저 포인트 조회하기")
    @Schema(description = "유저 포인트 조회 응답")
    public ResponseEntity<UserPointResponse> userPoint(@PathVariable(value = "userId") Long userId) {
        return ResponseEntity.ok().body(UserPointResponse.from(userPointFacade.getUserPoint(userId)));
    }

    @PatchMapping("/point/charge")
    @Operation(summary = "유저 포인트 충전 API", description = "유저 포인트 충전하기")
    @Schema(description = "유저 포인트 충전 응답")
    public ResponseEntity<UserPointResponse> chargePoint(@RequestBody @Valid UserChargeRequest request) {

        return ResponseEntity.ok().body(UserPointResponse.from(userPointFacade.charge(request.getUserId(), request.getChargePoint())));
    }
}
