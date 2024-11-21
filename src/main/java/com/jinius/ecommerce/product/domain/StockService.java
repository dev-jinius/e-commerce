package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.product.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 재고 서비스
 */
@Component
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;

    /**
     * 재고 차감 처리
     * @param orderItems
     */
    @Transactional
    public void decreaseStock(List<OrderItem> orderItems) {

        if (ObjectUtils.isEmpty(orderItems))
            throw new EcommerceException(ErrorCode.INVALID_PARAMETER);

        for (OrderItem item : orderItems) {
            //재고 조회
            Stock stock = productRepository.findStockById(item.getProductId()).orElseThrow(() -> new EcommerceException(ErrorCode.NOT_FOUND_PRODUCT));
            //재고 차감
            stock.decreaseStock(item.getQuantity());
            //차감한 재고 저장
            productRepository.updateStock(stock);
        }
    }
}
