package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.service.CustomerServicePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "aws.lambda.enabled", havingValue = "false", matchIfMissing = true)
public class LocalCustomerValidationAdapter implements CustomerServicePort {

    @Override
    public boolean isActiveCustomer(String customerId) {
        // Fallback local: aceita todos os clientes que não começam com "INVALID"
        return customerId != null && !customerId.startsWith("INVALID");
    }
}
