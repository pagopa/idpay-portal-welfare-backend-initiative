package it.gov.pagopa.initiative.dto.io.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * ServiceMetadata
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ServiceMetadataDTO {
  @JsonProperty("web_url")
  private String webUrl;
  @JsonProperty("app_ios")
  private String appIos;
  @JsonProperty("app_android")
  private String appAndroid;
  @JsonProperty("tos_url")
  private String tosUrl;
  @JsonProperty("privacy_url")
  private String privacyUrl;
  private String address;
  private String phone;
  private String email;
  private String pec;
  private String cta;
  @JsonProperty("token_name")
  private String tokenName;
  @JsonProperty("support_url")
  private String supportUrl;
  private String scope;

}

