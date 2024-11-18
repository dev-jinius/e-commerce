package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stock {
    private Long productId;
    private Long quantity;

    //재고 차감
    public void decreaseStock(Long quantity) {
        //재고 부족
        if (this.quantity < quantity)
            throw new EcommerceException(ErrorCode.NOT_ENOUGH_STOCK);

        //재고 차감
        setQuantity(this.quantity - quantity);
    }
}

