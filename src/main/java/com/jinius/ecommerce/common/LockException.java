package com.jinius.ecommerce.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LockException extends RuntimeException {
    private ErrorCode errorCode;
}
