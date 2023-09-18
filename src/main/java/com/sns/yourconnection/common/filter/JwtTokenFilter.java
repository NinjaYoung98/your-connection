package com.sns.yourconnection.common.filter;


import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.exception.MissingBearerTokenException;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.security.principal.PrincipalDetailsService;
import com.sns.yourconnection.security.token.JwtTokenGenerator;
import com.sns.yourconnection.service.users.UserService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String PUBLIC_API_PREFIX = "/public-api/";
    private final PrincipalDetailsService principalDetailsService;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (checkPublicApi(request, response, filterChain)) {
            return;
        }
        try {
            String accessToken = parseBearerToken(request);
            UserDetails userDetails = parseUserSpecification(accessToken);
            checkUserBan(userDetails);
            configureAuthenticatedUser(request, userDetails);
        } catch (Exception e) {
            //JwtAuthenticationEntryPoint 에서 jwt 에 대한 상세 예외 핸들링
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    private void checkUserBan(UserDetails userDetails) {

        if (!userDetails.isEnabled()) {
            throw new AppException(ErrorCode.USER_BANNED);
        }
    }

    private boolean checkPublicApi(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        if (request.getRequestURI().startsWith(PUBLIC_API_PREFIX)) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private String parseBearerToken(HttpServletRequest request) throws MissingBearerTokenException {
        String authorization = extractAuthorization(request);
        log.info("[JwtTokenFilter] Extract authorization for Jwt token: {}", authorization);
        return authorization.split(" ")[1];
    }

    private UserDetails parseUserSpecification(String accessToken) {
        String username = jwtTokenGenerator.getUsername(accessToken);
        return principalDetailsService.loadUserByUsername(username);
    }

    private void configureAuthenticatedUser(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String extractAuthorization(HttpServletRequest request)
        throws MissingBearerTokenException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER)) {
            throw new MissingBearerTokenException(ErrorCode.NOT_BEARER_TOKEN);
        }
        return authorization;
    }
}

