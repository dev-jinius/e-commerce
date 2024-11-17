package com.jinius.ecommerce.order.infra;

import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.order.domain.OrderItemStatus;
import com.jinius.ecommerce.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE OrderEntity " +
            "SET status = :status " +
            "WHERE id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") OrderStatus status);

}
