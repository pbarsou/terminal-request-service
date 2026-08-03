package com.desafio.terminalrequest.infrastructure.config.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceFieldError {

    private String field;
    private String message;
    
    @JsonProperty("rejected_value")
    private Object rejectedValue;

    public ResourceFieldError() {
    }

    public ResourceFieldError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
