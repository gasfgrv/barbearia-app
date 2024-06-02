package com.gasfgrv.barbearia.adapter.secret;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasfgrv.barbearia.port.secret.SecretPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

@Component
@RequiredArgsConstructor
public class SecretAdapter implements SecretPort {

    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper mapper;

    @Override
    public String obterSecret(String nomeSecret) {
        GetSecretValueRequest getSecretValueRequest = montarGetSecretValueRequest(nomeSecret);
        return secretsManagerClient.getSecretValue(getSecretValueRequest).secretString();
    }

    @Override
    public String obterSecret(String nome, String chave) {
        try {
            GetSecretValueRequest getSecretValueRequest = montarGetSecretValueRequest(nome);
            String secretString = secretsManagerClient.getSecretValue(getSecretValueRequest).secretString();
            JsonNode jsonNode = mapper.readTree(secretString);
            return jsonNode.path(chave).asText();
        } catch (JsonProcessingException e) {
            throw new ChaveSecretNaoEncontradaExeption(chave, nome);
        }
    }

    private GetSecretValueRequest montarGetSecretValueRequest(String nomeSecret) {
        return GetSecretValueRequest.builder()
                .secretId(nomeSecret)
                .build();
    }
}
