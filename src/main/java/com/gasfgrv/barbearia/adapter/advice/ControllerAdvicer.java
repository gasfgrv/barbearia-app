package com.gasfgrv.barbearia.adapter.advice;

import com.gasfgrv.barbearia.adapter.secret.ChaveSecretNaoEncontradaExeption;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
class ControllerAdvicer extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ApiResponse(responseCode = "401", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, java.util.List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));
        Problem problem = Problem.create()
                .withTitle("Erro ao autenticar usuário")
                .withDetail("Confira se as suas credenciais estão corretas")
                .withStatus(status)
                .withInstance(URI.create(obterUri(request)));
        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ChaveSecretNaoEncontradaExeption.class)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleChaveSecretNaoEncontradaExeption(ChaveSecretNaoEncontradaExeption exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, java.util.List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));
        Problem problem = Problem.create()
                .withTitle("Erro ao consultar chave")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(URI.create(obterUri(request)));
        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    private String obterUri(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

}
