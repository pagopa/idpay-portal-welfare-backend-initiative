package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(value = {
        InitiativeDTOsToModelMapper.class})
@Slf4j
class InitiativeDTOsToModelMapperTest {
    @Autowired
    InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    private Initiative initiativeOnlyInfoGeneral;
    private Initiative initiativeNoBaseFields;
    private InitiativeBeneficiaryRule initiativeBeneficiaryRule;
    private InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO;
    private InitiativeAdditionalDTO initiativeInfoDTOnoBaseFields;
    private InitiativeGeneralDTO initiativeInfoOnlyInfoGeneralDTO;

    private InitiativeRefundRuleDTO initiativeRefundRuleDTOAmount;
    private Initiative initiativeOnlyRefundRule;

    private InitiativeRefundRuleDTO initiativeRefundRuleDTOTimeParameter;
    private Initiative initiativeOnlyRefundRule2;

    private InitiativeRefundRuleDTO initiativeRefundRuleDTOAdditionalNull;

    private Initiative initiativeOnlyRefundRule3;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTOcomplete;

    private Initiative initiativeOnlyRewardAndTrxRules;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTORewardRuleNull;

    private Initiative initiativeOnlyRewardAndTrxRulesRewardRuleNull;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTORewardGroup;

    private Initiative initiativeOnlyRewardAndTrxRulesRewardGroup;



    @BeforeEach
    public void setUp() {
        initiativeOnlyInfoGeneral = createStep1InitiativeOnlyInfoGeneral();
        initiativeNoBaseFields = createStep1InitiativeNoBaseFields();
        initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiativeInfoOnlyInfoGeneralDTO = createStep1InitiativeInfoDTOonlyInfoGeneral();
        initiativeInfoDTOnoBaseFields = createStep1InitiativeInfoDTOnoBaseFields();
        initiativeRefundRuleDTOAmount = createRefundRuleDTOValidWithAccumulatedAmount();
        initiativeOnlyRefundRule = createInitiativeOnlyRefundRule();
        initiativeRefundRuleDTOTimeParameter = createRefundRuleDTOValidWithTimeParameter();
        initiativeOnlyRefundRule2 = createInitiativeOnlyRefundRule2();
        initiativeRefundRuleDTOAdditionalNull = createRefundRuleDTOValidWithTimeParameterAndAdditionalNull();
        initiativeOnlyRefundRule3 = createInitiativeOnlyRefundRule3();
        initiativeRewardAndTrxRulesDTOcomplete = createInitiativeRewardAndTrxRulesDTO();
        initiativeOnlyRewardAndTrxRules = createInitiativeOnlyRewardAndTrxRules();
        initiativeRewardAndTrxRulesDTORewardRuleNull = createInitiativeRewardAndTrxRulesDTORewardRuleNull();
        initiativeOnlyRewardAndTrxRulesRewardRuleNull = createInitiativeOnlyRewardAndTrxRulesRewardRuleNull();
        initiativeRewardAndTrxRulesDTORewardGroup = createInitiativeRewardAndTrxRulesDTORewardGroup();
        initiativeOnlyRewardAndTrxRulesRewardGroup = createInitiativeOnlyRewardAndTrxRulesRewardGroup();
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesRewardGroups_equals(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTORewardGroup);
        assertEquals(initiativeOnlyRewardAndTrxRulesRewardGroup, initiative);
    }
    @Test
    void toInitiativeOnlyRewardAndTrxRules_equals(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTOcomplete);
        assertEquals(initiativeOnlyRewardAndTrxRules, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesNull_equals(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative((InitiativeAdditionalDTO) null);
        assertEquals(null, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesRewardRuleNull_equals(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTORewardRuleNull);
        assertEquals(initiativeOnlyRewardAndTrxRulesRewardRuleNull, initiative);
    }

    @Test
    void toInitiativeOnlyInfoGeneral_ok(){
        Initiative initiativeActual = initiativeDTOsToModelMapper.toInitiative(initiativeInfoOnlyInfoGeneralDTO);
        //Check the equality of the results
        assertEquals(initiativeOnlyInfoGeneral, initiativeActual);
    }

    @Test
    void toInitiativeNoBaseFields_ok(){
        Initiative initiativeActual = initiativeDTOsToModelMapper.toInitiative(initiativeInfoDTOnoBaseFields);
        //Check the equality of the results
        assertEquals(initiativeNoBaseFields, initiativeActual);
    }

    @Test
    void toBeneficiaryRule_ok(){
        InitiativeBeneficiaryRule initiativeBeneficiaryRuleActual = initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        //Check the equality of the results
        assertEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRuleActual);
    }

    @Test
    void toInitiativeOnlyRefundRule_ok(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTOAmount);
        assertEquals(initiativeOnlyRefundRule, initiative);
    }

