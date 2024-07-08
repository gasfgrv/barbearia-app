package com.gasfgrv.barbearia.config.cache;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class RedisConfigTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar um bean de configuração do Redis")
    void deveCriarUmBeanDeConfiguracaoDoRedis() {
        RedisCacheConfiguration bean = applicationContext.getBean(RedisCacheConfiguration.class);
        assertNotNull(bean);
    }

}