package it.gov.pagopa.initiative.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InitiativeBeneficiaryRule {

  private List<ISelfDeclarationCriteria> selfDeclarationCriteria;
  private List<AutomatedCriteria> automatedCriteria;
  private String apiKeyClientId;
  private String apiKeyClientAssertion;

}
