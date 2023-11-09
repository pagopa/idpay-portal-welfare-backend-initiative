package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import java.time.LocalDate;
import java.util.Map;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * InitiativeDTO
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeIssuerDTO {

  @JsonProperty("initiativeId")
  private String initiativeId;

  @JsonProperty("initiativeName")
  private String initiativeName;

  @JsonProperty("organizationName")
  private String organizationName;

  @JsonProperty("descriptionMap")
  private Map<String, String> descriptionMap;

  @JsonProperty("startDate")
  private LocalDate startDate;

  @JsonProperty("endDate")
  private LocalDate endDate;

  @JsonProperty("rankingEnabled")
  private Boolean rankingEnabled;

  @JsonProperty("rankingStartDate")
  @FutureOrPresent(groups = ValidationApiEnabledGroup.class)
  private LocalDate rankingStartDate;

  @JsonProperty("rankingEndDate")
  @Future(groups = ValidationApiEnabledGroup.class)
  private LocalDate rankingEndDate;

  @JsonProperty("beneficiaryKnown")
  private Boolean beneficiaryKnown;

  @JsonProperty("status")
  private String status;

  @JsonProperty("tcLink")
  private String tcLink;

  @JsonProperty("privacyLink")
  private String privacyLink;

  @JsonProperty("logoURL")
  private String logoURL;

}
