package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RequestOnboardingListDTO {
  private String userId;
  private Instant startDate;
  private Instant endDate;
  private String status;
}
