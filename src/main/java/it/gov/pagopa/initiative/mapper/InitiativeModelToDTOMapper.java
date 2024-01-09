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
import it.gov.pagopa.initiative.service.AESTokenService;
import it.gov.pagopa.initiative.utils.InitiativeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Component
public class InitiativeModelToDTOMapper {

    private final AESTokenService aesTokenService;

    private final InitiativeUtils initiativeUtils;

    public InitiativeModelToDTOMapper(AESTokenService aesTokenService, InitiativeUtils initiativeUtils) {
        this.aesTokenService = aesTokenService;
        this.initiativeUtils = initiativeUtils;
    }

    public InitiativeDataDTO toInitiativeDataDTO(Initiative initiative, Locale acceptLanguage) {
        if (initiative == null) {
            return null;
        }
        String description = StringUtils.EMPTY;
        String logoURL = null;
        if (initiative.getGeneral() != null && initiative.getGeneral().getDescriptionMap() != null) {
            //if no description for the given accepted language, try the default to italian
            description = StringUtils.defaultString(
                    initiative.getGeneral().getDescriptionMap().get(acceptLanguage.getLanguage()),
                    initiative.getGeneral().getDescriptionMap().get(Locale.ITALIAN.getLanguage())
            );
        }
        if(initiative.getAdditionalInfo() != null && initiative.getAdditionalInfo().getLogoFileName() != null){
            logoURL = initiativeUtils.createLogoUrl(initiative.getOrganizationId(),
                    initiative.getInitiativeId());
        }
        return InitiativeDataDTO.builder()
                .initiativeId(initiative.getInitiativeId())
                .initiativeName(initiative.getInitiativeName())
                .description(description)
                .organizationId(initiative.getOrganizationId())
                .organizationName(initiative.getOrganizationName())
                .tcLink(initiative.getAdditionalInfo().getTcLink())
                .privacyLink(initiative.getAdditionalInfo().getPrivacyLink())
                .logoURL(logoURL)
                .build();
    }

    public  InitiativeDetailDTO toInitiativeDetailDTO(Initiative initiative,Locale acceptLanguage) {
        String ruleDescription = StringUtils.EMPTY;
        String logoURL = null;
        if (initiative.getGeneral() != null && initiative.getGeneral().getDescriptionMap() != null) {
            //if no description for the given accepted language, try the default to italian
            ruleDescription = StringUtils.defaultString(
                    initiative.getGeneral().getDescriptionMap().get(acceptLanguage.getLanguage()),
                    initiative.getGeneral().getDescriptionMap().get(Locale.ITALIAN.getLanguage())
            );
        }
        if(initiative.getAdditionalInfo() != null && initiative.getAdditionalInfo().getLogoFileName() != null){
            logoURL = initiativeUtils.createLogoUrl(initiative.getOrganizationId(),
                    initiative.getInitiativeId());
        }
        return InitiativeDetailDTO.builder()
                .initiativeName(initiative.getInitiativeName())
                .status(initiative.getStatus())
                .description(initiative.getAdditionalInfo().getDescription())
                .ruleDescription(ruleDescription)
                .onboardingStartDate(initiative.getGeneral().getRankingStartDate() != null ?
                        initiative.getGeneral().getRankingStartDate() : initiative.getGeneral().getStartDate())
                .onboardingEndDate(initiative.getGeneral().getRankingEndDate() != null ?
                        initiative.getGeneral().getRankingEndDate() : initiative.getGeneral().getEndDate())
                .fruitionStartDate(initiative.getGeneral().getStartDate())
                .fruitionEndDate(initiative.getGeneral().getEndDate())
                .rewardRule(this.toRewardRuleDTOWithoutType(initiative.getRewardRule()))
                .refundRule(this.toInitiativeRefundRuleDTOWithoutAdditionalInfo((initiative.getRefundRule())))
                .privacyLink(initiative.getAdditionalInfo().getPrivacyLink())
                .tcLink(initiative.getAdditionalInfo().getTcLink())
                .logoURL(logoURL)
                .updateDate(initiative.getUpdateDate())
                .serviceId(initiative.getAdditionalInfo().getServiceId())
                .build();
    }

