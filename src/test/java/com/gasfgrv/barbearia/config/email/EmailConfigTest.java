package com.gasfgrv.barbearia.config.email;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class EmailConfigTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar um bean do JavaMailSender")
    void deveCriarUmBeanDoJavaMailSender() {
        JavaMailSender bean = applicationContext.getBean(JavaMailSender.class);
        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) bean;
        Properties properties = javaMailSender.getJavaMailProperties();

        assertInstanceOf(JavaMailSenderImpl.class, bean);
        assertNotNull(bean);
        assertEquals("localhost", javaMailSender.getHost());
        assertEquals(3025, javaMailSender.getPort());
        assertEquals("testUser", javaMailSender.getUsername());
        assertEquals("1234567890", javaMailSender.getPassword());

        assertEquals("smtp", properties.getProperty("mail.transport.protocol"));
        assertEquals(Boolean.TRUE.toString(), properties.getProperty("mail.smtp.auth"));
        assertEquals(Boolean.TRUE.toString(), properties.getProperty("mail.smtp.starttls.enable"));
    }

}
