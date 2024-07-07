package com.gasfgrv.barbearia.config;

import com.gasfgrv.barbearia.utils.LocalStackContainerUtils;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractContainerIntegrationTestConfig {

    @LocalServerPort
    protected int port;

    protected static LocalStackContainer localStackContainer = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:latest"))
            .withServices(SECRETSMANAGER, S3);

    protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14-alpine"))
            .withDatabaseName("barbearia")
            .withUsername("postgres")
            .withPassword("postgres");

    @RegisterExtension
    protected static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("testUser", "1234567890"))
            .withPerMethodLifecycle(false);

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
        LocalStackContainerUtils.criarSecrets(localStackContainer);
    }

    @AfterAll
    static void afterAll() {
        LocalStackContainerUtils.destruirSecrets(localStackContainer);
    }

}
