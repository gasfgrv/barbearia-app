package com.gasfgrv.barbearia.adapter.email;

import com.gasfgrv.barbearia.adapter.exception.email.EnvioEmailException;
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
            log.info("Enviando email para reset da senha");
            DadosEmail dadosEmail = DadosEmail.builder()
                    .to(login)
                    .from("contato@barbearia.com")
                    .subject("Nova Senha")
                    .htmlContent(String.format("<a href=\"%s\">Link para gerar a nova senha</a>", url + "/nova"))
                    .build();

            MimeMessage mimeMessage = montarMensagem(mailSender, dadosEmail);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Erro ao enviar email {}", e.getMessage(), e);
            throw new EnvioEmailException(e);
        }
    }

    private MimeMessage montarMensagem(JavaMailSender mailSender, DadosEmail dadosEmail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setFrom(new InternetAddress(dadosEmail.from()));
        mimeMessage.setRecipients(Message.RecipientType.TO, dadosEmail.to());
        mimeMessage.setContent(dadosEmail.htmlContent(), "text/html; charset=utf-8");
        mimeMessage.setSubject(dadosEmail.subject());
        return mimeMessage;
    }

}
