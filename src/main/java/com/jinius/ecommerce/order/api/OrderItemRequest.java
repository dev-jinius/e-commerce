package com.jinius.ecommerce.order.api;

import com.jinius.ecommerce.common.validation.ValidNumber;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull(message = "상품 ID는 필수입니다.")
    @ValidNumber
    private Long productId;         //상품 ID

    @NotNull(message = "상품 가격은 필수입니다.")
    @ValidNumber
    private BigInteger price;       //상품 가격

    @NotNull(message = "상품 수량은 필수입니다.")
    @ValidNumber
    private Long quantity;          //상품 수량
}
