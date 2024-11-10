package com.jinius.ecommerce.product.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductJPARepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findStocksByIdIn(List<Long> itemIds);
}
