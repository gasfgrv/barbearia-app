package com.gasfgrv.barbearia.utils;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.ListSecretsResponse;

import java.util.Map;
import java.util.Set;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

@UtilityClass
public class LocalStackContainerUtils {

    public void criarSecrets(LocalStackContainer localStackContainer) {
        try (SecretsManagerClient secretsManagerClient = getSecretsManagerClient(localStackContainer)) {
            ListSecretsResponse listSecretsResponse = secretsManagerClient.listSecrets();
            if (listSecretsResponse.secretList().isEmpty()) {
                Set.of(montarCreateRequest("jwtSecretTest", "15678"),
                        montarCreateRequest("databaseSecretTest",
                                JsonUtils.montarJson(Map.ofEntries(
                                        Map.entry("password", "postgres"),
                                        Map.entry("username", "postgres")
                                ))),
                        montarCreateRequest("emailSecretTest",
                                JsonUtils.montarJson(Map.ofEntries(
                                        Map.entry("host", "localhost"),
                                        Map.entry("port", "3025"),
                                        Map.entry("username", "testUser"),
                                        Map.entry("password", "1234567890"),
                                        Map.entry("auth", "true"),
                                        Map.entry("starttls", "true")
                                )))
                ).forEach(secretsManagerClient::createSecret);
            }
        }
    }

    public void destruirSecrets(LocalStackContainer localStackContainer) {
        try (SecretsManagerClient secretsManagerClient = getSecretsManagerClient(localStackContainer)) {
            ListSecretsResponse listSecretsResponse = secretsManagerClient.listSecrets();
            if (!listSecretsResponse.secretList().isEmpty())
                Set.of(montarDeleteRequest("emailSecretTest"),
                        montarDeleteRequest("databaseSecretTest"),
                        montarDeleteRequest("jwtSecretTest")
                ).forEach(secretsManagerClient::deleteSecret);
        }
    }

    private DeleteSecretRequest montarDeleteRequest(String secretName) {
        return DeleteSecretRequest.builder()
                .secretId(secretName)
                .forceDeleteWithoutRecovery(true)
                .build();
    }

    private CreateSecretRequest montarCreateRequest(String secretName, String secretValue) {
        return CreateSecretRequest.builder()
                .name(secretName)
                .secretString(secretValue)
                .build();
    }

    private SecretsManagerClient getSecretsManagerClient(LocalStackContainer localStackContainer) {
        return SecretsManagerClient.builder()
                .region(Region.of(localStackContainer.getRegion()))
                .endpointOverride(localStackContainer.getEndpointOverride(SECRETSMANAGER))
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials(localStackContainer)))
                .build();
    }

    private AwsBasicCredentials getCredentials(LocalStackContainer localStackContainer) {
        return AwsBasicCredentials.builder()
                .accessKeyId(localStackContainer.getAccessKey())
                .secretAccessKey(localStackContainer.getSecretKey())
                .build();
    }

}
