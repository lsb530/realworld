package com.boki.realworld.filter;

import com.boki.realworld.common.servletwrapper.RereadableRequestWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomServletWrappingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        log.info("인터셉터 가기 전 CustomServletWrappingFilter 거치는중");
        RereadableRequestWrapper requestWrapper = new RereadableRequestWrapper(request);
        filterChain.doFilter(requestWrapper, response);
    }
}