package com.boki.realworld.filter;

import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.config.TokenProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter 거치는중");
        tokenProvider.extract(request.getHeader(AUTHORIZATION_HEADER))
            .ifPresent(token -> {
                String email = tokenProvider.parseSubject(token);
                UserToken userToken = tokenProvider.getUserTokenFrom(token);
                GrantedAuthority adminAuthority =
                    new SimpleGrantedAuthority("admin");
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(adminAuthority);
                Authentication authentication;
                if (email.startsWith("admin")) {
                    authentication = new UsernamePasswordAuthenticationToken(email, userToken,
                        authorities);
                } else {
                    authentication = new UsernamePasswordAuthenticationToken(email, userToken,
                        Collections.emptyList());
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 인증 정보를 저장했습니다");
            });
        filterChain.doFilter(request, response);
    }
}