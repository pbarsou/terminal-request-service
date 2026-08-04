package com.desafio.terminalrequest.integrated;

import com.desafio.terminalrequest.infrastructure.config.TestContainersConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfiguration.class)
@Testcontainers
@AutoConfigureTestRestTemplate
public abstract class IntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;
}
