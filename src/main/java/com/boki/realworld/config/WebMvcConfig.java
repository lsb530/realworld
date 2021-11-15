package com.boki.realworld.config;

import com.boki.realworld.interceptor.LoginInterceptor;
import com.boki.realworld.resolver.ClientIpArgumentResolver;
import com.boki.realworld.resolver.LoginUserArgumentResolver;
import com.boki.realworld.resolver.OptionalUserArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
//public class WebMvcConfig extends WebMvcConfigurationSupport {

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