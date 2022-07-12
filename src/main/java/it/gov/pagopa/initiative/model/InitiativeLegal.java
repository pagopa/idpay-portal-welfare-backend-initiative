package it.gov.pagopa.initiative.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class InitiativeLegal   {

  private String privacyLink;
  private String tcLink;
  private String regulationLink;
  private String dpiaLink;

}
