package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class InitiativeModelToDTOMapper {

    public InitiativeDTO toInitiativeDTO(Initiative initiative) {
        if (initiative == null) {
            return null;
        }
        InitiativeDTO initiativeDto = this.toDtoOnlyId(initiative);
        initiativeDto.setInitiativeName(initiative.getInitiativeName());
        initiativeDto.setStatus(initiative.getStatus());
        initiativeDto.setOrganizationId(initiative.getOrganizationId());
        initiativeDto.setCreationDate(initiative.getCreationDate());
        initiativeDto.setUpdateDate(initiative.getUpdateDate());
        initiativeDto.setPdndToken(initiative.getPdndToken());
        initiativeDto.setPdndCheck(initiative.getPdndCheck());
        initiativeDto.setAutocertificationCheck(initiative.getAutocertificationCheck());
        initiativeDto.setBeneficiaryRanking(initiative.getBeneficiaryRanking());
        initiativeDto.setGeneral(this.toInitiativeGeneralDTO(initiative.getGeneral()));
        initiativeDto.setAdditionalInfo(this.toInitiativeAdditionalDTO(initiative.getAdditionalInfo()));
        initiativeDto.setBeneficiaryRule(this.toInitiativeBeneficiaryRuleDTO(initiative.getBeneficiaryRule()));
        return initiativeDto;
    }

    public InitiativeDTO toDtoOnlyId(Initiative initiative) {
        if (initiative == null) {
            return null;
        }
        InitiativeDTO dto = new InitiativeDTO();
        dto.setInitiativeId(initiative.getInitiativeId());
        return dto;
    }

    private InitiativeGeneralDTO toInitiativeGeneralDTO(InitiativeGeneral general) {
        if (general == null) {
            return null;
        }
        return InitiativeGeneralDTO.builder().beneficiaryBudget(general.getBeneficiaryBudget())
                .beneficiaryKnown(general.getBeneficiaryKnown())
                .beneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.valueOf(general.getBeneficiaryType().name()))
                .budget(general.getBudget())
                .endDate(general.getEndDate())
                .startDate(general.getStartDate())
                .rankingEndDate(general.getRankingEndDate())
                .rankingStartDate(general.getRankingStartDate()).build();
    }

    private InitiativeAdditionalDTO toInitiativeAdditionalDTO(InitiativeAdditional additional) {
        if (additional == null) {
            return null;
        }
        return InitiativeAdditionalDTO.builder()
                .serviceId(additional.getServiceId())
                .argument(additional.getArgument())
                .description(additional.getDescription())
                .serviceName(additional.getServiceName())
                .channels(toChannelsDTO(additional.getChannels()))
                .build();
    }

    private List<ChannelDTO> toChannelsDTO(List<Channel> channels) {
        if (CollectionUtils.isEmpty(channels)) {
            return Collections.emptyList();
        } else {
            return channels.stream().map(channel ->
                    ChannelDTO.builder()
                            .type(ChannelDTO.TypeEnum.valueOf(channel.getType().name()))
                            .contact(channel.getContact())
                            .build()
            ).toList();
        }
    }

    public InitiativeBeneficiaryRuleDTO toInitiativeBeneficiaryRuleDTO(InitiativeBeneficiaryRule beneficiaryRule) {
        if (beneficiaryRule == null) {
            return null;
        }
        InitiativeBeneficiaryRuleDTO beneficiaryRuleDto = new InitiativeBeneficiaryRuleDTO();
        if (CollectionUtils.isEmpty(beneficiaryRule.getAutomatedCriteria())) {
            beneficiaryRuleDto.setAutomatedCriteria(Collections.emptyList());
        } else {

            beneficiaryRuleDto.setAutomatedCriteria(beneficiaryRule.getAutomatedCriteria().stream().map(x ->
                    AutomatedCriteriaDTO.builder()
                            .code(x.getCode())
                            .field(x.getField())
                            .operator(x.getOperator())
                            .authority(x.getAuthority())//TODO definire modalità di recupero authority
                            .value(x.getValue())
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
                                            .type(it.gov.pagopa.initiative.dto.TypeBoolEnum.valueOf(((SelfCriteriaBool) x).get_type().name()))
                                            .code(((SelfCriteriaBool) x).getCode())
                                            .description(((SelfCriteriaBool) x).getDescription())
                                            .value(((SelfCriteriaBool) x).getValue())
                                            .build();
                                } else if (x instanceof SelfCriteriaMulti) {
                                    return SelfCriteriaMultiDTO.builder()
                                            .type(it.gov.pagopa.initiative.dto.TypeMultiEnum.valueOf(((SelfCriteriaMulti) x).get_type().name()))
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

    public List<InitiativeSummaryDTO> toInitiativeSummaryDTOList(List<Initiative> initiatives) {
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
                .creationDate(x.getCreationDate())
                .updateDate(x.getUpdateDate())
                .build()).toList();
    }


}
