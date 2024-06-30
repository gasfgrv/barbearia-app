package com.gasfgrv.barbearia.adapter.secret;

import com.gasfgrv.barbearia.adapter.exception.secret.ChaveSecretNaoEncontradaExeption;
import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.port.secret.SecretPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class SecretAdapterTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    SecretPort secret;

    @Test
    @DisplayName("Deve obter o valor do secret")
    void deveObterOValorDoSecret() {
        String nomeSecret = "jwtSecretTest";
        String secretValue = secret.obterSecret(nomeSecret);
        assertNotNull(secretValue);
        assertEquals("15678", secretValue);
    }

    @Test
    @DisplayName("Deve obter o valor do secret passando uma chave")
    void deveObterOValorDoSecretPassandoUmaChave() {
        String nomeSecret = "databaseSecretTest";
        String chaveSecret = "username";
        String secretValue = secret.obterSecret(nomeSecret, chaveSecret);
        assertNotNull(secretValue);
        assertEquals("postgres", secretValue);
    }

    @Test
    @DisplayName("Deve lançar ChaveSecretNaoEncontradaExeption caso não exista a chave")
    void deveLancarChaveSecretNaoEncontradaExeptionCasoNaoExistaAChave() {
        String nomeSecret = "databaseSecretTest";
        String chaveSecret = "port";
        assertThrows(ChaveSecretNaoEncontradaExeption.class,
                () -> secret.obterSecret(nomeSecret, chaveSecret));
    }

}
