package com.gasfgrv.barbearia.adapter.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasfgrv.barbearia.adapter.utils.ResponseUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper mapper;

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

        DadosResposta resposta = new DadosResposta(
                request.getURI().getPath(),
                request.getMethod().name(),
                HttpStatus.valueOf(((ServletServerHttpResponse) response).getServletResponse().getStatus()).name(),
                response.getHeaders().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                        stringListEntry -> String.join(", ", stringListEntry.getValue()))),
                body
        );

        log.info(ResponseUtils.montarJson(mapper, resposta));
        return body;
    }

    @Builder
    record DadosResposta(String uri, String metodo, String status, Map<String, String> headers, Object corpo) {

    }
}
