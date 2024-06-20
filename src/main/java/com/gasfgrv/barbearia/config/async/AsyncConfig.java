package com.gasfgrv.barbearia.config.async;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "emailExecutor")
    public Executor threadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(1)
                .maxPoolSize(10)
                .queueCapacity(100)
                .threadNamePrefix("emailExecutor-")
                .build();
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }

}
