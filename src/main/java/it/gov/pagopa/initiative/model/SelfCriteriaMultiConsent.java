package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonProperty("_type")
  private TypeMultiConsentEnum _type;
  private String description;
  private String subDescription;
  private List<SelfCriteriaMultiConsentValueDTO> value;
  private String code;
  
}
