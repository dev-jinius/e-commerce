package com.jinius.ecommerce.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/order")
@Tag(name = "Order", description = "주문 API")
public class OrderController {

    @PostMapping("")
    @Operation(summary = "주문 생성 API", description = "새로운 주문을 생성합니다.")
    @Schema(description = "주문 생성 응답")
    public ResponseEntity<Object> order(@RequestBody OrderRequest request) {

        List<OrderItemResponse> orderItems = List.of(
            new OrderItemResponse(1L, request.getOrderItems().get(0).getProductId(), BigInteger.valueOf(49900), 1L),
            new OrderItemResponse(2L, request.getOrderItems().get(1).getProductId(), BigInteger.valueOf(30000), 2L)
        );

        OrderResponse response = new OrderResponse(
            1L, "PENDING", BigInteger.valueOf(109900), LocalDateTime.now(), orderItems
        );

        return ResponseEntity.ok().body(response);
    }
}