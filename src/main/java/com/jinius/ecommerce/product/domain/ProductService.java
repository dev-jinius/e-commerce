package com.jinius.ecommerce.product.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.common.ErrorCode;
import com.jinius.ecommerce.order.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 재고 차감 처리
     * @param orderItems
     */
    public void decreaseStock(List<OrderItem> orderItems) {

        if (ObjectUtils.isEmpty(orderItems))
            throw new EcommerceException(ErrorCode.INVALID_PARAMETER);

        for (OrderItem item : orderItems) {
            //재고 조회
            Stock stock = productRepository.findStockById(item.getProductId()).orElseThrow(() -> new EcommerceException(ErrorCode.NOT_FOUND_PRODUCT));
            //재고 차감
            stock.decreaseStock(item.getQuantity());
            //차감한 재고 저장
            productRepository.updateStock(stock);
        }
    }

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
