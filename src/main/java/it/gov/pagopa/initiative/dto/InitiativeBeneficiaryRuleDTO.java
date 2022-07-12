package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * InitiativeBeneficiaryRuleDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-10T13:24:21.794Z[GMT]")

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeBeneficiaryRuleDTO   {

  @JsonProperty("selfDeclarationCriteria")
  @Valid
  private List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> selfDeclarationCriteria;

  @JsonProperty("automatedCriteria")
  @Valid
  private List<AutomatedCriteriaDTO> automatedCriteria;

}