    public InitiativeDTO toInitiativeDTO(Initiative initiative, boolean checkEndDate) {
        if (initiative == null) {
            return null;
        }
        InitiativeDTO initiativeDto = this.toDtoOnlyId(initiative);
        initiativeDto.setInitiativeName(initiative.getInitiativeName());
        initiativeDto.setStatus(checkEndDate ? InitiativeUtils.checkEndDateToSetStatus(initiative) : initiative.getStatus());
        initiativeDto.setOrganizationId(initiative.getOrganizationId());
        initiativeDto.setOrganizationName(initiative.getOrganizationName());
        initiativeDto.setCreationDate(initiative.getCreationDate());
        initiativeDto.setUpdateDate(initiative.getUpdateDate());
        initiativeDto.setGeneral(this.toInitiativeGeneralDTO(initiative.getGeneral()));
        initiativeDto.setAdditionalInfo(this.toInitiativeAdditionalDTO(initiative.getAdditionalInfo()));
        initiativeDto.setIsLogoPresent(false);
        if(initiativeDto.getAdditionalInfo() != null && initiativeDto.getAdditionalInfo().getLogoFileName() != null){
            initiativeDto.getAdditionalInfo().setLogoURL(initiativeUtils.createLogoUrl(initiative.getOrganizationId(),
                    initiative.getInitiativeId()));
            initiativeDto.setIsLogoPresent(true);
        }
        initiativeDto.setBeneficiaryRule(this.toInitiativeBeneficiaryRuleDTO(initiative.getBeneficiaryRule()));
        initiativeDto.setInitiativeRewardType(initiative.getInitiativeRewardType());
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
                .beneficiaryType(general.getBeneficiaryType()!=null?InitiativeGeneralDTO.BeneficiaryTypeEnum.valueOf(general.getBeneficiaryType().name()):null)
                .familyUnitComposition(general.getFamilyUnitComposition()!=null?general.getFamilyUnitComposition():null)
                .budget(general.getBudget())
                .endDate(general.getEndDate())
                .startDate(general.getStartDate())
                .rankingEndDate(general.getRankingEndDate())
                .rankingStartDate(general.getRankingStartDate())
                .rankingEnabled(general.getRankingEnabled())
                .descriptionMap(general.getDescriptionMap()).build();
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
                .logoFileName(additional.getLogoFileName())
                .logoUploadDate(additional.getLogoUploadDate())
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
        beneficiaryRuleDto.setAutomatedCriteria(Optional.ofNullable(beneficiaryRule.getAutomatedCriteria())
                .orElse(Collections.emptyList())
                .stream().map(modelAC ->
                        AutomatedCriteriaDTO.builder()
                                .code(modelAC.getCode())
                                .field(modelAC.getField())
                                .operator(FilterOperatorEnum.valueOf(modelAC.getOperator().name()))
                                .authority(modelAC.getAuthority())
                                .value(modelAC.getValue())
                                .value2(modelAC.getValue2())
                                .orderDirection(modelAC.getOrderDirection() != null
                                        ? AutomatedCriteriaDTO.OrderDirection.valueOf(modelAC.getOrderDirection().name())
                                        : null)
                                .iseeTypes(modelAC.getIseeTypes())
                                .build()
                ).toList());

        beneficiaryRuleDto.setSelfDeclarationCriteria(Optional.ofNullable(beneficiaryRule.getSelfDeclarationCriteria())
                .orElse(Collections.emptyList())
                .stream().map(x -> {
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
                }).toList());
        if(beneficiaryRule.getApiKeyClientId() != null && beneficiaryRule.getApiKeyClientAssertion() != null) {
            beneficiaryRuleDto.setApiKeyClientId(aesTokenService.decrypt(beneficiaryRule.getApiKeyClientId()));
            beneficiaryRuleDto.setApiKeyClientAssertion(aesTokenService.decrypt(beneficiaryRule.getApiKeyClientAssertion()));
        }
        return beneficiaryRuleDto;
    }

    public List<InitiativeSummaryDTO> toInitiativeSummaryDTOList(List<Initiative> initiatives) {
        if (CollectionUtils.isEmpty(initiatives)) {
            return Collections.emptyList();
        }
        return initiatives.stream().map(initiativeModel -> {
                    String serviceName = initiativeModel.getAdditionalInfo() != null ?
                            initiativeModel.getAdditionalInfo().getServiceName() : StringUtils.EMPTY;
            return InitiativeSummaryDTO.builder()
                            .initiativeId(initiativeModel.getInitiativeId())
                            .initiativeName(StringUtils.isNotBlank(initiativeModel.getInitiativeName()) ?
                                    initiativeModel.getInitiativeName() : serviceName
                            )
                            .initiativeRewardType(initiativeModel.getInitiativeRewardType() != null ?
                                    initiativeModel.getInitiativeRewardType().name() : null)
                            .status(InitiativeUtils.checkEndDateToSetStatus(initiativeModel))
                            .creationDate(initiativeModel.getCreationDate())
                            .updateDate(initiativeModel.getUpdateDate())
                            .rankingEnabled(initiativeModel.getGeneral() != null ? initiativeModel.getGeneral().getRankingEnabled() : null)
                            .build();
        }).toList();
    }

