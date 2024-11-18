package com.jinius.ecommerce.order.infra;

import com.jinius.ecommerce.order.domain.*;
import com.jinius.ecommerce.order.domain.model.Order;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.order.domain.model.OrderItemStatus;
import com.jinius.ecommerce.order.infra.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.jinius.ecommerce.order.domain.model.OrderItemStatus.PREPARING;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public List<OrderItem> create(Order order) {
        List<OrderItemEntity> entities = order.getOrderItems().stream()
                .map((OrderItem oi) -> OrderItemEntity.from(order.getOrderId(), oi, PREPARING))
                .collect(Collectors.toList());

        return orderItemJpaRepository.saveAllAndFlush(entities).stream().map(OrderItemEntity::toOrderItem).toList();
    }

    @Override
    public void updateStatus(List<Long> list, OrderItemStatus status) {
        orderItemJpaRepository.updateStatus(list, status);
    }

    @Override
    public List<Long> findPreparingItems() {
        return orderItemJpaRepository.findPreparingItems(PREPARING);
    }
}
