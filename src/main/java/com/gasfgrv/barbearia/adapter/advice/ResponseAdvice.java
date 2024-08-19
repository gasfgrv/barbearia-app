package com.gasfgrv.barbearia.adapter.advice;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        boolean contemRotaQueNaoEhDeNegocio = Set.of("/swagger", "/actuator", "/api-docs")
                .stream().anyMatch(path -> request.getURI().getPath().contains(path));

        if (!contemRotaQueNaoEhDeNegocio) {
            DadosResposta resposta = DadosResposta.builder()
                    .uri(request.getURI().getPath())
                    .metodo(request.getMethod().name())
                    .status(HttpStatus.valueOf(((ServletServerHttpResponse) response).getServletResponse().getStatus()).value())
                    .headers(obterHeadersDaResponse(response))
                    .corpo(body)
                    .build();

            log.info("Resposta da requisição", StructuredArguments.keyValue("resposta", resposta));
        }

        return body;
    }

    private Map<String, String> obterHeadersDaResponse(ServerHttpResponse response) {
        return response.getHeaders().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                stringListEntry -> String.join(", ", stringListEntry.getValue())));
    }

    @Builder
    record DadosResposta(String uri, String metodo, int status, Map<String, String> headers, Object corpo) {
    }

}