    public List<InitiativeIssuerDTO> toInitiativeIssuerDTOList(List<Initiative> initiatives) {
        return Optional.ofNullable(initiatives)
                .orElse(Collections.emptyList())
                .stream().map(initiativeModel -> {
                    InitiativeIssuerDTO initiativeIssuerDTO = new InitiativeIssuerDTO();
                    initiativeIssuerDTO.setInitiativeId(initiativeModel.getInitiativeId());
                    String serviceName = initiativeModel.getAdditionalInfo() != null ? initiativeModel.getAdditionalInfo().getServiceName() : StringUtils.EMPTY;
                    initiativeIssuerDTO.setInitiativeName(StringUtils.isNotBlank(initiativeModel.getInitiativeName()) ?
                            initiativeModel.getInitiativeName() : serviceName);
                    initiativeIssuerDTO.setOrganizationName(initiativeModel.getOrganizationName());
                    initiativeIssuerDTO.setStatus(initiativeModel.getStatus());
                    if(initiativeModel.getAdditionalInfo() != null){
                        initiativeIssuerDTO.setTcLink(initiativeModel.getAdditionalInfo().getTcLink());
                        initiativeIssuerDTO.setPrivacyLink(initiativeModel.getAdditionalInfo().getPrivacyLink());
                        initiativeIssuerDTO.setLogoURL(initiativeModel.getAdditionalInfo().getLogoFileName() != null
                                ? initiativeUtils.createLogoUrl(initiativeModel.getOrganizationId(), initiativeModel.getInitiativeId()) : null);
                    }
                    if(initiativeModel.getGeneral() != null){
                        initiativeIssuerDTO.setDescriptionMap(initiativeModel.getGeneral().getDescriptionMap() != null ?
                                this.languageMap(initiativeModel.getGeneral().getDescriptionMap()) : null);
                        initiativeIssuerDTO.setStartDate(initiativeModel.getGeneral().getStartDate());
                        initiativeIssuerDTO.setEndDate(initiativeModel.getGeneral().getEndDate());
                        initiativeIssuerDTO.setRankingEnabled(initiativeModel.getGeneral().getRankingEnabled());
                        initiativeIssuerDTO.setRankingStartDate(initiativeModel.getGeneral().getRankingStartDate());
                        initiativeIssuerDTO.setRankingEndDate(initiativeModel.getGeneral().getRankingEndDate());
                        initiativeIssuerDTO.setBeneficiaryKnown(initiativeModel.getGeneral().getBeneficiaryKnown());
                    }
                    return initiativeIssuerDTO;
                }).toList();
    }

    public List<InitiativeMilDTO> toInitiativeListMilDTO(List<Initiative> initiatives) {
        return Optional.ofNullable(initiatives)
                .orElse(Collections.emptyList())
                .stream().map(initiativeModel -> {
                    InitiativeMilDTO initiativeMilDTO = new InitiativeMilDTO();
                    String serviceName = StringUtils.EMPTY;

                    if(initiativeModel.getAdditionalInfo() != null) {
                        initiativeMilDTO.setTcLink(initiativeModel.getAdditionalInfo().getTcLink());
                        initiativeMilDTO.setPrivacyLink(initiativeModel.getAdditionalInfo().getPrivacyLink());
                        initiativeMilDTO.setLogoURL(initiativeModel.getAdditionalInfo().getLogoFileName() != null
                                ? initiativeUtils.createLogoUrl(initiativeModel.getOrganizationId(), initiativeModel.getInitiativeId()) : null);
                        serviceName = initiativeModel.getAdditionalInfo().getServiceName();
                    }
                    if(initiativeModel.getGeneral() != null) {
                        initiativeMilDTO.setBeneficiaryType(initiativeModel.getGeneral().getBeneficiaryType());
                        initiativeMilDTO.setFruitionStartDate(initiativeModel.getGeneral().getStartDate());
                        initiativeMilDTO.setFruitionEndDate(initiativeModel.getGeneral().getEndDate());
                        initiativeMilDTO.setRankingEnabled(initiativeModel.getGeneral().getRankingEnabled());
                        initiativeMilDTO.setOnboardingStartDate(initiativeModel.getGeneral().getRankingStartDate());
                        initiativeMilDTO.setOnboardingEndDate(initiativeModel.getGeneral().getRankingEndDate());
                        initiativeMilDTO.setBeneficiaryKnown(initiativeModel.getGeneral().getBeneficiaryKnown());
                    }
                    initiativeMilDTO.setInitiativeId(initiativeModel.getInitiativeId());
                    initiativeMilDTO.setInitiativeName(StringUtils.isNotBlank(initiativeModel.getInitiativeName()) ?
                            initiativeModel.getInitiativeName() : serviceName);
                    initiativeMilDTO.setOrganizationId(initiativeModel.getOrganizationId());
                    initiativeMilDTO.setOrganizationName(initiativeModel.getOrganizationName());
                    initiativeMilDTO.setInitiativeRewardType(initiativeModel.getInitiativeRewardType());
                    return initiativeMilDTO;
                }).toList();
    }

