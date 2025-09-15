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
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.TypeTextEnum;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.reward.RewardValue;
import it.gov.pagopa.initiative.model.rule.trx.*;
import it.gov.pagopa.initiative.service.AESTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class InitiativeDTOsToModelMapper {

    private final AESTokenService aesTokenService;

    public InitiativeDTOsToModelMapper(AESTokenService aesTokenService) {
        this.aesTokenService = aesTokenService;
    }

    private static Long euroToCents(BigDecimal euro){
        return euro == null? null : euro.multiply(BigDecimal.valueOf(100)).longValue();
    }

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
        return InitiativeGeneral.builder().beneficiaryBudgetCents(euroToCents(generalDTO.getBeneficiaryBudget()))
                .beneficiaryBudgetMaxCents(euroToCents(generalDTO.getBeneficiaryBudgetMax()))
                .beneficiaryKnown(generalDTO.getBeneficiaryKnown())
                .beneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.valueOf(generalDTO.getBeneficiaryType().name()))
                .familyUnitComposition(generalDTO.getFamilyUnitComposition()!=null?generalDTO.getFamilyUnitComposition():null)
                .budgetCents(euroToCents(generalDTO.getBudget()))
                .endDate(generalDTO.getEndDate())
                .startDate(generalDTO.getStartDate())
                .rankingEndDate(generalDTO.getRankingEndDate())
                .rankingStartDate(generalDTO.getRankingStartDate())
                .rankingEnabled(generalDTO.getRankingEnabled())
                .descriptionMap(generalDTO.getDescriptionMap()).build();
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
        beneficiaryRule.setAutomatedCriteria(Optional.ofNullable(beneficiaryRuleDto.getAutomatedCriteria())
                .orElse(Collections.emptyList())
                .stream().map(automatedCriteriaDTO ->
                AutomatedCriteria.builder()
                        .code(automatedCriteriaDTO.getCode())
                        .field(automatedCriteriaDTO.getField())
                        .operator(FilterOperatorEnumModel.valueOf(automatedCriteriaDTO.getOperator().name()))
                        .authority(automatedCriteriaDTO.getAuthority())
                        .value(automatedCriteriaDTO.getValue())
                        .value2(StringUtils.isBlank(automatedCriteriaDTO.getValue2()) ? null : automatedCriteriaDTO.getValue2())
                        .orderDirection(automatedCriteriaDTO.getOrderDirection() != null
                                ? AutomatedCriteria.OrderDirection.valueOf(automatedCriteriaDTO.getOrderDirection().name())
                                : null)
                        .iseeTypes(automatedCriteriaDTO.getIseeTypes())
                        .build()
        ).toList());

        beneficiaryRule.setSelfDeclarationCriteria(Optional.ofNullable(beneficiaryRuleDto.getSelfDeclarationCriteria())
                .orElse(Collections.emptyList())
                .stream().map(dto -> {
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
                    } else if (dto instanceof SelfCriteriaTextDTO selfCriteriaTextDTO) {
                        return SelfCriteriaText.builder()
                                ._type(TypeTextEnum.valueOf(selfCriteriaTextDTO.getType().name()))
                                .code(selfCriteriaTextDTO.getCode())
                                .description(selfCriteriaTextDTO.getDescription())
                                .value(selfCriteriaTextDTO.getValue())
                                .build();
                    }else if (dto instanceof SelfCriteriaMultiConsentDTO selfCriteriaMultiConsentDTO) {
                        return SelfCriteriaMultiConsent.builder()
                                ._type(TypeMultiConsentEnum.valueOf(selfCriteriaMultiConsentDTO.getType().name()))
                                .code(selfCriteriaMultiConsentDTO.getCode())
                                .description(selfCriteriaMultiConsentDTO.getDescription())
                                .subDescription(selfCriteriaMultiConsentDTO.getSubDescription())
                                .thresholdCode(selfCriteriaMultiConsentDTO.getThresholdCode())
                                .value(selfCriteriaMultiConsentDTO.getValue())
                                .build();
                    }
                    return null;
                }).toList());

        if(beneficiaryRuleDto.getApiKeyClientId() != null && beneficiaryRuleDto.getApiKeyClientAssertion() != null) {
            beneficiaryRule.setApiKeyClientId(aesTokenService.encrypt(beneficiaryRuleDto.getApiKeyClientId()));
            beneficiaryRule.setApiKeyClientAssertion(aesTokenService.encrypt(beneficiaryRuleDto.getApiKeyClientAssertion()));
        }
        return beneficiaryRule;
    }

    public Initiative toInitiative(InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDto) {
        Initiative initiative = new Initiative();
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.valueOf(initiativeRewardAndTrxRulesDto.getInitiativeRewardType().name()));
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
                    .rewardValueType(RewardValue.RewardValueTypeEnum.valueOf(rewardValueInput.getRewardValueType().name()))
                    .rewardValue(rewardValueInput.getRewardValue())
                    .build();
        } else if (rewardRuleDTO instanceof RewardGroupsDTO rewardGroupsInput) {
            ret = RewardGroups.builder()
                    .type(rewardGroupsInput.getType())
                    .rewardGroups(rewardGroupsInput.getRewardGroups().stream().map(
                    x -> RewardGroups.RewardGroup.builder()
                            .fromCents(euroToCents(x.getFrom()))
                            .toCents(euroToCents(x.getTo()))
                            .rewardValue(x.getRewardValue()).build()
            ).toList()).build();
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
                .daysOfWeek(trxRulesDTO.getDaysOfWeek()!= null ? this.toDaysOfWeek(trxRulesDTO.getDaysOfWeek()) : null)
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
                .fromIncluded(trxCountDTO.isFromIncluded())
                .toIncluded(trxCountDTO.isToIncluded()).build();
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
                        .rewardLimitCents(euroToCents(x.getRewardLimit()))
                        .build())
                .toList();
    }

    private Threshold toThreshold(ThresholdDTO thresholdDTO) {
        if (thresholdDTO == null) {
            return null;
        }
        return Threshold.builder().fromCents(euroToCents(thresholdDTO.getFrom()))
                .toCents(euroToCents(thresholdDTO.getTo()))
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
        initiativeRefundRule.setAccumulatedAmount(toAccumulatedAmount(refundRuleDTO.getAccumulatedAmount()));
        initiativeRefundRule.setTimeParameter(toTimeParameter(refundRuleDTO.getTimeParameter()));
        initiativeRefundRule.setAdditionalInfo(toAdditionalInfo(refundRuleDTO.getAdditionalInfo()));
        return initiativeRefundRule;
    }

    private AccumulatedAmount toAccumulatedAmount(AccumulatedAmountDTO accumulatedAmountDTO){
        if(accumulatedAmountDTO == null){
            return null;
        }
        return AccumulatedAmount.builder().accumulatedType(AccumulatedAmount.AccumulatedTypeEnum.valueOf(accumulatedAmountDTO.getAccumulatedType().name()))
                .refundThresholdCents(euroToCents(accumulatedAmountDTO.getRefundThreshold())).build();
    }

    private TimeParameter toTimeParameter(TimeParameterDTO timeParameterDTO){
        if (timeParameterDTO == null || timeParameterDTO.getTimeType()==null){
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
                .creationDate(initiativeDTO.getCreationDate())
                .updateDate(initiativeDTO.getUpdateDate())
                .status(initiativeDTO.getStatus())
                .additionalInfo(initiativeAdditional)
                .general(initiativeGeneral)
                .beneficiaryRule(initiativeBeneficiaryRule)
                .rewardRule(initiativeRewardRule)
                .trxRule(initiativeTrxConditions)
                .refundRule(initiativeRefundRule)
                .initiativeRewardType(initiativeDTO.getInitiativeRewardType())
                .build();
    }
}
