package com.desafio.terminalrequest.infrastructure.config.exception;

import com.desafio.terminalrequest.domain.exceptions.BusinessException;
import com.desafio.terminalrequest.domain.exceptions.TerminalRequestNotFoundException;
import com.desafio.terminalrequest.infrastructure.config.exception.dto.ResourceError;
import com.desafio.terminalrequest.infrastructure.config.exception.dto.ResourceFieldError;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler({TerminalRequestNotFoundException.class})
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  protected ResponseEntity<Object> handleNotFound(BusinessException ex, WebRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    ResourceError errorBody =
        new ResourceError(status.value(), "Entity Not Found", ex.getMessage());
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, new HttpHeaders(), status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<Object> handleBusinessException(
      BusinessException ex, WebRequest request) {
    HttpStatus status = ex.getCode() != null ? ex.getCode() : HttpStatus.UNPROCESSABLE_ENTITY;
    String title = ex.getReason() != null ? ex.getReason() : "Business Error";
    ResourceError errorBody = new ResourceError(status.value(), title, ex.getMessage());
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, new HttpHeaders(), status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleBadHeaders(IllegalStateException ex, WebRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ResourceError errorBody =
        new ResourceError(
            status.value(),
            "The request is malformed",
            ex.getMessage() != null ? ex.getMessage() : "");
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, new HttpHeaders(), status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleIllegalArgumentError(
      IllegalArgumentException ex, WebRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ResourceError errorBody =
        new ResourceError(
            status.value(),
            "The argument passed is invalid",
            ex.getMessage() != null ? ex.getMessage() : "");
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, new HttpHeaders(), status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String detail =
        (ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null)
            ? ex.getMostSpecificCause().getMessage()
            : ex.getMessage();
    ResourceError errorBody =
        new ResourceError(status.value(), "The argument passed is invalid", detail);
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, new HttpHeaders(), status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    List<ResourceFieldError> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    new ResourceFieldError(
                        error.getField(),
                        error.getDefaultMessage() != null
                            ? error.getDefaultMessage()
                            : "invalid value",
                        error.getRejectedValue()))
            .collect(Collectors.toList());

    ResourceError errorBody =
        new ResourceError(
            status.value(), "Validation error", "One or more fields are invalid", fieldErrors);
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, headers, status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    String detail = "Failed to read request body. Check JSON syntax or field types.";

    if (ex.getCause() instanceof InvalidFormatException invalidFormat) {
      if (invalidFormat.getTargetType().isEnum()) {
        String field =
            invalidFormat.getPath().stream()
                .map(ref -> ref.getPropertyName())
                .collect(Collectors.joining("."));

        detail =
            String.format(
                "Invalid value '%s' for field '%s'. Accepted values are: %s",
                invalidFormat.getValue(),
                field,
                Arrays.toString(invalidFormat.getTargetType().getEnumConstants()));
      }
    }

    ResourceError errorBody = new ResourceError(status.value(), "The request is malformed", detail);
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, headers, status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  protected ResponseEntity<Object> handleGeneric(Exception ex, WebRequest request) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ResourceError errorBody = new ResourceError(status.value(), "Internal Server Error", "");
    ResponseEntity<Object> response =
        handleExceptionInternal(ex, errorBody, new HttpHeaders(), status, request);
    logger.error("{}", errorBody, ex);
    return response;
  }
}
