package com.gasfgrv.barbearia.config.security;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class SecurityConfigurationTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar os beans para autenticação e encode da senha")
    void devCriarOsBeansParaAutenticacaoEEncodeDaSenha() {
        AuthenticationManager authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        assertNotNull(authenticationManager);

        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

}
