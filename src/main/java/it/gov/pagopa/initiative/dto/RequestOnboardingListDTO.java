package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RequestOnboardingListDTO {
  private String userId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String status;
}
