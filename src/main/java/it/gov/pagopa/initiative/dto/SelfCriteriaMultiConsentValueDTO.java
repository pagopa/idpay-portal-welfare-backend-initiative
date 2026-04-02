package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
