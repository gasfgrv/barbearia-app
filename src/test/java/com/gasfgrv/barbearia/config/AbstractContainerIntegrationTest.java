package com.gasfgrv.barbearia.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AbstractContainerIntegrationTest {

    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:latest"))
            .withServices(SECRETSMANAGER, S3)
            .withReuse(true);

    @DynamicPropertySource
    private static void containerConfig(DynamicPropertyRegistry registry) {
        registry.add("api.security.token.secret", LOCAL_STACK_CONTAINER::getFirstMappedPort);

        registry.add("aws.region", LOCAL_STACK_CONTAINER::getRegion);
        registry.add("aws.endpoint.secretsManager", () -> LOCAL_STACK_CONTAINER.getEndpointOverride(SECRETSMANAGER));
        registry.add("aws.endpoint.s3", () -> LOCAL_STACK_CONTAINER.getEndpointOverride(S3));

        registry.add("email.secret", () -> "emailSecretTest");
    }

    @BeforeAll
    static void beforeAll() {
        LOCAL_STACK_CONTAINER.start();
        criarSecret();
    }

    @AfterAll
    static void afterAll() {
        LOCAL_STACK_CONTAINER.stop();
    }

    private static void criarSecret() {
        AwsBasicCredentials credentials = AwsBasicCredentials.builder()
                .accessKeyId(LOCAL_STACK_CONTAINER.getAccessKey())
                .secretAccessKey(LOCAL_STACK_CONTAINER.getSecretKey())
                .build();

        try (SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion()))
                .endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(SECRETSMANAGER))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build()) {

            CreateSecretRequest createSecretRequest = CreateSecretRequest.builder()
                    .name("emailSecretTest")
                    .secretString("{\"auth\":\"true\",\"host\":\"sandbox.smtp\",\"password\":\"1234567890\",\"port\":\"2525\",\"starttls\":\"true\",\"username\":\"testUser\"}")
                    .build();

            secretsManagerClient.createSecret(createSecretRequest);
        }
    }

}
