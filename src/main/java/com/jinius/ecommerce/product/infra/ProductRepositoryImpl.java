package com.jinius.ecommerce.product.infra;

import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.product.domain.model.Product;
import com.jinius.ecommerce.product.domain.ProductRepository;
import com.jinius.ecommerce.product.domain.model.Stock;
import com.jinius.ecommerce.product.infra.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJPARepository productJPARepository;

    @Override
    public Optional<Stock> findStockById(Long productId) {
        return productJPARepository.findById(productId).map(ProductEntity::toStock);
    }

    @Override
    public List<Stock> findStocksByIdIn(List<Long> productIds) {
        return productJPARepository.findStocksByIdIn(productIds).stream()
                .map(ProductEntity::toStock)
                .toList();
    }

    @Override
    public List<Product> findTop5ItemsLast3Days(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return productJPARepository.findTop5ItemsLast3Days(startDate, endDate, pageable).map(ProductEntity::toProduct).toList();
    }

    @Override
    public List<Product> findAll() {
        return productJPARepository.findAll().stream()
                .map(ProductEntity::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public int updateStock(Stock stock) {
        return productJPARepository.updateStock(stock.getProductId(), stock.getQuantity());
    }
}
