package com.gasfgrv.barbearia.adapter.secret;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasfgrv.barbearia.adapter.exception.secret.ChaveSecretNaoEncontradaExeption;
import com.gasfgrv.barbearia.adapter.exception.secret.ErroAoProcessarValorSecretExeption;
import com.gasfgrv.barbearia.domain.port.secret.SecretPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecretAdapter implements SecretPort {

    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper mapper;

    @Override
    public String obterSecret(String nomeSecret) {
        GetSecretValueRequest getSecretValueRequest = montarGetSecretValueRequest(nomeSecret);
        log.info("Obtendo o valor do secret '{}'", getSecretValueRequest.secretId());
        return secretsManagerClient.getSecretValue(getSecretValueRequest).secretString();
    }

    @Override
    public String obterSecret(String nome, String chave) {
        try {
            GetSecretValueRequest getSecretValueRequest = montarGetSecretValueRequest(nome);
            log.info("Obtendo o valor da chave '{}' no secret '{}'", chave, getSecretValueRequest.secretId());
            String secretString = secretsManagerClient.getSecretValue(getSecretValueRequest).secretString();
            return obterValorDaChave(nome, chave, secretString);
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar o valor do secret", e);
            throw new ErroAoProcessarValorSecretExeption();
        }
    }

    private String obterValorDaChave(String nome, String chave, String secretString) throws JsonProcessingException {
        JsonNode valorDaChave = mapper.readTree(secretString).path(chave);

        if (!valorDaChave.asText().isEmpty()) {
            return valorDaChave.asText();
        }

        log.error("A chave '{}' não existe no secret '{}'", chave, nome);
        throw new ChaveSecretNaoEncontradaExeption(chave, nome);
    }

    private GetSecretValueRequest montarGetSecretValueRequest(String nomeSecret) {
        return GetSecretValueRequest.builder()
                .secretId(nomeSecret)
                .build();
    }

}
