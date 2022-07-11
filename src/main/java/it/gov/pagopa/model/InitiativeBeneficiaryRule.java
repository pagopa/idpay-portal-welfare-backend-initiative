package it.gov.pagopa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.dto.AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems;
import it.gov.pagopa.dto.AutomatedCriteriaDTO;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class InitiativeBeneficiaryRule {

  private List<ISelfDeclarationCriteria> selfDeclarationCriteria;
  private List<AutomatedCriteriaDTO> automatedCriteria;

}
