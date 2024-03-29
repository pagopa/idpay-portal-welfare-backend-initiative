package it.gov.pagopa.initiative.dto.io.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseErrorDTO {

  /**
   * An absolute URI that identifies the problem type. When dereferenced, it SHOULD provide human-readable documentation for the problem type (e.g., using HTML).
   */
  private String type = "about:blank";
  /**
   * A short, summary of the problem type. Written in english and readable for engineers (usually not suited for non-technical stakeholders and not localized); example: Service Unavailable
   */
  private String title;
  /**
   * The HTTP status code generated by the origin server for this occurrence of the problem.
   */
  private Integer status;
  /**
   * A human-readable explanation specific to this occurrence of the problem.
   */
  private String detail;
  /**
   * An absolute URI that identifies the specific occurrence of the problem. It may or may not yield further information if dereferenced.
   */
  private String instance;

}

