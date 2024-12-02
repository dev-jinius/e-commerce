package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.exception.EcommerceException;
import com.jinius.ecommerce.common.exception.ErrorCode;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import com.jinius.ecommerce.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 상품 서비스
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 전체 상품 조회
     * @return
     */
    @Transactional
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 인기 TOP 5 상품 조회
     * @return
     */
    @Transactional
    @Cacheable(value = "products", key = "'top5Products'", cacheManager = "cacheManager")
    public List<Product> getTop5Products() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(3);
        Pageable limit = PageRequest.of(0, 5);

        List<Product> list = productRepository.findTop5ItemsLast3Days(startDate, endDate, limit);

        if (list.isEmpty()) throw new EcommerceException(ErrorCode.NOT_FOUND_PRODUCT);
        if (list.size() > 5) throw new EcommerceException(ErrorCode.EXCEED_COUNT);

        return list;
    }

    /**
     * 재고 차감 후 인기 상품 캐시 갱신을 위한 메서드
     * @param orderItems
     * @return
     */
    @CachePut(value = "products", key = "'top5Products'", cacheManager = "cacheManager")
    public List<Product> updateCacheAfterDecreaseStock(List<OrderItem> orderItems) {
        // 캐시에서 갱신될 상품 리스트
        return orderItems.stream()
                .map(item -> productRepository.findById(item.getProductId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
