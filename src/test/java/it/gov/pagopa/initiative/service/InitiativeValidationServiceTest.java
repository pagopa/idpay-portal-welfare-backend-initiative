package it.gov.pagopa.initiative.service;


import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.initiative.ranking.gracePeriod=10"
        })
@SpringJUnitConfig
@ImportAutoConfiguration(classes = {InitiativeValidationServiceImpl.class, ValidationAutoConfiguration.class})
class InitiativeValidationServiceTest {

    private static final String INITIATIVE_ID = "initiativeId";
    private static final String INITIATIVE_NAME = "initiativeName1";
    private static final String ORGANIZATION_ID = "organizationId1";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String SERVICE_ID = "serviceId";
    private static final String ANY_ROLE = "ANY_ROLE";
    private static final String ADMIN_ROLE = "admin";
    private static final String OPE_BASE_ROLE = "ope_base";
    private static final String ISEE = "ISEE";

    @Autowired
    InitiativeValidationService initiativeValidationService;

    @MockBean
    InitiativeRepository initiativeRepository;

    @Test
    void givenAdminRole_whenInitiativeStatusIsValid_thenOk() {
        Initiative step2Initiative = createStep2Initiative(false, true);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step2Initiative));
        
        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ADMIN_ROLE);

        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void whenInitiativeNotFound_then404isRaisedForInitiativeException() {
        //Instruct the Repo Mock to return Dummy Initiatives
        //Automatically doThrow InitiativeException for Optional.empty()
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.empty());

        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ANY_ROLE));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void givenOpeBaseRole_whenInitiativeStatusIsValid_thenOk() {
        Initiative step2Initiative = createStep2Initiative(false, true);
        step2Initiative.setStatus(InitiativeConstants.Status.IN_REVISION);
        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step2Initiative));
        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, OPE_BASE_ROLE);
        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);
        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1))

        clearInvocations(initiativeRepository);

        step2Initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step2Initiative));
        //Try to call the Real Service (which is using the instructed Repo)
        initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, OPE_BASE_ROLE);
        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);
        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1))

        clearInvocations(initiativeRepository);

        step2Initiative.setStatus(InitiativeConstants.Status.APPROVED);
        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step2Initiative));
        //Try to call the Real Service (which is using the instructed Repo)
        initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, OPE_BASE_ROLE);
        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);
        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1))
    }

    @Test
    void givenOpeBase_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException() {
        Initiative step2Initiative = createStep2Initiative(false, true);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step2Initiative));

        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, OPE_BASE_ROLE));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, OPE_BASE_ROLE), exception.getMessage());
    }

    @Test
    void testCheckPermissionBeforeInsert() {
        assertThrows(InitiativeException.class,
                () -> initiativeValidationService.checkPermissionBeforeInsert("ope_base"));
    }

    @Test
    void testCheckAutomatedCriteriaOrderDirectionWithRanking() {
        Initiative step3Initiative = createStep3Initiative(false, false);
        List<AutomatedCriteria> automatedCriteriaList = step3Initiative.getBeneficiaryRule().getAutomatedCriteria();
        Executable executable = () -> initiativeValidationService.checkAutomatedCriteriaOrderDirectionWithRanking(step3Initiative, automatedCriteriaList);
        assertDoesNotThrow(executable);
    }

    @Test
    void testCheckAutomatedCriteriaOrderDirectionWithRanking2() {
        Initiative step3Initiative = createStep3Initiative(false, true);
        List<AutomatedCriteria> automatedCriteriaList = step3Initiative.getBeneficiaryRule().getAutomatedCriteria();
        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeValidationService.checkAutomatedCriteriaOrderDirectionWithRanking(step3Initiative, automatedCriteriaList));
    }

    @Test
    void testCheckAutomatedCriteriaOrderDirectionWithRanking3() {
        Initiative step3Initiative = createStep3Initiative_EQ(false, true);
        List<AutomatedCriteria> automatedCriteriaList = step3Initiative.getBeneficiaryRule().getAutomatedCriteria();
        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeValidationService.checkAutomatedCriteriaOrderDirectionWithRanking(step3Initiative, automatedCriteriaList));
    }

    @Test
    void givenRankingEndDateAndStartDateNotValid_whenValidateAllWizardSteps_thenExceptionThrown() {
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO(false, true);
        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeValidationService.validateAllWizardSteps(step5InitiativeDTO));
    }

    @Test
    void givenInitiativeOk_whenValidateAllWizardSteps_thenDoNothing() {
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO(false, true);
        LocalDate startDate = step5InitiativeDTO.getGeneral().getStartDate();
        startDate = startDate.plusDays(20);
        LocalDate endDate = step5InitiativeDTO.getGeneral().getEndDate();
        endDate = endDate.plusDays(20);
        step5InitiativeDTO.getGeneral().setStartDate(startDate);
        step5InitiativeDTO.getGeneral().setEndDate(endDate);
        Executable executable = () -> initiativeValidationService.validateAllWizardSteps(step5InitiativeDTO);
        assertDoesNotThrow(executable);
    }

    /*
     * ############### Step 1 ###############
     */

    private Initiative createStep1Initiative () {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setStatus("DRAFT");
        initiative.setPdndToken("pdndToken1");
        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceIO(true);
        initiativeAdditional.setServiceId(SERVICE_ID);
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        initiativeAdditional.setDescription("Description");
        initiativeAdditional.setPrivacyLink("https://www.google.it");
        initiativeAdditional.setTcLink("https://www.google.it");
        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.EMAIL);
        channel.setContact("contact");
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        initiativeAdditional.setChannels(channels);
        return initiativeAdditional;
    }

    InitiativeDTO createStep1InitiativeDTO () {
        return InitiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName("initiativeName1")
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .pdndCheck(true)
                .pdndToken("pdndToken1")
                .additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    InitiativeAdditionalDTO createStep1InitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();
        return initiativeAdditionalDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId("serviceId");
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("Description");
        initiativeAdditionalDTO.setPrivacyLink("https://www.google.it");
        initiativeAdditionalDTO.setTcLink("https://www.google.it");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.EMAIL);
        channelDTO.setContact("contact");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

    /*
     * ############### Step 2 ###############
     */

    private Initiative createStep2Initiative (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral(beneficiaryKnown, rankingEnabled));
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(Boolean beneficiaryKnown, Boolean rankingEnabled) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneral.setBeneficiaryKnown(beneficiaryKnown);
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
        initiativeGeneral.setRankingEnabled(rankingEnabled);
        initiativeGeneral.setDescriptionMap(language);
        return initiativeGeneral;
    }

    private InitiativeDTO createStep2InitiativeDTO (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO(beneficiaryKnown, rankingEnabled));
        return initiativeDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(Boolean beneficiaryKnown, Boolean rankingEnabled) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(beneficiaryKnown);
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
        initiativeGeneralDTO.setRankingEnabled(rankingEnabled);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    /*
     * ############### Step 3 ###############
     */

    private Initiative createStep3Initiative (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        Initiative initiative = createStep2Initiative(beneficiaryKnown, rankingEnabled);
        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
        return initiative;
    }

    private Initiative createStep3Initiative_EQ (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        Initiative initiative = createStep2Initiative(beneficiaryKnown, rankingEnabled);
        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule_EQ());
        return initiative;
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
        automatedCriteria.setAuthority("INPS");
        automatedCriteria.setCode(ISEE);
        automatedCriteria.setField("true");
        automatedCriteria.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteria.setValue("value");
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteria);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRule;
    }

    private InitiativeBeneficiaryRule createInitiativeBeneficiaryRule_EQ() {
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
        automatedCriteria.setAuthority("INPS");
        automatedCriteria.setCode(ISEE);
        automatedCriteria.setField("true");
        automatedCriteria.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteria.setValue("value");
        automatedCriteria.setOrderDirection(AutomatedCriteria.OrderDirection.ASC);
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteria);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRule;
    }

    private InitiativeDTO createStep3InitiativeDTO (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO(beneficiaryKnown, rankingEnabled);
        if(!beneficiaryKnown) {
            initiativeDTO.setBeneficiaryRule(createInitiativeBeneficiaryRuleDTO());
        }
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

    @Test
    void testCheckPermissionBeforeInsert2() {
        assertThrows(InitiativeException.class,
                () -> initiativeValidationService.checkPermissionBeforeInsert("ope_base"));
    }

    /*
     * ############### Step 4 ###############
     */

    private Initiative createStep4Initiative (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        Initiative initiative = createStep3Initiative(beneficiaryKnown, rankingEnabled);
        return initiative;
    }

    private InitiativeDTO createStep4InitiativeDTO (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO(beneficiaryKnown, rankingEnabled);
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTO();
        initiativeDTO.setRewardRule(initiativeRewardAndTrxRulesDTO.getRewardRule());
        initiativeDTO.setTrxRule(initiativeRewardAndTrxRulesDTO.getTrxRule());
        return initiativeDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTO() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTO_RewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setTrxRule(createInitiativeTrxConditionsDTOValid());
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTO_RewardValueDTO(){
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .type("rewardValue")
                .build();
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOValid(){
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


    /*
     * ############### Step 5 ###############
     */

    private Initiative createStep5Initiative () {
        Initiative initiative = createStep5Initiative(false, false);
        initiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        return initiative;
    }

    private InitiativeRefundRule createRefundRuleValidWithTimeParameter(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameter_Valid());
        refundRule.setAdditionalInfo(createAdditionalInfo_Valid());
        return refundRule;
    }

    private TimeParameter createTimeParameter_Valid(){
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    private AdditionalInfo createAdditionalInfo_Valid(){
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }

    private Initiative createStep5Initiative (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        Initiative initiative = createStep4Initiative(beneficiaryKnown, rankingEnabled);
        return initiative;
    }

    private InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO(false, false);
        initiativeDTO.setRefundRule(createRefundRuleDTOValidWithTimeParameter());
        return initiativeDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameter(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();

        refundRuleDTO.setOrganizationName(ORGANIZATION_NAME);
        refundRuleDTO.setOrganizationUserRole(ADMIN_ROLE);

        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTO_Valid());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private TimeParameterDTO createTimeParameterDTO_Valid(){
        TimeParameterDTO timeParameterDTO = new TimeParameterDTO();
        timeParameterDTO.setTimeType(TimeParameterDTO.TimeTypeEnum.CLOSED);
        return timeParameterDTO;
    }

    private RefundAdditionalInfoDTO createAdditionalInfoDTOValid(){
        RefundAdditionalInfoDTO refundAdditionalInfoDTO = new RefundAdditionalInfoDTO();
        refundAdditionalInfoDTO.setIdentificationCode("B002");
        return refundAdditionalInfoDTO;
    }

    private InitiativeDTO createStep5InitiativeDTO (Boolean beneficiaryKnown, Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep4InitiativeDTO(beneficiaryKnown, rankingEnabled);
        initiativeDTO.setRefundRule(createRefundRuleDTOValidWithTimeParameter());
        return initiativeDTO;
    }
}
