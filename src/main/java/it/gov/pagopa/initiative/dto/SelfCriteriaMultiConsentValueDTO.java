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

}
