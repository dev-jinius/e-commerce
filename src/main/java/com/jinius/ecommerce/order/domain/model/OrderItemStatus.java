package com.jinius.ecommerce.order.domain.model;

public enum OrderItemStatus {
    PREPARING,          //배송 준비 중
    SHIPPING,           //배송 중
    DELIVERED,          //배송 완료
    CANCELED            //취소
}
