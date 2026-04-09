package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RequestOnboardingListDTO {
  private String userId;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
}
