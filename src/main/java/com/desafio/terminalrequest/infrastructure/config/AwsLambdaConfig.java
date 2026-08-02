package com.desafio.terminalrequest.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.lambda.LambdaClient;

@Configuration
public class AwsLambdaConfig {

    @Bean
    public LambdaClient lambdaClient() {
        // Usa a cadeia padrão de credenciais e região do AWS SDK (env/IAM role/etc.).
        return LambdaClient.create();
    }
}

