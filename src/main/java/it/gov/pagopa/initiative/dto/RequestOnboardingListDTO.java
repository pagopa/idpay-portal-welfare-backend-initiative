package it.gov.pagopa.initiative.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
