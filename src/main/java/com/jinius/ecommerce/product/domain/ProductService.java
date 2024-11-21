package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Product> getTop5Products() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(3);
        Pageable limit = PageRequest.of(0, 5);

        List<Product> list = productRepository.findTop5ItemsLast3Days(startDate, endDate, limit);

        if (list.size() == 0) throw new EcommerceException(ErrorCode.NOT_FOUND_PRODUCT);
        if (list.size() > 5) throw new EcommerceException(ErrorCode.EXCEED_COUNT);

        return list;
    }
}
