package com.gasfgrv.barbearia.adapter.email;

import com.gasfgrv.barbearia.adapter.exception.email.EnvioEmailException;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.email.EmailPort;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailAdapter implements EmailPort {

    private final JavaMailSender mailSender;

    @Override
    @Async("emailExecutor")
    public void enviarResetToken(String login, String url) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress("contato@barbearia.com"));
            mimeMessage.setRecipients(Message.RecipientType.TO, login);
            mimeMessage.setSubject("Nova Senha");
            String path = url + "/nova";
            String htmlContent = String.format("<a href=\"%s\">Link para gerar a nova senha</a>", path);
            mimeMessage.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EnvioEmailException(e);
        }
    }

}
