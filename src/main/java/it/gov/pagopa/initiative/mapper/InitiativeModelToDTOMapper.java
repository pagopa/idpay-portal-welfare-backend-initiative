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
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.reward.RewardValue;
import it.gov.pagopa.initiative.model.rule.trx.*;
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
        initiativeDto.setGeneral(this.toInitiativeGeneralDTO(initiative.getGeneral()));
        initiativeDto.setAdditionalInfo(this.toInitiativeAdditionalDTO(initiative.getAdditionalInfo()));
        initiativeDto.setBeneficiaryRule(this.toInitiativeBeneficiaryRuleDTO(initiative.getBeneficiaryRule()));
        initiativeDto.setRewardRule(this.toRewardRuleDTO(initiative.getRewardRule()));
        initiativeDto.setTrxRule(this.toTrxRuleDTO(initiative.getTrxRule()));
        initiativeDto.setRefundRule(this.toInitiativeRefundRuleDTO(initiative.getRefundRule()));
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
                .serviceIO(additional.getServiceIO())
                .serviceId(additional.getServiceId())
                .description(additional.getDescription())
                .serviceName(additional.getServiceName())
                .serviceScope(InitiativeAdditionalDTO.ServiceScope.valueOf(additional.getServiceScope().name()))
                .privacyLink(additional.getPrivacyLink())
                .tcLink(additional.getTcLink())
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
                            .operator(FilterOperatorEnum.valueOf(x.getOperator().name()))
                            .authority(x.getAuthority())//TODO definire modalità di recupero authority
                            .value(x.getValue())
                            .value2(x.getValue2())
                            .build()
            ).toList());
        }

        if (CollectionUtils.isEmpty(beneficiaryRule.getSelfDeclarationCriteria())) {
            beneficiaryRuleDto.setSelfDeclarationCriteria(Collections.emptyList());
        } else {
            beneficiaryRuleDto.setSelfDeclarationCriteria(beneficiaryRule.getSelfDeclarationCriteria().stream()
                    .map(x -> {
                                if (x instanceof SelfCriteriaBool selfCriteriaBool) {
                                    return SelfCriteriaBoolDTO.builder()
                                            .type(it.gov.pagopa.initiative.dto.TypeBoolEnum.valueOf(selfCriteriaBool.get_type().name()))
                                            .code(selfCriteriaBool.getCode())
                                            .description(selfCriteriaBool.getDescription())
                                            .value(selfCriteriaBool.getValue())
                                            .build();
                                } else if (x instanceof SelfCriteriaMulti selfCriteriaMulti) {
                                    return SelfCriteriaMultiDTO.builder()
                                            .type(it.gov.pagopa.initiative.dto.TypeMultiEnum.valueOf(selfCriteriaMulti.get_type().name()))
                                            .code(selfCriteriaMulti.getCode())
                                            .description(selfCriteriaMulti.getDescription())
                                            .value(selfCriteriaMulti.getValue())
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

    private InitiativeRewardRuleDTO toRewardRuleDTO(InitiativeRewardRule rewardRule) {
        if (rewardRule == null) {
            return null;
        }
        InitiativeRewardRuleDTO dto = null;
        if (rewardRule instanceof RewardValue rewardValueInput) {
            dto = RewardValueDTO.builder().rewardValue(rewardValueInput.getRewardValue()).build();
        } else if (rewardRule instanceof RewardGroups rewardGroupsInput) {
            dto = RewardGroupsDTO.builder().rewardGroups(rewardGroupsInput.getRewardGroups().stream().map(
                    x -> RewardGroupsDTO.RewardGroupDTO.builder().from(x.getFrom()).to(x.getTo()).rewardValue(x.getRewardValue()).build()
            ).toList()).build();
        }
        return dto;
    }

    private InitiativeTrxConditionsDTO toTrxRuleDTO(InitiativeTrxConditions trxRules) {
        if (trxRules == null) {
            return null;
        }
        return InitiativeTrxConditionsDTO.builder()
                .daysOfWeek(this.toDaysOfWeekDTO(trxRules.getDaysOfWeek()))
                .mccFilter(this.toMccFilterDTO(trxRules.getMccFilter()))
                .rewardLimits(this.toRewardLimitsDTO(trxRules.getRewardLimits()))
                .trxCount(this.toTrxCountDTO(trxRules.getTrxCount()))
                .threshold(this.toThresholdDTO(trxRules.getThreshold()))
                .build();
    }

    private TrxCountDTO toTrxCountDTO(TrxCount trxCount) {
        if (trxCount == null) {
            return null;
        }
        return TrxCountDTO.builder().to(trxCount.getTo())
                .from(trxCount.getFrom())
                .fromIncluded(trxCount.isFromIncluded())
                .toIncluded(trxCount.isToIncluded()).build();
    }

    private MccFilterDTO toMccFilterDTO(MccFilter mccFilter) {
        if (mccFilter == null) {
            return null;
        }
        return MccFilterDTO.builder()
                .allowedList(mccFilter.isAllowedList())
                .values(mccFilter.getValues())
                .build();
    }

    private DayOfWeekDTO toDaysOfWeekDTO(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            return null;
        }
        return new DayOfWeekDTO(dayOfWeek.stream().map(x -> DayOfWeekDTO.DayConfig.builder()
                        .daysOfWeek(x.getDaysOfWeek())
                        .intervals(x.getIntervals().stream().map(i -> DayOfWeekDTO.Interval.builder()
                                        .startTime(i.getStartTime())
                                        .endTime(i.getEndTime())
                                        .build())
                                .toList())
                        .build())
                .toList());
    }

    private List<RewardLimitsDTO> toRewardLimitsDTO(List<RewardLimits> rewardLimit) {
        if (CollectionUtils.isEmpty(rewardLimit)) {
            return Collections.emptyList();
        }
        return rewardLimit.stream().map(x -> RewardLimitsDTO.builder()
                        .frequency(RewardLimitsDTO.RewardLimitFrequency.valueOf(x.getFrequency().name()))
                        .rewardLimit(x.getRewardLimit())
                        .build())
                .toList();
    }

    private ThresholdDTO toThresholdDTO(Threshold threshold) {
        if (threshold == null) {
            return null;
        }
        return ThresholdDTO.builder().from(threshold.getFrom())
                .to(threshold.getTo())
                .fromIncluded(threshold.isFromIncluded())
                .toIncluded(threshold.isToIncluded()).build();
    }

    public InitiativeRefundRuleDTO toInitiativeRefundRuleDTO(InitiativeRefundRule refundRule){
        if (refundRule == null){
            return null;
        }
        InitiativeRefundRuleDTO initiativeRefundRuleDTO = new InitiativeRefundRuleDTO();
        initiativeRefundRuleDTO.setAccumulatedAmount(toAccomulatedAmountDTO(refundRule.getAccumulatedAmount()));
        initiativeRefundRuleDTO.setTimeParameter(toTimeParameterDTO(refundRule.getTimeParameter()));
        initiativeRefundRuleDTO.setAdditionalInfo(toAdditionalInfoDTO(refundRule.getAdditionalInfo()));
        return initiativeRefundRuleDTO;
    }

    private AccumulatedAmountDTO toAccomulatedAmountDTO(AccumulatedAmount accomulatedAmount){
        if(accomulatedAmount == null){
            return null;
        }
        return AccumulatedAmountDTO.builder().accumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.valueOf(accomulatedAmount.getAccomulatedType().name()))
                .refundThreshold(accomulatedAmount.getRefundThreshold()).build();
    }

    private TimeParameterDTO toTimeParameterDTO(TimeParameter timeParameter){
        if (timeParameter == null){
            return null;
        }
        return TimeParameterDTO.builder().timeType(TimeParameterDTO.TimeTypeEnum.valueOf(timeParameter.getTimeType().name())).build();
    }

    private RefundAdditionalInfoDTO toAdditionalInfoDTO(AdditionalInfo additionalInfo){
        if (additionalInfo == null){
            return null;
        }
        return RefundAdditionalInfoDTO.builder().identificationCode(additionalInfo.getIdentificationCode()).build();
    }

}
