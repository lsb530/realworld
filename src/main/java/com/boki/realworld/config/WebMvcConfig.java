package com.boki.realworld.config;

import com.boki.realworld.interceptor.LoginInterceptor;
import com.boki.realworld.resolver.ClientIpArgumentResolver;
import com.boki.realworld.resolver.LoginUserArgumentResolver;
import com.boki.realworld.resolver.OptionalUserArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
//public class WebMvcConfig extends WebMvcConfigurationSupport { // Question 1 => 작동 안됨
public class WebMvcConfig implements WebMvcConfigurer { //Question 2 => 작동 됨

    private final ClientIpArgumentResolver clientIpArgumentResolver;
    private final LoginUserArgumentResolver userArgumentResolver;
    private final OptionalUserArgumentResolver optionalUserArgumentResolver;
    private final LoginInterceptor loginInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(clientIpArgumentResolver);
        argumentResolvers.add(userArgumentResolver);
        argumentResolvers.add(optionalUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
            .addPathPatterns("/api/user/v2", "/api/profiles/**/v2");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("Content-Type", "X-Requested-With", "Authorization", "Cache-Control")
            .maxAge(3600L);
    }

}