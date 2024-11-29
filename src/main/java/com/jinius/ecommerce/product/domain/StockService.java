package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.RLockHandler;
import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.common.exception.LockException;
import com.jinius.ecommerce.order.domain.OrderRepository;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.product.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 재고 서비스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StockService {

    private static final String LOCK_KEY_PREFIX = "Stock_";
    private final RLockHandler rLockHandler;

    private final ProductRepository productRepository;

    /**
     * 재고 차감 처리 + 분산 락 동시성 제어
     * @param orderItems
     */
    @Transactional(rollbackFor = {EcommerceException.class, LockException.class})
    public void decreaseStock(List<OrderItem> orderItems) {
        if (orderItems == null ||  orderItems.size() <= 0)
            throw new EcommerceException(ErrorCode.INVALID_PARAMETER);

        for (OrderItem item : orderItems) {
            try {
                rLockHandler.callWithLock(LOCK_KEY_PREFIX + item.getProductId(), () -> {
                    //재고 조회
                    Stock stock = productRepository.findStockById(item.getProductId()).orElseThrow(() -> new EcommerceException(ErrorCode.NOT_FOUND_PRODUCT));
                    //재고 차감
                    stock.decreaseStock(item.getQuantity());
                    //차감한 재고 저장
                    productRepository.updateStock(stock);
                }, 5, 10, TimeUnit.SECONDS);
            } catch (LockException e) {
                log.info("Failed to acquire lock for product : [{}]" , item.getProductId());
                throw new LockException(ErrorCode.FAILED_LOCK);
            }
        }
    }
}
