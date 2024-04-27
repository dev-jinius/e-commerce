package com.jinius.ecommerce.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(),"ERR-000", "파라미터를 확인해주세요.")
    ;

    private int status;
    private String code;
    private String message;
}
