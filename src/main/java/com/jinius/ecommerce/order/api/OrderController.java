package com.jinius.ecommerce.order.api;

import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.order.domain.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/order")
@Tag(name = "Order", description = "주문 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    @Operation(summary = "주문 생성 API", description = "새로운 주문을 생성합니다.")
    @Schema(description = "주문 생성 응답")
    public ResponseEntity<Object> order(@RequestBody @Valid OrderRequest request) {

        //API 테스트
        Order order = orderService.createOrder(request);
        OrderResponse response = OrderResponse.from(order);

        return ResponseEntity.ok().body(response);
    }
}