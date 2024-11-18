package com.jinius.ecommerce.order.infra;

import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.OrderRepository;
import com.jinius.ecommerce.order.domain.model.OrderSheet;
import com.jinius.ecommerce.order.domain.model.OrderStatus;
import com.jinius.ecommerce.order.infra.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository @Primary
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order create(OrderSheet orderSheet) {
        return orderJpaRepository.saveAndFlush(OrderEntity.fromOrderSheet(orderSheet)).toDomain(orderSheet);
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus status) {
        orderJpaRepository.updateStatus(orderId, status);
    }
}
