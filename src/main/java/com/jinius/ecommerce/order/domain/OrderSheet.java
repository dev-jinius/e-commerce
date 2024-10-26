package com.jinius.ecommerce.order.domain;

import com.jinius.ecommerce.order.api.OrderRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static com.jinius.ecommerce.order.domain.OrderStatus.PENDING;

/**
 * 주문서 (주문 요청 정보)
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderSheet {
    private Long userId;                        //유저 ID
    private List<OrderItem> orderItems;         //주문상품 리스트
    private String paymentType;                 //지불 타입 (POINT, CARD, CASH)
    private BigInteger totalPrice;              //총 주문금액
    private OrderStatus orderStatus;                 //주문 상태
    
    //현재 포인트 결제만 가능
    public static OrderSheet from(OrderRequest request) {
        List<OrderItem> orderItems = request.getOrderItems().stream()
                .map(item -> new OrderItem(item.getProductId(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());

        return new OrderSheet(request.getUserId(), orderItems, "POINT", calculateOrderTotalPrice(orderItems), PENDING);
    }

    //총 주문금액 계산
    public static BigInteger calculateOrderTotalPrice(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public void log() {
        System.out.println("======[주문서]======");
        System.out.println("유저 ID = " + userId);
        orderItems.forEach(item ->
                System.out.println("상품 ID = " + item.getProductId() + ", 가격 = " + item.getProductPrice() + ", 수량 = " + item.getQuantity())        );
        System.out.println("총 주문 금액 = " + totalPrice);
        System.out.println("주문 상태 = " + orderStatus);
        System.out.println("===================");
    }
}
