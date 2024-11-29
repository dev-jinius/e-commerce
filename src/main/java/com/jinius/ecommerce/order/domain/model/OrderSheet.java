package com.jinius.ecommerce.order.domain.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;

/**
 * 주문서 (주문 요청 정보)
 */
@Data
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderSheet {
    private Long userId;                        //유저 ID
    private List<OrderItem> orderItems;         //주문상품 리스트
    private BigInteger totalPrice;              //총 주문금액

    //총 주문금액 계산
    public BigInteger calculateOrderTotalPrice(List<OrderItem> items) {
        BigInteger result = items.stream()
                                .map(OrderItem::calculateTotalPrice)
                                .reduce(BigInteger.ZERO, BigInteger::add);

        setTotalPrice(result);
        return result;
    }

    //주문서 출력
    public void log() {
        log.info("======[주문서]======");
        log.info("유저 ID = " + userId);
        orderItems.forEach(item ->
                log.info("상품 ID = " + item.getProductId() + ", 가격 = " + item.getProductPrice() + ", 수량 = " + item.getQuantity())        );
        log.info("총 주문 금액 = " + totalPrice);
        log.info("===================");
    }
}
