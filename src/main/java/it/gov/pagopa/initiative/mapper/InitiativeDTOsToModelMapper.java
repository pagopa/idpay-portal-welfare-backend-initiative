package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

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
        return InitiativeAdditional.builder()
                .serviceId(additionalDTO.getServiceId())
                .argument(additionalDTO.getArgument())
                .description(additionalDTO.getDescription())
                .serviceName(additionalDTO.getServiceName())
                .channels(toInitiativeAdditionalChannels(additionalDTO.getChannels()))
                .build();
    }

    private List<Channel> toInitiativeAdditionalChannels(List<ChannelDTO> channels) {
        if (CollectionUtils.isEmpty(channels)) {
            return Collections.emptyList();
        } else {
            return channels.stream().map(dto ->
                    Channel.builder()
                            .type(Channel.TypeEnum.valueOf(dto.getType().name()))
                            .contact(dto.getContact())
                            .build()
            ).toList();
        }
    }

    public InitiativeBeneficiaryRule toBeneficiaryRule(InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        if (beneficiaryRuleDto == null) {
            return null;
        }
        InitiativeBeneficiaryRule beneficiaryRule = new InitiativeBeneficiaryRule();
        if (CollectionUtils.isEmpty(beneficiaryRuleDto.getAutomatedCriteria())) {
            beneficiaryRule.setAutomatedCriteria(Collections.emptyList());
        } else {
            beneficiaryRule.setAutomatedCriteria(beneficiaryRuleDto.getAutomatedCriteria().stream().map(dto ->
                    AutomatedCriteria.builder()
                            .code(dto.getCode())
                            .field(dto.getField())
                            .operator(dto.getOperator())
                            .authority(dto.getAuthority())//TODO definire modalità di recupero authority
                            .value(dto.getValue())
                            .build()
            ).toList());
        }

        if (CollectionUtils.isEmpty(beneficiaryRuleDto.getSelfDeclarationCriteria())) {
            beneficiaryRule.setSelfDeclarationCriteria(Collections.emptyList());
        } else {
            beneficiaryRule.setSelfDeclarationCriteria(beneficiaryRuleDto.getSelfDeclarationCriteria().stream()
                    .map(dto -> {
                                if (dto instanceof SelfCriteriaBoolDTO) {
                                    return SelfCriteriaBool.builder()
                                            ._type(it.gov.pagopa.initiative.model.TypeBoolEnum.valueOf(((SelfCriteriaBoolDTO) dto).getType().name()))
                                            .code(((SelfCriteriaBoolDTO) dto).getCode())
                                            .description(((SelfCriteriaBoolDTO) dto).getDescription())
                                            .value(((SelfCriteriaBoolDTO) dto).getValue())
                                            .build();
                                } else if (dto instanceof SelfCriteriaMultiDTO) {
                                    return SelfCriteriaMulti.builder()
                                            ._type(TypeMultiEnum.valueOf(((SelfCriteriaMultiDTO) dto).getType().name()))
                                            .code(((SelfCriteriaMultiDTO) dto).getCode())
                                            .description(((SelfCriteriaMultiDTO) dto).getDescription())
                                            .value(((SelfCriteriaMultiDTO) dto).getValue())
                                            .build();
                                }
                                return null;
                            }
                    ).toList());
        }
        return beneficiaryRule;
    }

}
