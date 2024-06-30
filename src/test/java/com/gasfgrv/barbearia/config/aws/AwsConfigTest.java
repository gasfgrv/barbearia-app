package com.gasfgrv.barbearia.config.aws;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"aws.region=us-east-1", "aws.endpoint.s3=s3", "aws.endpoint.secretsManager=secrets"})
@SpringBootTest(classes = AwsConfig.class)
class AwsConfigTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar beans dos serviços da AWS")
    void deveCriarBeansDosServicosDaAWS() {
        AwsCredentialsProvider credentialsProvider = applicationContext.getBean(AwsCredentialsProvider.class);
        assertInstanceOf(DefaultCredentialsProvider.class, credentialsProvider);
        assertNotNull(credentialsProvider);

        S3Client s3Client = applicationContext.getBean(S3Client.class);
        assertInstanceOf(S3Client.class, s3Client);
        assertNotNull(s3Client);

        SecretsManagerClient secretsManagerClient = applicationContext.getBean(SecretsManagerClient.class);
        assertInstanceOf(SecretsManagerClient.class, secretsManagerClient);
        assertNotNull(secretsManagerClient);

    }

}