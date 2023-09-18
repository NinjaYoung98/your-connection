package com.sns.yourconnection.common.resolver;

import com.sns.yourconnection.common.annotation.ValidatedPageRequest;
import com.sns.yourconnection.utils.validation.PageUtil;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ValidatedPageRequestResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(ValidatedPageRequest.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        PageRequest pageRequest = PageRequest.of(
            Integer.parseInt(webRequest.getParameter("page")),
            Integer.parseInt(webRequest.getParameter("size")));
        return PageUtil.validatePageSize(pageRequest);
    }
}
