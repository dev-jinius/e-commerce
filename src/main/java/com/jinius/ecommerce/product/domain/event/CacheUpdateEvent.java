package com.jinius.ecommerce.product.domain.event;

import com.jinius.ecommerce.order.domain.model.OrderItem;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 캐시 갱신 이벤트
 */
@Getter
public class CacheUpdateEvent extends ApplicationEvent {

    private final List<OrderItem> orderItems;

    /**
     * 인기 상품 캐시 갱신 이벤트
     * @param source
     * @param items
     */
    public CacheUpdateEvent(Object source, List<OrderItem> items) {
        super(source);
        this.orderItems = items;
    }
}
