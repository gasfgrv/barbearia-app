package com.gasfgrv.barbearia.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.ListSecretsResponse;

import java.util.Map;
import java.util.Set;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractContainerIntegrationTestConfig {

    static LocalStackContainer localStackContainer = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:latest"))
            .withServices(SECRETSMANAGER, S3);

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14-alpine"))
            .withDatabaseName("barbearia")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        localStackContainer.start();
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    private static void containerConfig(DynamicPropertyRegistry registry) {
        registry.add("api.security.token.secret", () -> "jwtSecretTest");

        registry.add("aws.region", localStackContainer::getRegion);
        registry.add("aws.endpoint.secretsManager", () -> localStackContainer.getEndpointOverride(SECRETSMANAGER));
        registry.add("aws.endpoint.s3", () -> localStackContainer.getEndpointOverride(S3));

        registry.add("email.secret", () -> "emailSecretTest");

        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        criarSecrets();
    }

    @AfterAll
    static void afterAll() {
        destruirSecrets();
    }

    private static void destruirSecrets() {
        try (SecretsManagerClient secretsManagerClient = getSecretsManagerClient()) {
            ListSecretsResponse listSecretsResponse = secretsManagerClient.listSecrets();
            if (!listSecretsResponse.secretList().isEmpty())
                deleteSecretRequests().forEach(secretsManagerClient::deleteSecret);
        }
    }

    private static void criarSecrets() {
        try (SecretsManagerClient secretsManagerClient = getSecretsManagerClient()) {
            ListSecretsResponse listSecretsResponse = secretsManagerClient.listSecrets();
            if (listSecretsResponse.secretList().isEmpty())
                createSecretsRequests().forEach(secretsManagerClient::createSecret);
        }
    }

    private static Set<DeleteSecretRequest> deleteSecretRequests() {
        DeleteSecretRequest deleteJwtSecretTest = montarDeleteRequest("jwtSecretTest");
        DeleteSecretRequest deleteDatabaseSecretTest = montarDeleteRequest("databaseSecretTest");
        DeleteSecretRequest deleteEmailSecretTest = montarDeleteRequest("emailSecretTest");

        return Set.of(deleteEmailSecretTest, deleteDatabaseSecretTest, deleteJwtSecretTest);
    }

    private static DeleteSecretRequest montarDeleteRequest(String secretName) {
        return DeleteSecretRequest.builder()
                .secretId(secretName)
                .forceDeleteWithoutRecovery(true)
                .build();
    }

    private static Set<CreateSecretRequest> createSecretsRequests() {
        CreateSecretRequest createJwtSecretRequest = montarCreateRequest("jwtSecretTest", "15678");

        CreateSecretRequest createDbSecretRequest = montarCreateRequest("databaseSecretTest",
                montarJson(Map.ofEntries(
                        Map.entry("password", "postgres"),
                        Map.entry("username", "postgres")
                )));

        CreateSecretRequest createEmailSecretRequest = montarCreateRequest("emailSecretTest",
                montarJson(Map.ofEntries(
                        Map.entry("host", "localhost"),
                        Map.entry("port", "3025"),
                        Map.entry("username", "testUser"),
                        Map.entry("password", "1234567890"),
                        Map.entry("auth", "true"),
                        Map.entry("starttls", "true")
                )));

        return Set.of(createJwtSecretRequest, createDbSecretRequest, createEmailSecretRequest);
    }

    private static String montarJson(Map<String, String> secretMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(secretMap);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static CreateSecretRequest montarCreateRequest(String secretName, String secretValue) {
        return CreateSecretRequest.builder()
                .name(secretName)
                .secretString(secretValue)
                .build();
    }

    private static SecretsManagerClient getSecretsManagerClient() {
        return SecretsManagerClient.builder()
                .region(Region.of(localStackContainer.getRegion()))
                .endpointOverride(localStackContainer.getEndpointOverride(SECRETSMANAGER))
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
                .build();
    }

    private static AwsBasicCredentials getCredentials() {
        return AwsBasicCredentials.builder()
                .accessKeyId(localStackContainer.getAccessKey())
                .secretAccessKey(localStackContainer.getSecretKey())
                .build();
    }

}
