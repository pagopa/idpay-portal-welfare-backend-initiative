package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.model.TypeEnum;
import it.gov.pagopa.initiative.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class InitiativeMapper {

    public Initiative toInitiativeInfoModel(InitiativeInfoDTO initiativeInfoDto) {
        if (initiativeInfoDto == null) {
            return null;
        }

        Initiative initiative = new Initiative();

        initiative.setGeneral(this.toGeneralModel(initiativeInfoDto.getGeneral()));
        initiative.setAdditionalInfo(this.toInitiativeAdditionalModel(initiativeInfoDto.getAdditionalInfo()));
        return initiative;
    }

    private InitiativeGeneral toGeneralModel(InitiativeGeneralDTO generalDTO) {
        if (generalDTO == null) {
            return null;
        }
        return InitiativeGeneral.builder().beneficiaryBudget(generalDTO.getBeneficiaryBudget())
                .beneficiaryKnown(generalDTO.getBeneficiaryKnown())
                .beneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.valueOf(generalDTO.getBeneficiaryType().name()))
                .budget(generalDTO.getBudget())
                //.name(generalDTO.getName())
                .endDate(generalDTO.getEndDate())
                .startDate(generalDTO.getStartDate())
                .rankingEndDate(generalDTO.getRankingEndDate())
                .rankingStartDate(generalDTO.getRankingStartDate()).build();
    }

    private InitiativeAdditional toInitiativeAdditionalModel(InitiativeAdditionalDTO additionalDTO) {
        if (additionalDTO == null) {
            return null;
        }
        return InitiativeAdditional.builder().serviceId(additionalDTO.getServiceId())
                .argument(additionalDTO.getArgument())
                .description(additionalDTO.getDescription())
                .serviceName(additionalDTO.getServiceName()).build();
    }

    public Initiative toBeneficiaryRuleModel(InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        if (beneficiaryRuleDto == null) {
            return null;
        }
        Initiative initiative = new Initiative();
        InitiativeBeneficiaryRule beneficiaryRule = new InitiativeBeneficiaryRule();
        if (CollectionUtils.isEmpty(beneficiaryRuleDto.getAutomatedCriteria())) {
            beneficiaryRule.setAutomatedCriteria(Collections.emptyList());
        } else {
            //TODO definire modalità di recupero authority
            beneficiaryRule.setAutomatedCriteria(beneficiaryRuleDto.getAutomatedCriteria().stream().map(x ->
                    AutomatedCriteria.builder()
                            .code(x.getCode())
                            .field(x.getField()).operator(x.getOperator())
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
                                            ._type(TypeEnum.valueOf(((SelfCriteriaBoolDTO) x).get_type().name()))
                                            .code(((SelfCriteriaBoolDTO) x).getCode())
                                            .description(((SelfCriteriaBoolDTO) x).getDescription())
                                            .value(((SelfCriteriaBoolDTO) x).getValue())
                                            .build();
                                } else if (x instanceof SelfCriteriaMultiDTO) {
                                    return SelfCriteriaMulti.builder()
                                            ._type(TypeEnum.valueOf(((SelfCriteriaMultiDTO) x).get_type().name()))
                                            .code(((SelfCriteriaMultiDTO) x).getCode())
                                            .description(((SelfCriteriaMultiDTO) x).getDescription())
                                            .value(((SelfCriteriaMultiDTO) x).getValue())
                                            .build();
                                }
                                return null;
                            }
                    ).toList());
        }
        initiative.setBeneficiaryRule(beneficiaryRule);
        return initiative;
    }


    public InitiativeDTO toDtoOnlyId(Initiative initiative) {
        if (initiative == null) {
            return null;
        }
        InitiativeDTO dto = new InitiativeDTO();
        dto.setInitiativeId(initiative.getInitiativeId());
        return dto;
    }

    private InitiativeGeneralDTO toGeneralDto(InitiativeGeneral general) {
        if (general == null) {
            return null;
        }
        return InitiativeGeneralDTO.builder().beneficiaryBudget(general.getBeneficiaryBudget())
                .beneficiaryKnown(general.getBeneficiaryKnown())
                .beneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.valueOf(general.getBeneficiaryType().name()))
                .budget(general.getBudget())
                //.name(general.getName())
                .endDate(general.getEndDate())
                .startDate(general.getStartDate())
                .rankingEndDate(general.getRankingEndDate())
                .rankingStartDate(general.getRankingStartDate()).build();
    }

    private InitiativeAdditionalDTO toInitiativeAdditionalDto(InitiativeAdditional additional) {
        if (additional == null) {
            return null;
        }
        return InitiativeAdditionalDTO.builder().serviceId(additional.getServiceId())
                .argument(additional.getArgument())
                .description(additional.getDescription())
                .serviceName(additional.getServiceName()).build();
    }

    public InitiativeBeneficiaryRuleDTO toBeneficiaryRuleDto(InitiativeBeneficiaryRule beneficiaryRule) {
        if (beneficiaryRule == null) {
            return null;
        }
        InitiativeBeneficiaryRuleDTO beneficiaryRuleDto = new InitiativeBeneficiaryRuleDTO();
        if (CollectionUtils.isEmpty(beneficiaryRule.getAutomatedCriteria())) {
            beneficiaryRuleDto.setAutomatedCriteria(Collections.emptyList());
        } else {
            //TODO definire modalità di recupero authority
            beneficiaryRuleDto.setAutomatedCriteria(beneficiaryRule.getAutomatedCriteria().stream().map(x ->
                    AutomatedCriteriaDTO.builder()
                            .code(x.getCode())
                            .field(x.getField()).operator(x.getOperator())
                            .build()
            ).toList());
        }

        if (CollectionUtils.isEmpty(beneficiaryRule.getSelfDeclarationCriteria())) {
            beneficiaryRuleDto.setSelfDeclarationCriteria(Collections.emptyList());
        } else {
            beneficiaryRuleDto.setSelfDeclarationCriteria(beneficiaryRule.getSelfDeclarationCriteria().stream()
                    .map(x -> {
                                if (x instanceof SelfCriteriaBool) {
                                    return SelfCriteriaBoolDTO.builder()
                                            ._type(it.gov.pagopa.initiative.dto.TypeEnum.valueOf(((SelfCriteriaBool) x).get_type().name()))
                                            .code(((SelfCriteriaBool) x).getCode())
                                            .description(((SelfCriteriaBool) x).getDescription())
                                            .value(((SelfCriteriaBool) x).getValue())
                                            .build();
                                } else if (x instanceof SelfCriteriaMulti) {
                                    return SelfCriteriaMultiDTO.builder()
                                            ._type(it.gov.pagopa.initiative.dto.TypeEnum.valueOf(((SelfCriteriaMulti) x).get_type().name()))
                                            .code(((SelfCriteriaMulti) x).getCode())
                                            .description(((SelfCriteriaMulti) x).getDescription())
                                            .value(((SelfCriteriaMulti) x).getValue())
                                            .build();
                                }
                                return null;
                            }
                    ).toList());
        }
        return beneficiaryRuleDto;
    }

    public List<InitiativeSummaryDTO> toInitiativeSummaryDtoList(List<Initiative> initiatives) {
        if (CollectionUtils.isEmpty(initiatives)) {
            return Collections.emptyList();
        }
        return initiatives.stream().map(x -> InitiativeSummaryDTO.builder()
                .initiativeId(x.getInitiativeId())
                .initiativeName(StringUtils.isNotBlank(x.getInitiativeName()) ?
                        x.getInitiativeName() :
                        x.getAdditionalInfo() != null ?
                                x.getAdditionalInfo().getServiceName()
                                : StringUtils.EMPTY)
                .status(x.getStatus())
                .build()).toList();
    }

    public InitiativeDTO toInitiativeDto(Initiative initiative) {
        if (initiative == null) {
            return null;
        }
        InitiativeDTO initiativeDto = this.toDtoOnlyId(initiative);
        initiativeDto.setInitiativeName(initiative.getInitiativeName());
        initiativeDto.setStatus(initiative.getStatus());
        initiativeDto.setGeneral(this.toGeneralDto(initiative.getGeneral()));
        initiativeDto.setAdditionalInfo(this.toInitiativeAdditionalDto(initiative.getAdditionalInfo()));
        initiativeDto.setBeneficiaryRule(this.toBeneficiaryRuleDto(initiative.getBeneficiaryRule()));
        return initiativeDto;
    }


}
