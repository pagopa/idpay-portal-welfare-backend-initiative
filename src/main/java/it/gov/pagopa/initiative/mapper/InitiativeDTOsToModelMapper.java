package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
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

    public Initiative toInitiative(InitiativeAdditionalDTO initiativeAdditionalDTO) {
        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(this.toInitiativeAdditional(initiativeAdditionalDTO));
        if(null != initiative.getAdditionalInfo() && null != initiative.getAdditionalInfo().getServiceName())
            initiative.setInitiativeName(initiative.getAdditionalInfo().getServiceName());
        return initiative;
    }

    public Initiative toInitiative(InitiativeGeneralDTO initiativeGeneralDTO) {
        Initiative initiative = new Initiative();
        initiative.setGeneral(toInitiativeGeneral(initiativeGeneralDTO));
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
                .serviceIO(additionalDTO.getServiceIO())
                .serviceId(additionalDTO.getServiceId())
                .description(additionalDTO.getDescription())
                .serviceName(additionalDTO.getServiceName())
                .serviceScope(InitiativeAdditional.ServiceScope.valueOf(additionalDTO.getServiceScope().name()))
                .privacyLink(additionalDTO.getPrivacyLink())
                .tcLink(additionalDTO.getTcLink())
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
            ret = RewardValue.builder()
                    .type(rewardValueInput.getType())
                    .rewardValue(rewardValueInput.getRewardValue())
                    .build();
        } else if (rewardRuleDTO instanceof RewardGroupsDTO rewardGroupsInput) {
            ret = RewardGroups.builder()
                    .type(rewardGroupsInput.getType())
                    .rewardGroups(rewardGroupsInput.getRewardGroups().stream().map(
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
                                .toList())
                        .build())
                .toList());
    }

    private List<RewardLimits> toRewardLimits(List<RewardLimitsDTO> rewardLimitDTO) {
        if (CollectionUtils.isEmpty(rewardLimitDTO)) {
            return Collections.emptyList();
        }
        return rewardLimitDTO.stream().map(x -> RewardLimits.builder()
                        .frequency(RewardLimits.RewardLimitFrequency.valueOf(x.getFrequency().name()))
                        .rewardLimit(x.getRewardLimit())
                        .build())
                .toList();
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

    public Initiative toInitiative(InitiativeRefundRuleDTO refundRuleDTO){
        Initiative initiative = new Initiative();
        initiative.setRefundRule(toInitiativeRefundRule(refundRuleDTO));
        return initiative;
    }
    private InitiativeRefundRule toInitiativeRefundRule(InitiativeRefundRuleDTO refundRuleDTO){
        if (refundRuleDTO == null){
            return null;
        }
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();
        initiativeRefundRule.setAccumulatedAmount(toAccomulatedAmount(refundRuleDTO.getAccumulatedAmount()));
        initiativeRefundRule.setTimeParameter(toTimeParameter(refundRuleDTO.getTimeParameter()));
        initiativeRefundRule.setAdditionalInfo(toAdditionalInfo(refundRuleDTO.getAdditionalInfo()));
        return initiativeRefundRule;
    }

    private AccumulatedAmount toAccomulatedAmount(AccumulatedAmountDTO accomulatedAmountDTO){
        if(accomulatedAmountDTO == null){
            return null;
        }
        return AccumulatedAmount.builder().accomulatedType(AccumulatedAmount.AccumulatedTypeEnum.valueOf(accomulatedAmountDTO.getAccumulatedType().name()))
                .refundThreshold(accomulatedAmountDTO.getRefundThreshold()).build();
    }

    private TimeParameter toTimeParameter(TimeParameterDTO timeParameterDTO){
        if (timeParameterDTO == null){
            return null;
        }
        return TimeParameter.builder().timeType(TimeParameter.TimeTypeEnum.valueOf(timeParameterDTO.getTimeType().name())).build();
    }

    private AdditionalInfo toAdditionalInfo(RefundAdditionalInfoDTO refundAdditionalInfoDTO){
        if (refundAdditionalInfoDTO == null){
            return null;
        }
        return AdditionalInfo.builder().identificationCode(refundAdditionalInfoDTO.getIdentificationCode()).build();
    }

    public Initiative toInitiative(InitiativeDTO initiativeDTO) {
        InitiativeAdditional initiativeAdditional = toInitiativeAdditional(initiativeDTO.getAdditionalInfo());
        InitiativeGeneral initiativeGeneral = toInitiativeGeneral(initiativeDTO.getGeneral());
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = toBeneficiaryRule(initiativeDTO.getBeneficiaryRule());
        InitiativeRewardRule initiativeRewardRule = toInitiativeRewardRule(initiativeDTO.getRewardRule());
        InitiativeTrxConditions initiativeTrxConditions = toInitiativeTrxRule(initiativeDTO.getTrxRule());
        InitiativeRefundRule initiativeRefundRule = toInitiativeRefundRule(initiativeDTO.getRefundRule());
        return Initiative.builder()
                .initiativeId(initiativeDTO.getInitiativeId())
                .initiativeName(initiativeDTO.getInitiativeName())
                .organizationId(initiativeDTO.getOrganizationId())
                .pdndToken(initiativeDTO.getPdndToken())
                .creationDate(initiativeDTO.getCreationDate())
                .updateDate(initiativeDTO.getUpdateDate())
                .status(initiativeDTO.getStatus())
                .additionalInfo(initiativeAdditional)
                .general(initiativeGeneral)
                .beneficiaryRule(initiativeBeneficiaryRule)
                .rewardRule(initiativeRewardRule)
                .trxRule(initiativeTrxConditions)
                .refundRule(initiativeRefundRule)
                .build();
    }
}
