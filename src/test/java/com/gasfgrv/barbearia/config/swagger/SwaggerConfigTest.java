package com.gasfgrv.barbearia.config.swagger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SwaggerConfig.class)
class SwaggerConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar os beans para a documentação via Swagger")
    void deveCriarOsBeansParaADocumentacaoViaSwagger() {
        GroupedOpenApi groupedOpenApiBean = applicationContext.getBean(GroupedOpenApi.class);
        SwaggerConfig swaggerConfigBean = applicationContext.getBean(SwaggerConfig.class);

        assertTrue(Arrays.asList(applicationContext.getBeanDefinitionNames()).contains("swaggerConfig"));
        assertTrue(Arrays.asList(applicationContext.getBeanDefinitionNames()).contains("groupedOpenApi"));

        assertNotNull(groupedOpenApiBean);
        assertInstanceOf(GroupedOpenApi.class, groupedOpenApiBean);
        assertEquals("api-controllers", groupedOpenApiBean.getGroup());
        assertTrue(groupedOpenApiBean.getPathsToMatch().contains("/**"));
        assertTrue(groupedOpenApiBean.getPackagesToScan().contains("com.gasfgrv.barbearia.adapter.controller"));

        assertNotNull(swaggerConfigBean);
        assertInstanceOf(SwaggerConfig.class, swaggerConfigBean);
    }

}