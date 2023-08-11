package it.gov.pagopa.common.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ErrorDTO
 */
@JsonPropertyOrder({
    it.gov.pagopa.common.web.dto.ErrorDTO.JSON_PROPERTY_CODE,
    it.gov.pagopa.common.web.dto.ErrorDTO.JSON_PROPERTY_MESSAGE
})
@Data
@AllArgsConstructor
public class ErrorDTO {

  public static final String JSON_PROPERTY_CODE = "code";
  private String code;

  public static final String JSON_PROPERTY_MESSAGE = "message";
  private String message;

}
