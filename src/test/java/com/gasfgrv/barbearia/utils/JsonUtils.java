package com.gasfgrv.barbearia.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class JsonUtils {

    public String montarJson(Map<String, String> secretMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(secretMap);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
