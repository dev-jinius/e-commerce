package com.jinius.ecommerce.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(),"ERR-000", "파라미터를 확인해주세요."),
    /**
     * 유저 관련 예외
     */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND.value(), "ERR-001", "존재하지 않는 유저입니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST.value(), "ERR-002", "포인트가 부족합니다. 충전해주세요."),

    /**
     * 주문 및 결제 관련 예외
     */
    ALREADY_PAID_ORDER(HttpStatus.BAD_REQUEST.value(), "ERR-101", "이미 결제한 주문입니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND.value(), "ERR-102", "주문 정보가 없습니다."),

    /**
     * 상품 관련 예외
     */
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND.value(), "ERR-201", "상품이 존재하지 않습니다."),
    NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST.value(), "ERR-202", "재고가 부족합니다."),
    ;

    private int status;
    private String code;
    private String message;
}
