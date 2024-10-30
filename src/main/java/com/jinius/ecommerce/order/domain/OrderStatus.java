package com.jinius.ecommerce.order.domain;

public enum OrderStatus {
    
    PENDING,        //대기 중
    PAID,           //결제 완료
    COMPLETED,      //주문 완료
    CANCELED,        //주문 취소
    PARTIAL_REFUND   //부분 환불/취소
}