    private Map<String,String> languageMap(Map<String,String> map){
        Map<String, String> descriptionItaEng = new HashMap<>();
        descriptionItaEng.put(Locale.ITALIAN.getLanguage(),
                map.get(map.get(Locale.ITALIAN.getLanguage())));
        if (map.containsKey(Locale.ENGLISH.getLanguage())) {
            descriptionItaEng.put(Locale.ENGLISH.getLanguage(),
                    map.get(map.get(Locale.ENGLISH.getLanguage())));
        }
        return descriptionItaEng;
    }

    private InitiativeRewardRuleDTO toRewardRuleDTO(InitiativeRewardRule rewardRule) {
        if (rewardRule == null) {
            return null;
        }
        InitiativeRewardRuleDTO dto = null;
        if (rewardRule instanceof RewardValue rewardValueInput) {
            dto = RewardValueDTO.builder()
                    .type(rewardValueInput.getType())
                    .rewardValueType(RewardValueDTO.RewardValueTypeEnum.valueOf(rewardValueInput.getRewardValueType().name()))
                    .rewardValue(rewardValueInput.getRewardValue())
                    .build();
        } else if (rewardRule instanceof RewardGroups rewardGroupsInput) {
            dto = RewardGroupsDTO.builder()
                    .type(rewardGroupsInput.getType())
                    .rewardGroups(rewardGroupsInput.getRewardGroups().stream().map(
                    x -> RewardGroupsDTO.RewardGroupDTO.builder().from(x.getFrom()).to(x.getTo()).rewardValue(x.getRewardValue()).build()
            ).toList())
                    .build();
        }
        return dto;
    }

    private InitiativeRewardRuleDTO toRewardRuleDTOWithoutType(InitiativeRewardRule rewardRule) {
        if (rewardRule == null) {
            return null;
        }
        InitiativeRewardRuleDTO dto = null;
        if (rewardRule instanceof RewardValue rewardValueInput) {
            dto = RewardValueDTO.builder()
                    .rewardValueType(RewardValueDTO.RewardValueTypeEnum.valueOf(rewardValueInput.getRewardValueType().name()))
                    .rewardValue(rewardValueInput.getRewardValue())
                    .build();
        } else if (rewardRule instanceof RewardGroups rewardGroupsInput) {
            dto = RewardGroupsDTO.builder()
                    .rewardGroups(rewardGroupsInput.getRewardGroups().stream().map(
                            x -> RewardGroupsDTO.RewardGroupDTO.builder().from(x.getFrom()).to(x.getTo()).rewardValue(x.getRewardValue()).build()
                    ).toList())
                    .build();
        }
        return dto;
    }

    private InitiativeTrxConditionsDTO toTrxRuleDTO(InitiativeTrxConditions trxRules) {
        if (trxRules == null) {
            return null;
        }
        return InitiativeTrxConditionsDTO.builder()
                .daysOfWeek(trxRules.getDaysOfWeek() != null ? this.toDaysOfWeekDTO(trxRules.getDaysOfWeek()) : null)
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
        initiativeRefundRuleDTO.setAccumulatedAmount(toAccumulatedAmountDTO(refundRule.getAccumulatedAmount()));
        initiativeRefundRuleDTO.setTimeParameter(toTimeParameterDTO(refundRule.getTimeParameter()));
        initiativeRefundRuleDTO.setAdditionalInfo(toAdditionalInfoDTO(refundRule.getAdditionalInfo()));
        return initiativeRefundRuleDTO;
    }
    public InitiativeRefundRuleDTO toInitiativeRefundRuleDTOWithoutAdditionalInfo(InitiativeRefundRule refundRule){
        if (refundRule == null){
            return null;
        }
        InitiativeRefundRuleDTO initiativeRefundRuleDTO = new InitiativeRefundRuleDTO();
        initiativeRefundRuleDTO.setAccumulatedAmount(toAccumulatedAmountDTO(refundRule.getAccumulatedAmount()));
        initiativeRefundRuleDTO.setTimeParameter(toTimeParameterDTO(refundRule.getTimeParameter()));
        return initiativeRefundRuleDTO;
    }

    private AccumulatedAmountDTO toAccumulatedAmountDTO(AccumulatedAmount accumulatedAmount){
        if(accumulatedAmount == null){
            return null;
        }
        return AccumulatedAmountDTO.builder().accumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.valueOf(accumulatedAmount.getAccumulatedType().name()))
                .refundThreshold(accumulatedAmount.getRefundThreshold()).build();
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
