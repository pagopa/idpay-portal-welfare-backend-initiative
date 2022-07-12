package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

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
