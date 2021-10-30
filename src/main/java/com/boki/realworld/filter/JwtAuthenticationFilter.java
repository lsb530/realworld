package com.boki.realworld.filter;

import com.boki.realworld.config.TokenProvider;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.domain.UserRepository;
import com.boki.realworld.api.user.exception.UserNotFoundException;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        log.info("CustomServletWrappingFilter 가기 전 JwtAuthenticationFilter 거치는중");
        tokenProvider.extract(request.getHeader(AUTHORIZATION_HEADER))
            .ifPresentOrElse(token -> {
                String email = tokenProvider.parseSubject(token);
                User user = userRepository.findUserByEmail(email)
                    .orElseThrow(UserNotFoundException::new);
                user.setToken(token);
                GrantedAuthority grantedAuthority =
                    new SimpleGrantedAuthority("admin");
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(grantedAuthority);
                Authentication authentication;
                if(email.startsWith("admin")) {
                    authentication = new UsernamePasswordAuthenticationToken(email,
                        user, authorities);
                } else {
                    authentication = new UsernamePasswordAuthenticationToken(email,
                        user, Collections.emptyList());
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 인증 정보를 저장했습니다");
            }, () -> log.info("헤더 없음"));
        filterChain.doFilter(request, response);
    }
}