package com.desafio.terminalrequest.infrastructure.config.exception.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ResourceError {

  private Integer status;
  private String title;
  private String detail;
  private String timestamp;

  @JsonProperty("error_fields")
  private List<ResourceFieldError> errorFields = new ArrayList<>();

  public ResourceError() {}

  public ResourceError(Integer status, String title, String detail) {
    this.status = status;
    this.title = title;
    this.detail = detail;
    this.timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public ResourceError(
      Integer status, String title, String detail, List<ResourceFieldError> errorFields) {
    this(status, title, detail);
    this.errorFields = errorFields;
  }

  public Integer getStatus() {
    return status;
  }

  public String getTitle() {
    return title;
  }

  public String getDetail() {
    return detail;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public List<ResourceFieldError> getErrorFields() {
    return errorFields;
  }
}
