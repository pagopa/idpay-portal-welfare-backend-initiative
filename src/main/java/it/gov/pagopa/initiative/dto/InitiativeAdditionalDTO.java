package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * InitiativeAdditionalDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitiativeAdditionalDTO extends InitiativeOrganizationInfoDTO {

  private static final String VALID_LINK = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
  public enum ServiceScope{
    LOCAL,
    NATIONAL
  }

  @JsonProperty("serviceIO")
  @NotNull(groups = ValidationOnGroup.class)
  private Boolean serviceIO;

  @JsonProperty("serviceId")
  private String serviceId;

  @JsonProperty("serviceName")
  @NotBlank(groups = ValidationOnGroup.class)
  private String serviceName;

  @JsonProperty("serviceScope")
  @NotNull(groups = ValidationOnGroup.class)
  private ServiceScope serviceScope;

  @JsonProperty("description")
  @NotBlank(groups = ValidationOnGroup.class)
  private String description;

  @JsonProperty("primaryTokenIO")
  private String primaryTokenIO;

  @JsonProperty("secondaryTokenIO")
  private String secondaryTokenIO;

  @JsonProperty("privacyLink")
  @URL(protocol = "https", regexp = VALID_LINK, groups = ValidationOnGroup.class)
  private String privacyLink;

  @JsonProperty("tcLink")
  @URL(protocol = "https", regexp = VALID_LINK, groups = ValidationOnGroup.class)
  private String tcLink;

  @JsonProperty("channels")
  @Valid
  @NotEmpty(groups = ValidationOnGroup.class)
  private List<ChannelDTO> channels;

  @JsonProperty("logoFileName")
  private String logoFileName;

  @JsonProperty("logoURL")
  private String logoURL;

  @JsonProperty("logoUploadDate")
  private LocalDateTime logoUploadDate;

}
