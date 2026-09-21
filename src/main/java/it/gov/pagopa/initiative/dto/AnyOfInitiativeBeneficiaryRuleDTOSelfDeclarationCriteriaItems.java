package it.gov.pagopa.initiative.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems
*/
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "_type",
        visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = SelfCriteriaTextDTO.class, name = "text"),
  @JsonSubTypes.Type(value = SelfCriteriaMultiDTO.class, name = "multi"),
  @JsonSubTypes.Type(value = SelfCriteriaMultiConsentDTO.class, name = "multi_consent"),
  @JsonSubTypes.Type(value = SelfCriteriaBoolDTO.class, name = "boolean"),
  // BND-1882 / BND-1883: register informative discriminator (end-to-end informative requirements ANPR/ADE)
  @JsonSubTypes.Type(value = SelfCriteriaInformativeDTO.class, name = "informative")
})
public interface AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

}
