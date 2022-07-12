package it.gov.pagopa.initiative.model;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeBeneficiaryRule {

  private List<ISelfDeclarationCriteria> selfDeclarationCriteria;
  private List<AutomatedCriteria> automatedCriteria;

}
