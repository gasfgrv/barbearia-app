package com.gasfgrv.barbearia.adapter.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class ResponseUtils {

    public static String montarJson(ObjectMapper mapper, Object body) {
        try {
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar corpo da resposta", e);
            return null;
        }
    }

}
