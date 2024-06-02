package com.gasfgrv.barbearia.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "barbearia",
        description = "Api para agendamento de horários",
        version = "V1",
        contact = @Contact(
                name = "gasfgrv",
                email = "gustavo_almeida11@hotmail.com",
                url = "https://github.com/gasfgrv")
))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi logApi() {
        return GroupedOpenApi.builder()
                .group("api-controllers")
                .packagesToScan("com.gasfgrv.barbearia.adapter.controller")
                .pathsToMatch("/**")
                .build();
    }

}
