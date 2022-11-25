package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingStatusCitizenDTO {

  private String userId;
  private String status;
  private String statusDate;

}

