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
  @JsonSubTypes.Type(value = SelfCriteriaMultiDTO.class, name = "multi_consent"),
  @JsonSubTypes.Type(value = SelfCriteriaBoolDTO.class, name = "boolean")
})
public interface AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

}
