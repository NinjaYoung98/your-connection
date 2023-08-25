package com.sns.yourconnection.utils;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import org.springframework.data.domain.Pageable;

public class PageUtil {

    public static Pageable validatePageSize(Pageable pageable) {
        if (pageable.getPageSize() > 20) {
            throw new AppException(ErrorCode.PAGE_SIZE_NOT_APPLICABLE);
        }
        return pageable;
    }
}
