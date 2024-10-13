package com.jinius.ecommerce.user.domain;

import com.jinius.ecommerce.common.EcommerceException;
import com.jinius.ecommerce.user.api.UserPointRequest;

import static com.jinius.ecommerce.common.ErrorCode.NOT_FOUND_USER;

//@Service
public class StubUserService {

    public UserPointRequest checkUser(Long id) {
        throw new EcommerceException(NOT_FOUND_USER);
    }
}
