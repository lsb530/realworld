package com.boki.realworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealworldApplication.class, args);
    }

    // @Bean(initMethod = "X", destroyMethod = "Y")
    // implements InitializingBean, DisposableBean에서
    // @PostConstruct -> @Override afterPropertiesSet() -> X 순으로 생성되고
    // @PreDestroy -> @Override destroy() -> Y 순으로 없어진다
}