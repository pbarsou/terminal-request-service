package com.desafio.terminalrequest.infrastructure.adapter;

import com.desafio.terminalrequest.domain.service.CustomerValidationServicePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "aws.lambda.enabled", havingValue = "false", matchIfMissing = true)
public class LocalCustomerValidationAdapter implements CustomerValidationServicePort {

    @Override
    public boolean isActiveCustomer(String customerId) {
        return customerId != null && !customerId.startsWith("INVALID");
    }
}
