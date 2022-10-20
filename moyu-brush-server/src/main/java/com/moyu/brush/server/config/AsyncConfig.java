package com.moyu.brush.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AsyncConfig {

    @Bean
    ExecutorService executorService() {
        return new ThreadPoolExecutor(
                8, 16, 10, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

}
