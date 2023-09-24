package com.sns.yourconnection.common.configuration.auditor;


import com.sns.yourconnection.security.principal.UserPrincipal;
import com.sns.yourconnection.utils.loader.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    /*
    AuditEntity 의 createBy,updatedBy의 값을 SecurityContext 에 저장된 user의 username으로 등록하기 위함
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getPrincipal)
            .map(principal -> ClassUtil.castingInstance(principal, UserPrincipal.class))
            .map(UserPrincipal::getUsername);
    }
}
