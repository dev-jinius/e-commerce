package com.jinius.ecommerce.order.infra;

import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.order.domain.OrderRepository;
import com.jinius.ecommerce.order.domain.OrderSheet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository @Primary
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order create(OrderSheet orderSheet) {
        return orderJpaRepository.saveAndFlush(OrderEntity.fromDomain(orderSheet)).toDomain(orderSheet);
    }

    @Override
    public void updateStatus(Order order) {
        orderJpaRepository.save(OrderEntity.fromDomain(order));
    }
}
