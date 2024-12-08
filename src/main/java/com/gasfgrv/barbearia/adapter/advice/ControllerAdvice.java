package com.gasfgrv.barbearia.adapter.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasfgrv.barbearia.adapter.exception.email.EnvioEmailException;
import com.gasfgrv.barbearia.adapter.exception.secret.ChaveSecretNaoEncontradaExeption;
import com.gasfgrv.barbearia.adapter.exception.secret.ErroAoProcessarValorSecretExeption;
import com.gasfgrv.barbearia.adapter.exception.token.ResetTokenInvalidoException;
import com.gasfgrv.barbearia.application.exception.pessoa.IdadeInvalidaException;
import com.gasfgrv.barbearia.application.exception.pessoa.PessoaExistenteException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    @ExceptionHandler(IdadeInvalidaException.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleIdadeInvalidaException(IdadeInvalidaException exception, WebRequest request) {
        return handleErrorResponse(UNPROCESSABLE_ENTITY, "Erro ao cadastrar cliente", exception.getMessage(), request);
    }

    @ExceptionHandler(PessoaExistenteException.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handlePessoaExistenteException(PessoaExistenteException exception, WebRequest request) {
        return handleErrorResponse(UNPROCESSABLE_ENTITY, "Erro ao cadastrar cliente", exception.getMessage(), request);
    }

    @ExceptionHandler(SemDadosParaAlterarException.class)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleSemDadosParaAlterarException(SemDadosParaAlterarException exception, WebRequest request) {
        return handleErrorResponse(BAD_REQUEST, "Erro ao alterar o serviço", exception.getMessage(), request);
    }

    @ExceptionHandler(ServicoAtivoExeception.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoAtivoExeception(ServicoAtivoExeception exception, WebRequest request) {
        return handleErrorResponse(UNPROCESSABLE_ENTITY, "Erro ao ativar o serviço", exception.getMessage(), request);
    }

    @ExceptionHandler(ServicoDesativadoExeception.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoDesativadoExeception(ServicoDesativadoExeception exception, WebRequest request) {
        return handleErrorResponse(UNPROCESSABLE_ENTITY, "Erro ao desativar o serviço", exception.getMessage(), request);
    }

    @ExceptionHandler(ServicoExistenteExeception.class)
    @ApiResponse(responseCode = "422", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoExistenteExeception(ServicoExistenteExeception exception, WebRequest request) {
        return handleErrorResponse(UNPROCESSABLE_ENTITY, "Erro ao salvar o serviço", exception.getMessage(), request);
    }

    @ExceptionHandler(ServicoNaoEncontradoException.class)
    @ApiResponse(responseCode = "404", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleServicoNaoEncontradoException(ServicoNaoEncontradoException exception, WebRequest request) {
        return handleErrorResponse(NOT_FOUND, "Erro ao consultar o serviço", exception.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ApiResponse(responseCode = "401", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
        return handleErrorResponse(UNAUTHORIZED,
                "Erro ao autenticar usuário",
                "Confira se as suas credenciais estão corretas",
                request);
    }

    @ExceptionHandler(ChaveSecretNaoEncontradaExeption.class)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleChaveSecretNaoEncontradaExeption(ChaveSecretNaoEncontradaExeption exception, WebRequest request) {
        return handleErrorResponse(BAD_REQUEST, "Erro ao consultar chave", exception.getMessage(), request);
    }

    @ExceptionHandler(ErroAoProcessarValorSecretExeption.class)
    @ApiResponse(responseCode = "500", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleErroAoProcessarValorSecretExeption(ErroAoProcessarValorSecretExeption exception, WebRequest request) {
        return handleErrorResponse(INTERNAL_SERVER_ERROR,
                "Erro inesperado ao consultar valor do segredo",
                exception.getMessage(),
                request);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ApiResponse(responseCode = "404", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException exception, WebRequest request) {
        return handleErrorResponse(NOT_FOUND, "Erro ao consultar chave", exception.getMessage(), request);
    }

    @ExceptionHandler(ResetTokenInvalidoException.class)
    @ApiResponse(responseCode = "403", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleResetTokenInvalidoException(ResetTokenInvalidoException exception, WebRequest request) {
        return handleErrorResponse(FORBIDDEN, "Erro ao alterar a senha", exception.getMessage(), request);
    }

    @ExceptionHandler(EnvioEmailException.class)
    @ApiResponse(responseCode = "503", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public ResponseEntity<Object> handleEnvioEmailException(EnvioEmailException exception, WebRequest request) {
        return handleErrorResponse(SERVICE_UNAVAILABLE,
                "Erro ao enviar e-mail",
                exception.getCause().getMessage(),
                request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
        String listBoundariesRegex = "[\\[\\]]";
        String erros = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(getFieldsWithError())
                .toList()
                .toString()
                .replaceAll(listBoundariesRegex, "");

        return handleErrorResponse(BAD_REQUEST, "Erro no valor dos parâmetros", erros, request);
    }

    @Override
    @ApiResponse(responseCode = "403", content = {
            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Problem.class))
    })
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        ResponseEntity<Object> respostaErro = handleErrorResponse(FORBIDDEN,
                "Acesso proibido",
                "Você não tem a permissão necessária para acessar esse recurso",
                new ServletWebRequest(request));

        response.setStatus(respostaErro.getStatusCode().value());
        response.setContentType(APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String json = mapper.writeValueAsString(respostaErro.getBody());

        try (PrintWriter responseWriter = response.getWriter()) {
            responseWriter.write(json);
            responseWriter.flush();
        }
    }

    private Function<ObjectError, String> getFieldsWithError() {
        return erro -> {
            String campo = ((FieldError) erro).getField();
            String mensagem = erro.getDefaultMessage();
            return String.format("'%s' %s", campo, mensagem);
        };
    }

    private ResponseEntity<Object> handleErrorResponse(HttpStatus status, String title, String detail, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(CONTENT_TYPE, List.of(APPLICATION_PROBLEM_JSON_VALUE));

        Problem problem = Problem.create()
                .withTitle(title)
                .withDetail(detail)
                .withStatus(status)
                .withInstance(obterUri(request));

        return ResponseEntity.status(status)
                .headers(headers)
                .body(problem);
    }

    private URI obterUri(WebRequest request) {
        return URI.create(((ServletWebRequest) request).getRequest().getRequestURI());
    }

}
