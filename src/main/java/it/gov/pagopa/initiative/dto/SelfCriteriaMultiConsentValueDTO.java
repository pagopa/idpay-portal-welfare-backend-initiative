package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelfCriteriaMultiConsentValueDTO {

  private String description;

  private String subDescription;

  private String value;

  private boolean verify;

  private String thresholdCode;

  private Long beneficiaryBudgetCentsMin;

  private Long beneficiaryBudgetCentsMax;

  private boolean blockingVerify;

}
