package com.gasfgrv.barbearia.adapter.database.reset;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.UsuarioMock;
import com.gasfgrv.barbearia.domain.port.database.reset.PasswordResetTokenRepositoryPort;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PasswordResetTokenRepositoryAdapterTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    PasswordResetTokenRepositoryPort repository;

    @Autowired
    UsuarioRepositoryPort usuarioRepository;

    PasswordResetTokenSchema tokenSchema;

    @BeforeEach
    void setUp() {
        tokenSchema = PasswordResetTokenSchemaMock.montarPasswordResetTokenSchema();
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar token")
    void deveSalvarToken() {
        usuarioRepository.salvarUsuario(UsuarioMock.montarUsuario());

        repository.salvarResetToken(tokenSchema.getUsuarioLogin().getLogin(),
                tokenSchema.getExpiryDate(),
                tokenSchema.getToken());

        assertTrue(repository.existeTokenParaAtualizarSenha(tokenSchema.getToken()));
    }

    @Test
    @Order(2)
    @DisplayName("Deve obter a data de expiração de um token existente")
    void deveObterADataDeExpiracaoDeUmTokenExistente() {
        LocalDateTime dataExpiracao = repository.obterExpiracaoToken(tokenSchema.getToken());
        LocalDateTime dataGeracao = LocalDateTime.of(2024, 1, 1, 10, 10);
        assertEquals(dataGeracao.plusHours(2), dataExpiracao);
    }

    @Test
    @Order(3)
    @DisplayName("Deve obter a data de expiração de um token inexistente")
    void deveObterADataDeExpiracaoDeUmTokenInexistente() {
        LocalDateTime dataExpiracao = repository.obterExpiracaoToken(String.valueOf(tokenSchema.getId()));
        assertEquals(LocalDateTime.MIN, dataExpiracao);
    }

    @Test
    @Order(4)
    @DisplayName("Deve verificar se existe token para atualizar senha")
    void deveVerificarSeExisteTokenParaAtualizarSenha() {
        assertTrue(repository.existeTokenParaAtualizarSenha(tokenSchema.getToken()));
    }

    @Test
    @Order(5)
    @DisplayName("Deve verificar se não existe token para atualizar senha")
    void deveVerificarSeNaoExisteTokenParaAtualizarSenha() {
        assertFalse(repository.existeTokenParaAtualizarSenha(tokenSchema.getToken().replaceAll("\\.", "")));
    }

}