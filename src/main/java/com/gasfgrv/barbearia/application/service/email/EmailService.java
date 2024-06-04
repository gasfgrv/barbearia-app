package com.gasfgrv.barbearia.application.service.email;

import com.gasfgrv.barbearia.domain.entity.Usuario;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void enviarResetToken(Usuario usuario, String url) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress("contato@barbearia.com"));
            mimeMessage.setRecipients(Message.RecipientType.TO, usuario.getLogin());
            mimeMessage.setSubject("Nova Senha");
            String path = url + "/nova";
            String htmlContent = String.format("<a href=\"%s\">Link para gerar a nova senha</a>", path);
            mimeMessage.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
