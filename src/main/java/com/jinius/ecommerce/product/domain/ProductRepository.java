package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.product.domain.model.Product;
import com.jinius.ecommerce.product.domain.model.Stock;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Stock> findStockById(Long productId);

    int updateStock(Stock stock);

    List<Stock> findStocksByIdIn(List<Long> productIds);

    List<Product> findTop5ItemsLast3Days(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<Product> findAll();

    Optional<Product> findById(Long productId);
}
