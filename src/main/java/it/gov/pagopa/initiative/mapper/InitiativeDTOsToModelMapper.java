package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InitiativeDTOsToModelMapper {

    public Initiative toInitiativeTrxAndRewardRules(InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO){
        if (initiativeRewardAndTrxRulesDTO == null){
            return null;
        }

        Initiative initiative = new Initiative();
        //initiative.setRewardRule();
        initiative.setTrxRule(this.toInitiativeTrxRule(initiativeRewardAndTrxRulesDTO.getTrxRule()));
        return initiative;
    }

    private InitiativeTrxRule toInitiativeTrxRule(InitiativeTrxRulesDTO trxRulesDTO){
        if (trxRulesDTO == null){
            return null;
        }
        InitiativeTrxRule initiativeTrxRule = new InitiativeTrxRule();
        initiativeTrxRule.setTrxCount(this.toTrxCount(trxRulesDTO.getTrxCount()));
        initiativeTrxRule.setMccFilter(this.toMccFilter(trxRulesDTO.getMccFilter()));
        initiativeTrxRule.setDaysOfWeekAllowed(this.toDaysOfWeekAllowedItem(trxRulesDTO.getDaysOfWeekAllowed()));
        initiativeTrxRule.setRewardLimit(this.toRewardLimit(trxRulesDTO.getRewardLimit()));
        initiativeTrxRule.setThreshold(this.toThreshold(trxRulesDTO.getThreshold()));
        return initiativeTrxRule;

    }
    private TrxCount toTrxCount(TrxCountDTO trxCountDTO){
        if (trxCountDTO == null){
            return null;
        }
        return TrxCount.builder().to(trxCountDTO.getTo())
                .from(trxCountDTO.getFrom())
                .fromIncluded(trxCountDTO.getFromIncluded())
                .toIncluded(trxCountDTO.getToIncluded()).build();
    }

    private MccFilter toMccFilter(MccFilterDTO mccFilterDTO){
        if (mccFilterDTO == null){
            return null;
        }
        return MccFilter.builder().value(mccFilterDTO.getValue())
                .filterOperator(MccFilter.FilterOperatorEnum.valueOf(mccFilterDTO.getFilterOperator().name())).build();
    }
    private List<DaysOfWeekAllowedItem> toDaysOfWeekAllowedItem(List<DaysOfWeekAllowedItemDTO> daysOfWeekAllowedItemDTO){
        if (CollectionUtils.isEmpty(daysOfWeekAllowedItemDTO)) {
            return Collections.emptyList();
        }
        else {
            List<DaysOfWeekAllowedItem> daysOfWeekAllowedItems = new ArrayList<>();
            BeanUtils.copyProperties(daysOfWeekAllowedItemDTO, daysOfWeekAllowedItems);
            return daysOfWeekAllowedItems;
//            return daysOfWeekAllowedItemDTO.stream().map(dto -> {
//                BeanUtils.copyProperties();
//                List<DaysOfWeekDTO> daysOfWeekDTOS = new ArrayList<>();
//                return daysOfWeekDTOS.stream().map(dow -> DaysOfWeek.builder().days(dto.getDaysOfWeek().))
//            })
        }
    }

    private List<RewardLimit> toRewardLimit(List<RewardLimitDTO> rewardLimitDTO){
        if (CollectionUtils.isEmpty(rewardLimitDTO)) {
            return Collections.emptyList();
        }
        else {
            List<RewardLimit> rewardLimits = new ArrayList<>();
            BeanUtils.copyProperties(rewardLimitDTO, rewardLimits);
            return rewardLimits;
//            return daysOfWeekAllowedItemDTO.stream().map(dto -> {
//                BeanUtils.copyProperties();
//                List<DaysOfWeekDTO> daysOfWeekDTOS = new ArrayList<>();
//                return daysOfWeekDTOS.stream().map(dow -> DaysOfWeek.builder().days(dto.getDaysOfWeek().))
//            })
        }
    }

    private Threshold toThreshold(ThresholdDTO thresholdDTO){
        if (thresholdDTO == null){
            return null;
        }
        return Threshold.builder().from(thresholdDTO.getFrom())
                .to(thresholdDTO.getTo())
                .fromIncluded(thresholdDTO.getFromIncluded())
                .toIncluded(thresholdDTO.getToIncluded()).build();
    }


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
