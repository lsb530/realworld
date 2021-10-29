package com.boki.realworld.config;

import com.boki.realworld.log.P6spyPrettySqlFormatter;
import com.p6spy.engine.spy.P6SpyOptions;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6spyConfig {

    @PostConstruct
    public void setLogMessageFormat() {
        System.out.println(P6spyPrettySqlFormatter.class.getName());
        P6SpyOptions.getActiveInstance()
            .setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
    }
}