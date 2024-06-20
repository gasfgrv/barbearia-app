package com.gasfgrv.barbearia.config.email;

import com.gasfgrv.barbearia.domain.port.secret.SecretPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    @Value("${email.secret}")
    private String emailSecret;

    private final SecretPort secrets;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(secrets.obterSecret(emailSecret, "host"));
        mailSender.setPort(Integer.parseInt(secrets.obterSecret(emailSecret, "port")));
        mailSender.setUsername(secrets.obterSecret(emailSecret, "username"));
        mailSender.setPassword(secrets.obterSecret(emailSecret, "password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", secrets.obterSecret(emailSecret, "auth"));
        props.put("mail.smtp.starttls.enable", secrets.obterSecret(emailSecret, "starttls"));

        return mailSender;
    }

}
