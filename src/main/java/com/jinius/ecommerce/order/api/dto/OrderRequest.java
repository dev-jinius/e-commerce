package com.jinius.ecommerce.order.api.dto;

import com.jinius.ecommerce.common.validation.ValidNumber;
import com.jinius.ecommerce.order.application.dto.OrderDto;
import com.jinius.ecommerce.order.application.dto.OrderItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "유저 ID는 필수입니다.")
    @ValidNumber
    private Long userId;                        //유저 ID

    @Valid
    private List<OrderItemRequest> orderItems;  //주문 상품 목록

    public OrderDto toFacade() {
        return OrderDto.builder()
                .userId(this.userId)
                .orderItems(this.orderItems.stream()
                        .map(OrderItemRequest::toFacade)
                        .collect(Collectors.toList()))
                .build();
    }
}
