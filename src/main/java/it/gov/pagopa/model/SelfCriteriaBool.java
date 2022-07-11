package it.gov.pagopa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.dto.AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems;
import lombok.*;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
public class SelfCriteriaBool implements ISelfDeclarationCriteria {

  private TypeEnum _type;
  private String description;
  private Boolean value;
  private String code;

}
