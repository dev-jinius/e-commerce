package com.jinius.ecommerce.product.application;

import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.product.domain.ProductService;
import com.jinius.ecommerce.product.domain.event.CacheUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * 캐시 갱신 입벤트 리스너
 */
@Component
@RequiredArgsConstructor
public class CacheUpdateEventListener {

    private final ProductService productService;

    /**
     * 재고 차감 후 인기 상품 캐시 갱신을 위한 이벤트 리스너
     * @param event
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateTop5ProductsCacheAfterDecreaseStock(CacheUpdateEvent event) {

        // 캐시 갱신
        productService.updateTop5ProductsCache(event.getOrderItems());
    }
}
