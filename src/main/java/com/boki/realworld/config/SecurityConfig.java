package com.boki.realworld.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;

import com.boki.realworld.common.exception.jwt.JwtAuthenticationEntryPoint;
import com.boki.realworld.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired // 서블릿 컨테이너에 있는 빈 주입
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
        // 서블릿 컨테이너에 빈 등록
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    String[] pathArray = new String[]{
        "/h2-console/**", "/favicon.ico", "/error", "/webjars/**", "/configuration/**",
        "/v3/api-docs", "/v2/api-docs", "/swagger-resources/**", "/swagger/**", "/swagger-ui/**"
        , "/swagger-ui/index.html", "/swagger-ui.html"};

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
            .antMatchers(pathArray);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//            .accessDeniedHandler(new JwtAccessDeniedHandler()) // 권한핸들링. 역할로 안해줌
            .and()
            .authorizeRequests()
            .antMatchers(OPTIONS).permitAll() // CORS
            .antMatchers(GET, "/api/articles/**", "/api/profiles/**", "/api/tags")
            .permitAll()
            .antMatchers(GET, "/api/articles/feed")
            .authenticated()
            .antMatchers(POST, "/api/users", "/api/users/login")
            .permitAll()
            .antMatchers(POST, "/api/tag/**", "/api/tags").hasAuthority("admin")
            .antMatchers(DELETE, "/api/tag/**").hasAuthority("admin")
            .antMatchers(GET, "/api/tag/**", "/api/tags").permitAll()
            .antMatchers("/api/test*", "/api/test/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            // 인증을 처리하는 기본필터인 UsernamePasswordAuthenticationFilter 대신 별도의 인증 로직을 가진 커스텀
            // 필터 등록
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//            .addFilterAfter(customServletWrappingFilter, JwtAuthenticationFilter.class);
    }
}