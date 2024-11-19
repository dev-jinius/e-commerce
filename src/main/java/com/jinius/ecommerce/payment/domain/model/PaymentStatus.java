package com.jinius.ecommerce.payment.domain.model;

//포인트 결제 상태

public enum PaymentStatus {
    PNEDING,            //결제 대기     //포인트 결제 시 PENDING 절차가 없음.
    PAID,               //결제 완료
    FAILED,             //결제 실패
    REFUNDED,           //전체 환불
    PARTIAL_REFUND;     //부분 환불
}
