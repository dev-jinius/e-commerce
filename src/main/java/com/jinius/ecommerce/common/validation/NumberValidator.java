package com.jinius.ecommerce.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigInteger;

public class NumberValidator implements ConstraintValidator<ValidNumber, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value instanceof Long && ((Long) value).compareTo(0L) < 0)
            return false;

        if (value instanceof BigInteger && ((BigInteger) value).compareTo(BigInteger.ZERO) < 0)
            return false;

        return true;
    }
}
