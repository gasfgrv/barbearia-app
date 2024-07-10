package com.gasfgrv.barbearia.adapter.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class RequestUtils {

    public static void logRequisicaoRecebida(HttpServletRequest request) {
        log.info("Requisição recebida para: [{}] - {}", request.getMethod(), request.getServletPath());
    }

    public static void logRequisicaoRecebida(HttpServletRequest request, Map<String, String> queryParameters) {
        StringBuilder params = new StringBuilder().append("?");

        queryParameters.forEach((chave, valor) ->
                params.append(chave)
                        .append("=")
                        .append(valor)
                        .append("&"));

        log.info("Requisição recebida para: [{}] - {}{}",
                request.getMethod(),
                request.getServletPath(),
                StringUtils.chop(params.toString()));
    }

    public static Map<String, String> capturarQueryParameters(HttpServletRequest request) {
        return request.getParameterMap()
                .entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        param -> param.getValue()[0]
                ));
    }

    public static String capturarHeader(String header, HttpServletRequest request) {
        return request.getHeader(header);
    }

}
