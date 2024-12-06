package com.jinius.ecommerce.product.infra;

import com.jinius.ecommerce.product.infra.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductJPARepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findStocksByIdIn(List<Long> itemIds);

    @Query("SELECT p.id AS productId, p.name AS productName, p.price AS productPrice, p.quantity AS stockQuantity " +
        "FROM ProductEntity p " +
        "JOIN OrderItemEntity oi ON p.id = oi.productId " +
        "JOIN OrderEntity o ON o.id = oi.orderId " +
        "WHERE o.updatedAt BETWEEN :startDate AND :endDate " +
        "AND o.status = 'COMPLETED' " +
        "GROUP BY p.id, p.name, p.price, p.quantity " +
        "ORDER BY SUM(oi.quantity) DESC")
    Page<ProductEntity> findTop5ItemsLast3Days(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ProductEntity p " +
            "SET p.quantity = :quantity " +
            "WHERE p.id = :id")
    int updateStock(@Param("id") Long id, @Param("quantity") Long quantity);
}