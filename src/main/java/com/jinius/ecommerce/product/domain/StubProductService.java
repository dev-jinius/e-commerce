package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class StubProductService {

    public void decreaseStock(List<OrderItem> orderItems) {
        //DB에 있는 데이터
        List<Stock> stocks = List.of(
                new Stock(1L, 50L),
                new Stock(3L, 50L)
        );

        if (ObjectUtils.isEmpty(stocks))
            throw new EcommerceException(ErrorCode.NOT_FOUND_PRODUCT);


        for (OrderItem item : orderItems) {
            //DB 에서 Select 후 재고 차감해 DB update
        }
    }
}
