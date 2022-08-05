package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.reward.RewardValue;
import it.gov.pagopa.initiative.model.rule.trx.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
                            .operator(FilterOperatorEnumModel.valueOf(dto.getOperator().name()))
                            .authority(dto.getAuthority())//TODO definire modalità di recupero authority
                            .value(dto.getValue())
                            .value2(dto.getValue2())
                            .build()
            ).toList());
        }

        if (CollectionUtils.isEmpty(beneficiaryRuleDto.getSelfDeclarationCriteria())) {
            beneficiaryRule.setSelfDeclarationCriteria(Collections.emptyList());
        } else {
            beneficiaryRule.setSelfDeclarationCriteria(beneficiaryRuleDto.getSelfDeclarationCriteria().stream()
                    .map(dto -> {
                                if (dto instanceof SelfCriteriaBoolDTO selfCriteriaBoolDTOInput) {
                                    return SelfCriteriaBool.builder()
                                            ._type(it.gov.pagopa.initiative.model.TypeBoolEnum.valueOf(selfCriteriaBoolDTOInput.getType().name()))
                                            .code(selfCriteriaBoolDTOInput.getCode())
                                            .description(selfCriteriaBoolDTOInput.getDescription())
                                            .value(selfCriteriaBoolDTOInput.getValue())
                                            .build();
                                } else if (dto instanceof SelfCriteriaMultiDTO selfCriteriaMultiDTO) {
                                    return SelfCriteriaMulti.builder()
                                            ._type(TypeMultiEnum.valueOf(selfCriteriaMultiDTO.getType().name()))
                                            .code(selfCriteriaMultiDTO.getCode())
                                            .description(selfCriteriaMultiDTO.getDescription())
                                            .value(selfCriteriaMultiDTO.getValue())
                                            .build();
                                }
                                return null;
                            }
                    ).toList());
        }
        return beneficiaryRule;
    }

    public Initiative toInitiative(InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDto) {
        if (initiativeRewardAndTrxRulesDto == null) {
            return null;
        }
        Initiative initiative = new Initiative();
        initiative.setTrxRule(this.toInitiativeTrxRule(initiativeRewardAndTrxRulesDto.getTrxRule()));
        initiative.setRewardRule(this.toInitiativeRewardRule(initiativeRewardAndTrxRulesDto.getRewardRule()));
        return initiative;
    }

    private InitiativeRewardRule toInitiativeRewardRule(InitiativeRewardRuleDTO rewardRuleDTO) {
        if (rewardRuleDTO == null) {
            return null;
        }
        InitiativeRewardRule ret;
        if(rewardRuleDTO instanceof RewardValueDTO rewardValueInput){
            ret = RewardValue.builder().rewardValue(rewardValueInput.getRewardValue()).build();
        } else if (rewardRuleDTO instanceof RewardGroupsDTO rewardGroupsInput) {
            ret = RewardGroups.builder().rewardGroups(rewardGroupsInput.getRewardGroups().stream().map(
                    x -> RewardGroups.RewardGroup.builder().from(x.getFrom()).to(x.getTo()).rewardValue(x.getRewardValue()).build()
            ).collect(Collectors.toList())).build();
        } else {
            throw new IllegalArgumentException("Initiative Reward Rule not handled: %s".formatted(rewardRuleDTO.getClass().getName()));
        }
        return ret;
    }

    private InitiativeTrxConditions toInitiativeTrxRule(InitiativeTrxConditionsDTO trxRulesDTO) {
        if (trxRulesDTO == null) {
            return null;
        }
        return InitiativeTrxConditions.builder()
                .daysOfWeek(this.toDaysOfWeek(trxRulesDTO.getDaysOfWeek()))
                .mccFilter(this.toMccFilter(trxRulesDTO.getMccFilter()))
                .rewardLimits(this.toRewardLimits(trxRulesDTO.getRewardLimits()))
                .trxCount(this.toTrxCount(trxRulesDTO.getTrxCount()))
                .threshold(this.toThreshold(trxRulesDTO.getThreshold()))
                .build();
    }

    private TrxCount toTrxCount(TrxCountDTO trxCountDTO) {
        if (trxCountDTO == null) {
            return null;
        }
        return TrxCount.builder().to(trxCountDTO.getTo())
                .from(trxCountDTO.getFrom())
                .fromIncluded(trxCountDTO.getFromIncluded())
                .toIncluded(trxCountDTO.getToIncluded()).build();
    }

    private MccFilter toMccFilter(MccFilterDTO mccFilterDTO) {
        if (mccFilterDTO == null) {
            return null;
        }
        return MccFilter.builder()
                .allowedList(mccFilterDTO.isAllowedList())
                .values(mccFilterDTO.getValues())
                .build();
    }

    private DayOfWeek toDaysOfWeek(DayOfWeekDTO dayOfWeekDTO) {
        if (dayOfWeekDTO == null) {
            return null;
        }
        return new DayOfWeek(dayOfWeekDTO.stream().map(x -> DayOfWeek.DayConfig.builder()
                        .daysOfWeek(x.getDaysOfWeek())
                        .intervals(x.getIntervals().stream().map(i -> DayOfWeek.Interval.builder()
                                        .startTime(i.getStartTime())
                                        .endTime(i.getEndTime())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList()));
    }

    private List<RewardLimits> toRewardLimits(List<RewardLimitsDTO> rewardLimitDTO) {
        if (CollectionUtils.isEmpty(rewardLimitDTO)) {
            return Collections.emptyList();
        }
        return rewardLimitDTO.stream().map(x -> RewardLimits.builder()
                        .frequency(RewardLimits.RewardLimitFrequency.valueOf(x.getFrequency().name()))
                        .rewardLimit(x.getRewardLimit())
                        .build())
                .collect(Collectors.toList());
    }

    private Threshold toThreshold(ThresholdDTO thresholdDTO) {
        if (thresholdDTO == null) {
            return null;
        }
        return Threshold.builder().from(thresholdDTO.getFrom())
                .to(thresholdDTO.getTo())
                .fromIncluded(thresholdDTO.getFromIncluded())
                .toIncluded(thresholdDTO.getToIncluded()).build();
    }
}
