package it.gov.pagopa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
public class InitiativeLegal   {

  private String privacyLink;
  private String tcLink;
  private String regulationLink;
  private String dpiaLink;

}
