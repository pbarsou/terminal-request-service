package com.desafio.terminalrequest.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        // Cria um ObjectMapper sem depender da auto-config do Spring Boot.
        // Para serialização/leitura de Map (JSON simples) já é suficiente.
        return new ObjectMapper();
    }
}

