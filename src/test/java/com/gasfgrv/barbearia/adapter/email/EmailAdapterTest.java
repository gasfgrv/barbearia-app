package com.gasfgrv.barbearia.adapter.email;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.UsuarioMock;
import com.gasfgrv.barbearia.domain.port.email.EmailPort;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class EmailAdapterTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    EmailPort emailAdapter;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("testUser", "1234567890"))
            .withPerMethodLifecycle(false);

    @Test
    @DisplayName("Deve enviar email com link para reset de senha")
    void deveEnviarEmailComLinkParaResetDeSenha() {
        String login = UsuarioMock.montarUsuario().getLogin();
        String url = "localhost";

        emailAdapter.enviarResetToken(login, url);

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> {
            MimeMessage[] mensagens = greenMail.getReceivedMessages();
            assertEquals(1, mensagens.length);

            MimeMessage mensagem = mensagens[0];
            assertEquals("contato@barbearia.com", mensagem.getFrom()[0].toString());
            assertEquals(login, mensagem.getRecipients(Message.RecipientType.TO)[0].toString());
            assertEquals("Nova Senha", mensagem.getSubject());
        });
    }

}
