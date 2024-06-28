package com.gasfgrv.barbearia.config.async;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AsyncConfig.class)
class AsyncConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar um bean de ThreadPoolTaskExecutor")
    void deveCriarUmBeanDeThreadPoolTaskExecutor() {
        Executor bean = applicationContext.getBean(Executor.class);
        ThreadPoolTaskExecutor threadPoolExecutor = (ThreadPoolTaskExecutor) bean;

        assertTrue(Arrays.asList(applicationContext.getBeanDefinitionNames()).contains("emailExecutor"));
        assertInstanceOf(ThreadPoolTaskExecutor.class, bean);
        assertNotNull(bean);
        assertEquals(1, threadPoolExecutor.getCorePoolSize());
        assertEquals(10, threadPoolExecutor.getMaxPoolSize());
        assertEquals(100, threadPoolExecutor.getQueueCapacity());
        assertEquals("emailExecutor-", threadPoolExecutor.getThreadNamePrefix());
    }

}