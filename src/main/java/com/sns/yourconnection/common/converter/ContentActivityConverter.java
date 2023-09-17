package com.sns.yourconnection.common.converter;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.model.entity.report.ContentActivity;
import java.util.Locale;
import org.springframework.core.convert.converter.Converter;

public class ContentActivityConverter implements Converter<String, ContentActivity> {

    @Override
    public ContentActivity convert(String source) {
        if (source == null) {
            return ContentActivity.FLAGGED;
        }
        try {
            return ContentActivity.valueOf(source.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.NOT_EXIST_ACTIVITY, e.getMessage());
        }
    }
}
