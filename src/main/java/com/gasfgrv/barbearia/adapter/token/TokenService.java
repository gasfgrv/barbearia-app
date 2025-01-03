package com.gasfgrv.barbearia.adapter.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasfgrv.barbearia.adapter.exception.token.ResetTokenInvalidoException;
import com.gasfgrv.barbearia.application.exception.token.TokenException;
import com.gasfgrv.barbearia.domain.port.database.reset.PasswordResetTokenRepositoryPort;
import com.gasfgrv.barbearia.domain.port.secret.SecretPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.gasfgrv.barbearia.adapter.exception.token.ResetTokenInvalidoException.TipoErro.EXPIRADO;
import static com.gasfgrv.barbearia.adapter.exception.token.ResetTokenInvalidoException.TipoErro.INEXISTENTE;
import static com.gasfgrv.barbearia.adapter.utils.ResponseUtils.montarJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private final Clock clock;
    private final SecretPort secretPort;
    private final PasswordResetTokenRepositoryPort passwordResetTokenRepository;
    private final ObjectMapper mapper;

    public String gerarToken(DadosToken dadosToken) {
        log.info("Gerando token com os seguintes dados: {}", montarJson(mapper, dadosToken));
        return JWT.create()
                .withIssuer("barbearia")
                .withSubject(dadosToken.subject())
                .withExpiresAt(getDataExpiracao(dadosToken.dataGeracao()))
                .sign(obterAlgoritmo());
    }

    public String getSubject(String token) {
        try {
            log.info("Obtendo subject do token: {}", token);
            return JWT.require(obterAlgoritmo())
                    .withIssuer("barbearia")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new TokenException(e);
        }
    }

    public String criarResetToken(DadosToken dadosToken) {
        log.info("Salvando token de reset de senha com os seguintes dados: {}", montarJson(mapper, dadosToken));
        String token = gerarToken(dadosToken);
        passwordResetTokenRepository.salvarResetToken(dadosToken.subject(), dadosToken.dataGeracao(), token);
        return token;
    }

    public void validarResetToken(String token) {
        log.info("Validando dados do token: {}", token);
        boolean tokenNaoExiste = !passwordResetTokenRepository.existeTokenParaAtualizarSenha(token);
        if (tokenNaoExiste) {
            log.error("Token inexistente");
            throw new ResetTokenInvalidoException(INEXISTENTE);
        }

        boolean isExpirado = passwordResetTokenRepository.obterExpiracaoToken(token).isBefore(LocalDateTime.now(clock));
        if (isExpirado) {
            log.error("Token inválido ou inexistente");
            throw new ResetTokenInvalidoException(EXPIRADO);
        }
    }

    private Algorithm obterAlgoritmo() {
        log.info("Obtendo algoritmo para assinar token");
        return Algorithm.HMAC256(secretPort.obterSecret(secret));
    }

    private Instant getDataExpiracao(LocalDateTime dataGeracao) {
        log.info("Calculando expiração do token");
        return dataGeracao.plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
