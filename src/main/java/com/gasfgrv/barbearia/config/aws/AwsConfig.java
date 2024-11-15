package com.gasfgrv.barbearia.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.endpoint.secretsManager}")
    private String endpointSecretsManager;

    @Value("${aws.endpoint.s3}")
    private String endpointS3;

    @Bean
    public S3AsyncClient s3Client(AwsCredentialsProvider credentialsProvider) {
        return S3AsyncClient.builder()
                .region(Region.of(region))
                .endpointOverride(montarEndpoint(endpointS3))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public SecretsManagerClient secretsManagerClient(AwsCredentialsProvider credentialsProvider) {
        return SecretsManagerClient.builder()
                .region(Region.of(region))
                .endpointOverride(montarEndpoint(endpointSecretsManager))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public AwsCredentialsProvider getCredentialsProvider() {
        return DefaultCredentialsProvider.builder().build();
    }

    private URI montarEndpoint(String serviceEndpoint) {
        return URI.create(serviceEndpoint.matches("^(http://|https://).*")
                ? serviceEndpoint : "https://" + serviceEndpoint);
    }

}
