package com.gasfgrv.barbearia.adapter.token;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchemaMock;
import com.gasfgrv.barbearia.adapter.exception.token.ResetTokenInvalidoException;
import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.entity.UsuarioMock;
import com.gasfgrv.barbearia.domain.port.database.reset.PasswordResetTokenRepositoryPort;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TokenServiceTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepositoryPort usuarioRepositoryPort;

    @Autowired
    PasswordResetTokenRepositoryPort passwordResetTokenRepository;

    @MockBean
    Clock clock;

    @Test
    @DisplayName("Deve gerar um token JWT")
    void deveGerarUmTokenJwt() {
        String usuario = UsuarioSchemaMock.montarUsuarioSchema().getLogin();
        String token = tokenService.gerarToken(new DadosToken(usuario, LocalDateTime.now()));
        assertNotNull(token);
    }

    @Test
    @DisplayName("Deve obter o subject do token")
    void deveOterOSubjectDoToken() {
        String usuario = UsuarioSchemaMock.montarUsuarioSchema().getLogin();
        String token = tokenService.gerarToken(new DadosToken(usuario, LocalDateTime.now()));

        String subject = tokenService.getSubject(token);

        assertEquals(usuario, subject);
    }

    @Test
    @DisplayName("Deve salvar token de reset na base de dados")
    void deveSalvaTokenDeResetNaBaseDeDados() {
        Usuario usuario = UsuarioMock.montarUsuario();
        usuarioRepositoryPort.salvarUsuario(usuario);

        String schema = UsuarioSchemaMock.montarUsuarioSchema().getLogin();
        String token = tokenService.gerarToken(new DadosToken(schema, LocalDateTime.now()));

        tokenService.criarResetToken(new DadosToken(usuario.getLogin(), LocalDateTime.now()));
        assertTrue(passwordResetTokenRepository.existeTokenParaAtualizarSenha(token));
    }

    @Test
    @DisplayName("Deve lançar ResetTokenInvalidoException ao validar reset token inexistente")
    void DeveLancarResetTokenInvalidoExceptionAoValidarResetTokenInexistente() {
        ZonedDateTime now = ZonedDateTime.of(
                LocalDateTime.of(2020, 8, 8, 12, 45),
                ZoneOffset.of("-03:00"));

        when(clock.getZone()).thenReturn(now.getZone());
        when(clock.instant()).thenReturn(now.toInstant());

        LocalDateTime dataGeracao = LocalDateTime.of(2020, 7, 7, 1, 10);
        String schema = UsuarioSchemaMock.montarUsuarioSchema().getLogin();
        String token = tokenService.gerarToken(new DadosToken(schema, dataGeracao));

        assertThrows(ResetTokenInvalidoException.class,
                () -> tokenService.validarResetToken(token));
    }

    @Test
    @DisplayName("Deve lançar ResetTokenInvalidoException ao validar reset token expirado")
    void DeveLancarResetTokenInvalidoExceptionAoValidarResetTokenExpirado() {
        ZonedDateTime now = ZonedDateTime.of(
                LocalDateTime.of(2020, 8, 8, 12, 45),
                ZoneOffset.of("-03:00"));

        when(clock.getZone()).thenReturn(now.getZone());
        when(clock.instant()).thenReturn(now.toInstant());

        Usuario usuario = UsuarioMock.montarUsuario();
        usuarioRepositoryPort.salvarUsuario(usuario);

        LocalDateTime dataGeracao = LocalDateTime.of(2020, 7, 7, 1, 10);
        String schema = UsuarioSchemaMock.montarUsuarioSchema().getLogin();
        String token = tokenService.gerarToken(new DadosToken(schema, dataGeracao));

        String login = UsuarioMock.montarUsuario().getLogin();
        passwordResetTokenRepository.salvarResetToken(login, dataGeracao, token);

        assertThrows(ResetTokenInvalidoException.class,
                () -> tokenService.validarResetToken(token));
    }

    @Test
    @DisplayName("Deve validar reset token")
    void DeveValidarResetToken() {
        ZonedDateTime now = ZonedDateTime.of(
                LocalDateTime.of(2020, 8, 8, 12, 45),
                ZoneOffset.of("-03:00"));

        when(clock.getZone()).thenReturn(now.getZone());
        when(clock.instant()).thenReturn(now.toInstant());

        Usuario usuario = UsuarioMock.montarUsuario();
        usuarioRepositoryPort.salvarUsuario(usuario);

        String schema = UsuarioSchemaMock.montarUsuarioSchema().getLogin();
        String token = tokenService.gerarToken(new DadosToken(schema, now.toLocalDateTime()));

        String login = UsuarioMock.montarUsuario().getLogin();
        passwordResetTokenRepository.salvarResetToken(login, now.toLocalDateTime(), token);

        assertDoesNotThrow(() -> tokenService.validarResetToken(token));
    }

}
