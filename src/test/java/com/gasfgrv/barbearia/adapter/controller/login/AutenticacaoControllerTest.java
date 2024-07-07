package com.gasfgrv.barbearia.adapter.controller.login;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.entity.UsuarioMock;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import io.restassured.RestAssured;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

class AutenticacaoControllerTest extends AbstractContainerIntegrationTestConfig {

    Usuario usuario;

    @Autowired
    UsuarioRepositoryPort usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/v1/login";
        usuario = UsuarioMock.montarUsuario();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.salvarUsuario(usuario);
    }

    @Test
    @DisplayName("Deve retornar o token quando fizer o login")
    void deveFazerRetornarTokenQuandoFizerLogin() {
        Map<String, String> body = Map.ofEntries(
                Map.entry("login", usuario.getLogin()),
                Map.entry("senha", "123456")
        );

        RestAssured
                .given()
                    .log().everything()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                .when()
                    .log().everything()
                    .post()
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("token", Matchers.matchesPattern("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$"));
    }

    @Test
    @DisplayName("Deve retornar um token quando for para o reset da senha")
    void deveRetornarUmTokenQuandoForChamadoUmReset() {
        Map<String, String> body = Map.ofEntries(
                Map.entry("login", usuario.getLogin())
        );

        RestAssured
                .given()
                    .log().everything()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                .when()
                    .log().everything()
                    .post("/reset")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("token", Matchers.matchesPattern("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$"));
    }

    @Test
    @DisplayName("Deve alterar a senha do usuário")
    void deveAlterarSenhaDoUsuario() {
        Map<String, String> body = Map.ofEntries(
                Map.entry("senha", "novaSenha")
        );

        Map<String, String> tokenBody = Map.ofEntries(
                Map.entry("login", usuario.getLogin())
        );

        String token = getToken(tokenBody);

        RestAssured
                .given()
                    .log().everything()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token))
                    .body(body)
                .when()
                    .log().everything()
                    .put("/reset/nova")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private static String getToken(Map<String, String> tokenBody) {
        return RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(tokenBody)
                .when()
                    .post("/reset")
                    .then()
                .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .response()
                    .jsonPath().getString("token");
    }

}