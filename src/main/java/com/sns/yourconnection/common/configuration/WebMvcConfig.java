package com.sns.yourconnection.common.configuration;

import com.sns.yourconnection.common.converter.ContentActivityConverter;
import com.sns.yourconnection.common.converter.UserActivityConverter;
import com.sns.yourconnection.common.interceptor.RateLimitingInterceptor;
import com.sns.yourconnection.common.resolver.AuthenticateUserResolver;
import com.sns.yourconnection.common.resolver.ValidatedPageRequestResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final RateLimitingInterceptor rateLimitingInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Stream.of(
            new UserActivityConverter(),
            new ContentActivityConverter()
        ).forEach(registry::addConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        Stream.of(
            new AuthenticateUserResolver(),
            new ValidatedPageRequestResolver()
        ).forEach(resolvers::add);
    }
}
