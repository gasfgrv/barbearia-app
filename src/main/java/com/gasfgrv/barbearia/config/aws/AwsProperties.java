package com.gasfgrv.barbearia.config.aws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Setter
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    @Getter
    private String region;
    private Endpoints endpoint;

    public String getEndpointS3() {
        return endpoint.s3;
    }

    public String getEndpointSecretsManager() {
        return endpoint.secretsManager;
    }

    @Getter
    @Setter
    public static class Endpoints {
        private String secretsManager;
        private String s3;
    }

}
