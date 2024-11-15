package com.gasfgrv.barbearia.config.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

    private final AwsProperties awsProperties;

    @Bean
    public S3AsyncClient s3Client(AwsCredentialsProvider credentialsProvider) {
        return S3AsyncClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .endpointOverride(montarEndpoint(awsProperties.getEndpointS3()))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public SecretsManagerClient secretsManagerClient(AwsCredentialsProvider credentialsProvider) {
        return SecretsManagerClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .endpointOverride(montarEndpoint(awsProperties.getEndpointSecretsManager()))
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
