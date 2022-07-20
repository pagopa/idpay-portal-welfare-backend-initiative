package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class InitiativeDTOsToModelMapper {

    public Initiative toInitiative(InitiativeInfoDTO initiativeInfoDto) {
        if (initiativeInfoDto == null) {
            return null;
        }

        Initiative initiative = new Initiative();

        initiative.setGeneral(this.toInitiativeGeneral(initiativeInfoDto.getGeneral()));
        initiative.setAdditionalInfo(this.toInitiativeAdditional(initiativeInfoDto.getAdditionalInfo()));
        return initiative;
    }

    private InitiativeGeneral toInitiativeGeneral(InitiativeGeneralDTO generalDTO) {
        if (generalDTO == null) {
            return null;
        }
        return InitiativeGeneral.builder().beneficiaryBudget(generalDTO.getBeneficiaryBudget())
                .beneficiaryKnown(generalDTO.getBeneficiaryKnown())
                .beneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.valueOf(generalDTO.getBeneficiaryType().name()))
                .budget(generalDTO.getBudget())
                .endDate(generalDTO.getEndDate())
                .startDate(generalDTO.getStartDate())
                .rankingEndDate(generalDTO.getRankingEndDate())
                .rankingStartDate(generalDTO.getRankingStartDate()).build();
    }

    private InitiativeAdditional toInitiativeAdditional(InitiativeAdditionalDTO additionalDTO) {
        if (additionalDTO == null) {
            return null;
        }
        return InitiativeAdditional.builder().serviceId(additionalDTO.getServiceId())
                .argument(additionalDTO.getArgument())
                .description(additionalDTO.getDescription())
                .serviceName(additionalDTO.getServiceName()).build();
    }

    public InitiativeBeneficiaryRule toBeneficiaryRule(InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        if (beneficiaryRuleDto == null) {
            return null;
        }
        InitiativeBeneficiaryRule beneficiaryRule = new InitiativeBeneficiaryRule();
        if (CollectionUtils.isEmpty(beneficiaryRuleDto.getAutomatedCriteria())) {
            beneficiaryRule.setAutomatedCriteria(Collections.emptyList());
        } else {
            beneficiaryRule.setAutomatedCriteria(beneficiaryRuleDto.getAutomatedCriteria().stream().map(x ->
                    AutomatedCriteria.builder()
                            .code(x.getCode())
                            .field(x.getField())
                            .operator(x.getOperator())
                            .authority(x.getAuthority())//TODO definire modalità di recupero authority
                            .value(x.getValue())
                            .build()
            ).toList());
        }

        if (CollectionUtils.isEmpty(beneficiaryRuleDto.getSelfDeclarationCriteria())) {
            beneficiaryRule.setSelfDeclarationCriteria(Collections.emptyList());
        } else {
            beneficiaryRule.setSelfDeclarationCriteria(beneficiaryRuleDto.getSelfDeclarationCriteria().stream()
                    .map(x -> {
                                if (x instanceof SelfCriteriaBoolDTO) {
                                    return SelfCriteriaBool.builder()
                                            ._type(it.gov.pagopa.initiative.model.TypeBoolEnum.valueOf(((SelfCriteriaBoolDTO) x).getType().name()))
                                            .code(((SelfCriteriaBoolDTO) x).getCode())
                                            .description(((SelfCriteriaBoolDTO) x).getDescription())
                                            .value(((SelfCriteriaBoolDTO) x).getValue())
                                            .build();
                                } else if (x instanceof SelfCriteriaMultiDTO) {
                                    return SelfCriteriaMulti.builder()
                                            ._type(TypeMultiEnum.valueOf(((SelfCriteriaMultiDTO) x).getType().name()))
                                            .code(((SelfCriteriaMultiDTO) x).getCode())
                                            .description(((SelfCriteriaMultiDTO) x).getDescription())
                                            .value(((SelfCriteriaMultiDTO) x).getValue())
                                            .build();
                                }
                                return null;
                            }
                    ).toList());
        }
        return beneficiaryRule;
    }

}
