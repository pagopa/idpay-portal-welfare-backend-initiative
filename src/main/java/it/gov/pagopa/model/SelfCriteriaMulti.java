package it.gov.pagopa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.dto.AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;


@Getter
@Setter
public class SelfCriteriaMulti implements ISelfDeclarationCriteria {

  private TypeEnum _type;
  private String description;
  private List<String> value;
  private String code;
  
}
