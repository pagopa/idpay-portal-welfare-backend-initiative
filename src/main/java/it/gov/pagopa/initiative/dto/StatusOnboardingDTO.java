package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class StatusOnboardingDTO {
  private String beneficiary;
  private String beneficiaryState;
  private String updateStatusDate;
  private String familyId;
}
