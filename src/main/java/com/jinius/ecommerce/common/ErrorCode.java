package com.jinius.ecommerce.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 낙관적 락 예외
     */
    OPTIMISTIC_LOCK(HttpStatus.BAD_REQUEST.value(), "ERR-L01", "요청이 많아 처리에 실패했습니다. 다시 시도해주세요."),

    /**
     * Path 파라미터 Validation 예외
     */
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(),"ERR-000", "파라미터를 확인해주세요."),
    
    /**
     * 유저 관련 예외
     */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND.value(), "ERR-001", "존재하지 않는 유저입니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST.value(), "ERR-002", "포인트가 부족합니다. 충전해주세요."),

    /**
     * 주문 및 결제 관련 예외
     */
    NOT_FOUND_ORDER_SHEET(HttpStatus.BAD_REQUEST.value(), "ERR-101", "주문서 정보가 없습니다."),
    NOT_FOUND_ORDER_ITEMS(HttpStatus.NOT_FOUND.value(), "ERR-102", "주문 상품이 없습니다."),
    INVALID_TOTAL_PRICE(HttpStatus.NOT_FOUND.value(), "ERR-103", "총 주문금액이 0 이하입니다."),
    INVALID_ORDER_STATUS(HttpStatus.NOT_FOUND.value(), "ERR-104", "주문 상태를 확인해주세요."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND.value(), "ERR-105", "주문 정보가 없습니다."),
    ALREADY_PAID_ORDER(HttpStatus.BAD_REQUEST.value(), "ERR-106", "이미 결제한 주문입니다."),
    FAIL_CREATE_ORDER(HttpStatus.NOT_FOUND.value(), "ERR-107", "주문 생성에 실패했습니다."),
    FAIL_CREATE_ORDER_ITEMS(HttpStatus.NOT_FOUND.value(), "ERR-108", "상품 주문 생성에 실패했습니다."),
    NOT_SUPPORT_PAYMENT_TYPE(HttpStatus.BAD_REQUEST.value(), "ERR-109", "지원하지 않는 결제 타입입니다."),

    /**
     * 상품 관련 예외
     */
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND.value(), "ERR-201", "상품이 존재하지 않습니다."),
    NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST.value(), "ERR-202", "재고가 부족합니다."),
    EXCEED_COUNT(HttpStatus.BAD_REQUEST.value(), "ERR-203", "조회할 상품 개수를 초과했습니다."),
    ;

    private int status;
    private String code;
    private String message;
}
