package com.gasfgrv.barbearia.adapter.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasfgrv.barbearia.adapter.exception.email.EnvioEmailException;
import com.gasfgrv.barbearia.adapter.exception.secret.ChaveSecretNaoEncontradaExeption;
import com.gasfgrv.barbearia.adapter.exception.secret.ErroAoProcessarValorSecretExeption;
import com.gasfgrv.barbearia.adapter.exception.token.ResetTokenInvalidoException;
import com.gasfgrv.barbearia.application.exception.servico.SemDadosParaAlterarException;
import com.gasfgrv.barbearia.application.exception.servico.ServicoAtivoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoDesativadoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoExistenteExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoNaoEncontradoException;
import com.gasfgrv.barbearia.application.exception.usuario.UsuarioNaoEncontradoException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler implements AccessDeniedHandler {

    @ExceptionHandler(SemDadosParaAlterarException.class)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleSemDadosParaAlterarException(SemDadosParaAlterarException exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao alterar o serviço")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ServicoAtivoExeception.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoAtivoExeception(ServicoAtivoExeception exception, WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao ativar o serviço")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ServicoDesativadoExeception.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoDesativadoExeception(ServicoDesativadoExeception exception, WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao desativar o serviço")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ServicoExistenteExeception.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoExistenteExeception(ServicoExistenteExeception exception, WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao salvar o serviço")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ServicoNaoEncontradoException.class)
    @ApiResponse(responseCode = "404", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoNaoEncontradoException(ServicoNaoEncontradoException exception, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao consultar o serviço")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ApiResponse(responseCode = "401", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao autenticar usuário")
                .withDetail("Confira se as suas credenciais estão corretas")
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ChaveSecretNaoEncontradaExeption.class)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleChaveSecretNaoEncontradaExeption(ChaveSecretNaoEncontradaExeption exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao consultar chave")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ErroAoProcessarValorSecretExeption.class)
    @ApiResponse(responseCode = "500", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleErroAoProcessarValorSecretExeption(ErroAoProcessarValorSecretExeption exception, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro inesperado ao consultar valor do segredo")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ApiResponse(responseCode = "404", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException exception, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao consultar chave")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(ResetTokenInvalidoException.class)
    @ApiResponse(responseCode = "403", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleResetTokenInvalidoException(ResetTokenInvalidoException exception, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao alterar a senha")
                .withDetail(exception.getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(EnvioEmailException.class)
    @ApiResponse(responseCode = "503", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleEnvioEmailException(EnvioEmailException exception, WebRequest request) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle("Erro ao enviar e-mail")
                .withDetail(exception.getCause().getMessage())
                .withStatus(status)
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @Override
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addAll(headers);
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_PROBLEM_JSON_VALUE));

        List<String> erros = exception.getBindingResult().getAllErrors()
                .stream().map(erro -> {
                    String campo = ((FieldError) erro).getField();
                    String mensagem = erro.getDefaultMessage();
                    return String.format("'%s' %s", campo, mensagem);
                })
                .toList();

        Problem problem = Problem.create()
                .withTitle("Erro no valor dos parâmetros")
                .withDetail(erros.toString().replaceAll("[\\[\\]]", ""))
                .withStatus(HttpStatus.valueOf(status.value()))
                .withInstance(obterUri(request));

        return handleExceptionInternal(exception, problem, httpHeaders, status, request);
    }

    @Override
    @ApiResponse(responseCode = "403", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        int status = HttpStatus.FORBIDDEN.value();

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("title", "Acesso proibido");
        responseBody.put("detail", "Você não tem a permissão necessária para acessar esse recurso");
        responseBody.put("status", status);
        responseBody.put("instance", obterUri(new ServletWebRequest(request)));

        String json = new ObjectMapper().writeValueAsString(responseBody);

        try (PrintWriter responseWriter = response.getWriter()) {
            responseWriter.write(json);
            responseWriter.flush();
        }
    }

    private URI obterUri(WebRequest request) {
        return URI.create(((ServletWebRequest) request).getRequest().getRequestURI());
    }

}
