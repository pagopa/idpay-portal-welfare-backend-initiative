package it.gov.pagopa.initiative.model;

import it.gov.pagopa.initiative.dto.SelfCriteriaMultiConsentValueDTO;
import it.gov.pagopa.initiative.dto.TypeMultiConsentEnum;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SelfCriteriaMultiConsent implements ISelfDeclarationCriteria {

  private TypeMultiConsentEnum _type;
  private String description;
  private String subDescription;
  private String thresholdCode;
  private List<SelfCriteriaMultiConsentValueDTO> value;
  private String code;
  
}
