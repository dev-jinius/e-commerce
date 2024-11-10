package com.jinius.ecommerce.product.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Stock> findStockById(Long productId);

    void updateStock(Stock stock);

    List<Stock> findStocksByIdIn(List<Long> productIds);
}