    @Test
    void toInitiativeOnlyRefundRule2_ok(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTOTimeParameter);
        assertEquals(initiativeOnlyRefundRule2, initiative);
    }

    @Test
    void toInitiativeOnlyRefundRule3_ok(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTOAdditionalNull);
        assertEquals(initiativeOnlyRefundRule3, initiative);
    }

    private Initiative createFullInitiative () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        Initiative initiative = createStep2Initiative();
        return initiative;
    }

    private InitiativeDTO createFullInitiativeDTO () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        return initiativeDTO;
    }

    private Initiative createInitiativeBaseFields(Initiative initiative) {
        initiative.setInitiativeId("Id1");
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId("organizationId1");
        initiative.setStatus("DRAFT");
        initiative.setAutocertificationCheck(true);
        initiative.setBeneficiaryRanking(true);
        initiative.setPdndCheck(true);
        initiative.setPdndToken("pdndToken1");
        return initiative;
    }

    private Initiative createStep1Initiative () {
        Initiative initiative = new Initiative();
        initiative = createInitiativeBaseFields(initiative);
        initiative.setAdditionalInfo(createInitiativeAdditional());
//        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
//        initiative.setLegal(createInitiativeLegal());
        return initiative;
    }

    private Initiative createStep1InitiativeOnlyInfoGeneral () {
        Initiative initiative = new Initiative();
//        initiative = createInitiativeBaseFields(initiative);
        initiative.setGeneral(createInitiativeGeneral());
        return initiative;
    }

    private Initiative createStep1InitiativeNoBaseFields () {
        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(createInitiativeAdditional());
        if(initiative.getAdditionalInfo() != null && initiative.getAdditionalInfo().getServiceName() != null)
            initiative.setInitiativeName(initiative.getAdditionalInfo().getServiceName());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral() {
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        initiativeGeneral.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneral.setRankingStartDate(rankingStartDate);
        initiativeGeneral.setRankingEndDate(rankingEndDate);
        initiativeGeneral.setStartDate(startDate);
        initiativeGeneral.setEndDate(endDate);
        return initiativeGeneral;
    }

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceIO(true);
        initiativeAdditional.setServiceId("serviceId");
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        initiativeAdditional.setDescription("Description");
        initiativeAdditional.setPrivacyLink("privacyLink");
        initiativeAdditional.setTcLink("tcLink");
        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.EMAIL);
        channel.setContact("contact");
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        initiativeAdditional.setChannels(channels);
        return initiativeAdditional;
    }

    private InitiativeBeneficiaryRule createInitiativeBeneficiaryRule() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        SelfCriteriaBool selfCriteriaBool = new SelfCriteriaBool();
        selfCriteriaBool.set_type(TypeBoolEnum.BOOLEAN);
        selfCriteriaBool.setCode("B001");
        selfCriteriaBool.setDescription("Desc_bool");
        selfCriteriaBool.setValue(true);
        SelfCriteriaMulti selfCriteriaMulti = new SelfCriteriaMulti();
        selfCriteriaMulti.set_type(TypeMultiEnum.MULTI);
        selfCriteriaMulti.setCode("B001");
        selfCriteriaMulti.setDescription("Desc_Multi");
        List<String> values = new ArrayList<>();
        values.add("valore1");
        values.add("valore2");
        selfCriteriaMulti.setValue(values);
        List<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        iSelfDeclarationCriteriaList.add(selfCriteriaBool);
        iSelfDeclarationCriteriaList.add(selfCriteriaMulti);
        initiativeBeneficiaryRule.setSelfDeclarationCriteria(iSelfDeclarationCriteriaList);
        AutomatedCriteria automatedCriteria = new AutomatedCriteria();
        automatedCriteria.setAuthority("Authority_ISEE");
        automatedCriteria.setCode("Code_ISEE");
        automatedCriteria.setField("true");
        automatedCriteria.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteria.setValue("value");
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteria);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRule;
    }

    private InitiativeDTO createStep1InitiativeDTO () {
        return InitiativeDTO.builder()
                .initiativeId("Id1")
                .initiativeName("initiativeName1")
                .organizationId("organizationId1")
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .pdndCheck(true)
                .pdndToken("pdndToken1")
                .additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    private InitiativeAdditionalDTO createStep1InitiativeInfoDTOnoBaseFields() {
        return createInitiativeAdditionalDTO();
    }

    private InitiativeGeneralDTO createStep1InitiativeInfoDTOonlyInfoGeneral() {
        return createInitiativeGeneralDTO();
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId("serviceId");
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("Description");
        initiativeAdditionalDTO.setPrivacyLink("privacyLink");
        initiativeAdditionalDTO.setTcLink("tcLink");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.EMAIL);
        channelDTO.setContact("contact");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

    private Initiative createStep2Initiative () {
        Initiative initiative = createStep1Initiative();
        InitiativeGeneral initiativeGeneral = createInitiativeGeneral();
        initiative.setGeneral(initiativeGeneral);
        return initiative;
    }

    private InitiativeDTO createStep2InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO();
        initiativeDTO.setGeneral(initiativeGeneralDTO);
        return initiativeDTO;
    }

    private InitiativeBeneficiaryRuleDTO createInitiativeBeneficiaryRuleDTO() {
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();
        SelfCriteriaBoolDTO selfCriteriaBoolDTO = new SelfCriteriaBoolDTO();
        selfCriteriaBoolDTO.setType(it.gov.pagopa.initiative.dto.TypeBoolEnum.BOOLEAN);
        selfCriteriaBoolDTO.setCode("B001");
        selfCriteriaBoolDTO.setDescription("Desc_bool");
        selfCriteriaBoolDTO.setValue(true);
        SelfCriteriaMultiDTO selfCriteriaMultiDTO = new SelfCriteriaMultiDTO();
        selfCriteriaMultiDTO.setType(it.gov.pagopa.initiative.dto.TypeMultiEnum.MULTI);
        selfCriteriaMultiDTO.setCode("B001");
        selfCriteriaMultiDTO.setDescription("Desc_Multi");
        List<String> values = new ArrayList<>();
        values.add("valore1");
        values.add("valore2");
        selfCriteriaMultiDTO.setValue(values);
        List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems = new ArrayList<>();
        anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems.add(selfCriteriaBoolDTO);
        anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems.add(selfCriteriaMultiDTO);
        initiativeBeneficiaryRuleDTO.setSelfDeclarationCriteria(anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems);
        AutomatedCriteriaDTO automatedCriteriaDTO = new AutomatedCriteriaDTO();
        automatedCriteriaDTO.setAuthority("Authority_ISEE");
        automatedCriteriaDTO.setCode("Code_ISEE");
        automatedCriteriaDTO.setField("true");
        automatedCriteriaDTO.setOperator(FilterOperatorEnum.EQ);
        automatedCriteriaDTO.setValue("value");
        List<AutomatedCriteriaDTO> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaDTO);
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRuleDTO;
    }

    private Initiative createStep3Initiative () {
        Initiative initiative = createStep2Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiative.setBeneficiaryRule(initiativeBeneficiaryRule);
        return initiative;
    }

    private InitiativeDTO createStep3InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeDTO.setBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        return initiativeDTO;
    }



    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO(){
        return new RewardValueDTO(BigDecimal.valueOf(50));
    }

    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupDTO(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        List<RewardGroupsDTO.RewardGroupDTO> list = new ArrayList<RewardGroupsDTO.RewardGroupDTO>();
        RewardGroupsDTO.RewardGroupDTO groupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        list.add(groupDTO1);
        rewardGroupsDTO.setRewardGroups(list);
        return rewardGroupsDTO;
    }

    InitiativeTrxConditionsDTO creatInitiativeTrxConditionsDTOValid(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<DayOfWeekDTO.DayConfig>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<DayOfWeekDTO.Interval>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<String>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<RewardLimitsDTO>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTO(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(initiativeRewardRuleDTO);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOValid();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTORewardGroup(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(initiativeRewardRuleDTO);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOValid();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTORewardRuleNull(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(null);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOValid();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeDTO createStep4InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setTrxRule(creatInitiativeTrxConditionsDTOValid());
        return initiativeDTO;
    }

    InitiativeRewardRule createInitiativeRewardRuleRewardValue(){
        return new RewardValue(BigDecimal.valueOf(50));
    }

    InitiativeRewardRule createInitiativeRewardRuleRewardGroup(){
        RewardGroups rewardGroups = new RewardGroups();
        List<RewardGroups.RewardGroup> list = new ArrayList<RewardGroups.RewardGroup>();
        RewardGroups.RewardGroup group1 = new RewardGroups.RewardGroup(BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        list.add(group1);
        rewardGroups.setRewardGroups(list);
        return rewardGroups;
    }

    InitiativeTrxConditions creatInitiativeTrxConditionsValid(){
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig> dayConfigs = new ArrayList<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig dayConfig1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval> intervals = new ArrayList<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval interval1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek dayOfWeek = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek(dayConfigs);

        Threshold threshold = new Threshold();

        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);

        TrxCount trxCount = new TrxCount();

        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        MccFilter mccFilter = new MccFilter();
        mccFilter.setAllowedList(true);
        Set<String> values = new HashSet<String>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<RewardLimits>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }
    private Initiative createStep4Initiative () {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = creatInitiativeTrxConditionsValid();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRulesRewardRuleNull(){
        Initiative initiative = new Initiative();
        initiative.setRewardRule(null);
        initiative.setTrxRule(creatInitiativeTrxConditionsValid());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRules(){
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setTrxRule(creatInitiativeTrxConditionsValid());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRulesRewardGroup(){
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardGroup());
        initiative.setTrxRule(creatInitiativeTrxConditionsValid());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardRule(){
        Initiative initiative =new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        return initiative;
    }

    private Initiative createInitiativeOnlyTrxConditions(){
        Initiative initiative = new Initiative();
        initiative.setTrxRule(creatInitiativeTrxConditionsValid());
        return initiative;
    }
    private Initiative createStep5Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    private InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    private Initiative createStep6Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    private InitiativeDTO createStep6InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }





    private AccumulatedAmountDTO createAccumulatedAmountDTOValid(){
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
    }

    private TimeParameterDTO createTimeParameterDTOValid(){
        TimeParameterDTO timeParameterDTO = new TimeParameterDTO();
        timeParameterDTO.setTimeType(TimeParameterDTO.TimeTypeEnum.CLOSED);
        return timeParameterDTO;
    }

    private RefundAdditionalInfoDTO createAdditionalInfoDTOValid(){
        RefundAdditionalInfoDTO refundAdditionalInfoDTO = new RefundAdditionalInfoDTO();
        refundAdditionalInfoDTO.setIdentificationCode("B002");
        return refundAdditionalInfoDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameter(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTOValid());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameterAndAdditionalNull(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTOValid());
        refundRuleDTO.setAdditionalInfo(null);
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithAccumulatedAmount(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTOValid());
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }



    private AccumulatedAmount createAccumulatedAmountValid(){
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccomulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThreshold(BigDecimal.valueOf(100000));
        return amount;
    }

    private TimeParameter createTimeParameterValid(){
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    private AdditionalInfo createAdditionalInfoValid(){
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }


    private InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmountValid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }
    private InitiativeRefundRule createRefundRuleValidWithTimeParameter(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameterValid());
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }

    private InitiativeRefundRule createRefundRuleValidWithTimeParameterAndAdditionalNull(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameterValid());
        refundRule.setAdditionalInfo(null);
        return refundRule;
    }
    private Initiative createInitiativeOnlyRefundRule(){
        Initiative initiative = new Initiative();
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }

    private Initiative createInitiativeOnlyRefundRule2(){
        Initiative initiative = new Initiative();
        initiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        return initiative;
    }

    private Initiative createInitiativeOnlyRefundRule3(){
        Initiative initiative = new Initiative();
        initiative.setRefundRule(createRefundRuleValidWithTimeParameterAndAdditionalNull());
        return initiative;
    }
}
