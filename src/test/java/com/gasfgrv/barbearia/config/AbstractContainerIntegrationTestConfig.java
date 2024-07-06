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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest;

import java.util.Map;
import java.util.Set;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AbstractContainerIntegrationTestConfig {

    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:latest"))
            .withServices(SECRETSMANAGER, S3)
            .withReuse(true);

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14-alpine"))
            .withDatabaseName("barbearia")
            .withUsername("postgres")
            .withPassword("postgres")
            .withReuse(true);

    @DynamicPropertySource
    private static void containerConfig(DynamicPropertyRegistry registry) {
        registry.add("api.security.token.secret", () -> "jwtSecretTest");

        registry.add("aws.region", LOCAL_STACK_CONTAINER::getRegion);
        registry.add("aws.endpoint.secretsManager", () -> LOCAL_STACK_CONTAINER.getEndpointOverride(SECRETSMANAGER));
        registry.add("aws.endpoint.s3", () -> LOCAL_STACK_CONTAINER.getEndpointOverride(S3));

        registry.add("email.secret", () -> "emailSecretTest");

        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        LOCAL_STACK_CONTAINER.start();
        POSTGRE_SQL_CONTAINER.start();
        criarSecret();
    }

    @AfterAll
    static void afterAll() {
        destruirSecrets();
        LOCAL_STACK_CONTAINER.stop();
        POSTGRE_SQL_CONTAINER.stop();
    }

    private static void destruirSecrets() {
        try (SecretsManagerClient secretsManagerClient = getSecretsManagerClient()) {
            deleteSecretRequests().forEach(secretsManagerClient::deleteSecret);
        }
    }

    private static void criarSecret() {
        try (SecretsManagerClient secretsManagerClient = getSecretsManagerClient()) {
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
                        Map.entry("auth", "true"),
                        Map.entry("host", "sandbox.smtp"),
                        Map.entry("password", "1234567890"),
                        Map.entry("port", "2525"),
                        Map.entry("starttls", "true"),
                        Map.entry("username", "testUser")
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
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion()))
                .endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(SECRETSMANAGER))
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
                .build();
    }

    private static AwsBasicCredentials getCredentials() {
        return AwsBasicCredentials.builder()
                .accessKeyId(LOCAL_STACK_CONTAINER.getAccessKey())
                .secretAccessKey(LOCAL_STACK_CONTAINER.getSecretKey())
                .build();
    }

}
