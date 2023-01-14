package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import java.time.LocalDate;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
  @Valid
  @NotEmpty(groups = ValidationOnGroup.class)
  private Map<String, String> descriptionMap;

  @JsonProperty("startDate")
  @NotNull(groups = ValidationOnGroup.class)
  private LocalDate startDate;

  @JsonProperty("endDate")
  @NotNull(groups = ValidationOnGroup.class)
  private LocalDate endDate;

  @JsonProperty("rankingEnabled")
  private Boolean rankingEnabled;

  @JsonProperty("rankingStartDate")
  @FutureOrPresent(groups = ValidationOnGroup.class)
  private LocalDate rankingStartDate;

  @JsonProperty("rankingEndDate")
  @Future(groups = ValidationOnGroup.class)
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
