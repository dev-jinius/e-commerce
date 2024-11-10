package com.jinius.ecommerce.product.infra;

import com.jinius.ecommerce.product.domain.ProductRepository;
import com.jinius.ecommerce.product.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductReposirotyImpl implements ProductRepository {

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
    public void updateStock(Stock stock) {
        productJPARepository.save(ProductEntity.fromStock(stock)).toStock();
    }
}
