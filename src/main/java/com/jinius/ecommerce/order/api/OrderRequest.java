package com.jinius.ecommerce.order.api;

import com.jinius.ecommerce.common.validation.ValidNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "유저 ID는 필수입니다.")
    @ValidNumber
    private Long userId;                        //유저 ID

    @Valid
    private List<OrderItemRequest> orderItems;  //주문 상품 목록
}
