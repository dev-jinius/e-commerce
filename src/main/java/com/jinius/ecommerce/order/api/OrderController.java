package com.jinius.ecommerce.order.api;

import com.jinius.ecommerce.order.api.dto.OrderRequest;
import com.jinius.ecommerce.order.api.dto.OrderResponse;
import com.jinius.ecommerce.order.application.OrderFacade;
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

    private final OrderFacade orderFacade;

    @PostMapping("")
    @Operation(summary = "주문 요청 처리", description = "새로운 주문을 생성하고, 결제와 재고 처리를 진행합니다.")
    @Schema(description = "주문 생성 응답")
    public ResponseEntity<OrderResponse> order(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok().body(OrderResponse.from(orderFacade.order(request.toFacade())));
    }
}