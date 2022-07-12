package it.gov.pagopa.initiative.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems
*/
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = SelfCriteriaMultiDTO.class, name = "multi"),
  @JsonSubTypes.Type(value = SelfCriteriaBoolDTO.class, name = "boolean")
})
public interface AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems {

}
