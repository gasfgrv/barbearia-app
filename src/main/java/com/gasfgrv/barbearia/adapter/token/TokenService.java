package com.gasfgrv.barbearia.adapter.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import com.gasfgrv.barbearia.adapter.exception.token.ResetTokenInvalidoException;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.database.reset.PasswordResetTokenRepositoryPort;
import com.gasfgrv.barbearia.domain.port.secret.SecretPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private final SecretPort secretPort;
    private final PasswordResetTokenRepositoryPort passwordResetTokenRepository;

    public String gerarToken(UsuarioSchema principal) {
        Algorithm algoritmo = Algorithm.HMAC256(obterValorDoSecret());
        return JWT.create()
                .withIssuer("barbearia")
                .withSubject(principal.getUsername())
                .withExpiresAt(dataExpiracao())
                .sign(algoritmo);
    }

    public String getSubject(String token) {
        Algorithm algoritmo = Algorithm.HMAC256(obterValorDoSecret());
        return JWT.require(algoritmo)
                .withIssuer("barbearia")
                .build()
                .verify(token)
                .getSubject();
    }

    public void criarResetToken(Usuario usuario, String token) {
        passwordResetTokenRepository.salvarResetToken(usuario, token);
    }

    public void validarResetToken(String token) {
        boolean tokenNaoExiste = !passwordResetTokenRepository.existeTokenParaAtualizarSenha(token);
        boolean isExpirado = !passwordResetTokenRepository.obterExpiracaoToken(token).isBefore(LocalDateTime.now());

        if (tokenNaoExiste && isExpirado) {
            throw new ResetTokenInvalidoException();
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    private String obterValorDoSecret() {
        return secretPort.obterSecret(secret);
    }

}
