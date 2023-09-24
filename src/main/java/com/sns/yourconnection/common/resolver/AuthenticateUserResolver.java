package com.sns.yourconnection.common.resolver;

import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.security.principal.UserPrincipal;
import com.sns.yourconnection.utils.loader.ClassUtil;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticateUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthUser.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = ClassUtil.castingInstance(authentication.getPrincipal(),
            UserPrincipal.class);
        return User.fromPrincipal(userPrincipal);
    }
}
