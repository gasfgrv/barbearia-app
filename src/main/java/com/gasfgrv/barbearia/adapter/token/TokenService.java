package com.gasfgrv.barbearia.adapter.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(UsuarioSchema principal) {
        var algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("Login")
                .withSubject(principal.getUsername())
                .withExpiresAt(dataExpiracao())
                .sign(algoritmo);
    }

    public String getSubject(String token) {
        var algoritmo = Algorithm.HMAC256(secret);
        return JWT.require(algoritmo)
                .withIssuer("Login")
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
