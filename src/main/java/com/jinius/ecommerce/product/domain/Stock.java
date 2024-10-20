package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Stock {
    private Long productId;
    private Long quantity;

    public Stock decrease(Long id, Long quantity) {
        if (this.quantity < quantity)
            throw new EcommerceException(ErrorCode.NOT_ENOUGH_STOCK);
        return new Stock(
                id,
                this.quantity - quantity
        );
    }
}
