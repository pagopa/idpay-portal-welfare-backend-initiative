package it.gov.pagopa.initiative.service;


import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.event.InitiativeProducer;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.trx.*;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebMvcTest(value = {
        InitiativeService.class})
@Slf4j
class InitiativeServiceTest {

    public static final String INITIATIVE_NAME = "initiativeName1";
    public static final String ORGANIZATION_ID = "organizationId1";
    public static final String INITIATIVE_ID = "Id1";

    @Autowired
    InitiativeService initiativeService;

    @MockBean
    InitiativeRepository initiativeRepository;

    @MockBean
    InitiativeProducer initiativeProducer;

    @MockBean
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @Test
    void retrieveInitiativeSummary_ok() throws Exception {
        Initiative step2Initiative1 = createStep2Initiative();
        Initiative step2Initiative2 = createStep2Initiative();
        List<Initiative> initiativeList = Arrays.asList(step2Initiative1, step2Initiative2);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(anyString())).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(anyString());

        //Check the equality of the results
        assertEquals(initiativeList, initiatives);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(anyString()); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void retrieveInitiativeSummary_ko() {
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(anyString());
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void insertInitiative_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.insert(any(Initiative.class))).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.insertInitiative(step2Initiative);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).insert(any(Initiative.class));
    }

    @Test
    void getInitiative_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiative(anyString(), anyString());

        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeId(anyString(), anyString()); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void getInitiative_ko() throws Exception {
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            Initiative initiative = initiativeService.getInitiative(anyString(), anyString());
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void getInitiativeBeneficiaryView_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeBeneficiaryView(anyString())).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiativeBeneficiaryView(anyString());

        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeBeneficiaryView(anyString()); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void getInitiativeBeneficiaryView_ko() throws Exception {
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            Initiative initiative = initiativeService.getInitiativeBeneficiaryView(anyString());
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeGeneralInfo_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeGeneralInfo("Ente1", INITIATIVE_ID, step2Initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeId(anyString(), anyString());
    }

    @Test
    void updateInitiativeGeneralInfo_ko() throws Exception {
        Initiative fullInitiative = createStep2Initiative();
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeGeneralInfo("Ente1", INITIATIVE_ID, fullInitiative);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeAdditionalInfo_ok() throws Exception {
        Initiative step2Initiative = createStep1Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeAdditionalInfo("Ente1", INITIATIVE_ID, step2Initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeId(anyString(), anyString());
    }

    @Test
    void updateInitiativeAdditionalInfo_ko() throws Exception {
        Initiative fullInitiative = createStep1Initiative();
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeAdditionalInfo("Ente1", INITIATIVE_ID, fullInitiative);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeBeneficiary_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeBeneficiary("Ente1", INITIATIVE_ID, initiativeBeneficiaryRule);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeId(anyString(), anyString());
    }

    @Test
    void updateInitiativeBeneficiary_ko() throws Exception {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeBeneficiary("Ente1", INITIATIVE_ID, initiativeBeneficiaryRule);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateInitiativeRewardAndTrxRules_ok() throws Exception {
        Initiative step3Initiative = createStep3Initiative();

        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(step3Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateTrxAndRewardRules("Ente1", INITIATIVE_ID, initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeId(anyString(), anyString());
    }

    @Test
    void updateInitiativeRewardAndTrxRules_ko() throws Exception {
        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateTrxAndRewardRules("Ente1", INITIATIVE_ID, initiative);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateRefundRules_ok(){
        Initiative initiative = createInitiativeOnlyRefundRule();
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(initiative));
        initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, false);
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeId(anyString(), anyString());
    }

    @Test
    void updateRefundRules_ko(){
        Initiative initiative = createInitiativeOnlyRefundRule();
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        try{
            initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, false);
        }catch (InitiativeException e){
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateRefundRule_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException(){
        Initiative initiative = Initiative.builder().initiativeId(INITIATIVE_ID).status(InitiativeConstants.Status.APPROVED).build();
        Initiative initiativeNotProcessable = Initiative.builder().initiativeId(INITIATIVE_ID).status(InitiativeConstants.Status.APPROVED).build();

        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(initiativeNotProcessable));

        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, true));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, INITIATIVE_ID), exception.getMessage());
    }

    Initiative createFullInitiative () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        Initiative initiative = createStep2Initiative();
        return initiative;
    }

    InitiativeDTO createFullInitiativeDTO () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        return initiativeDTO;
    }

    /*
     * Step 1
     */

    Initiative createStep1Initiative () {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setInitiativeName(INITIATIVE_NAME);
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setStatus("DRAFT");
        initiative.setAutocertificationCheck(true);
        initiative.setBeneficiaryRanking(true);
        initiative.setPdndCheck(true);
        initiative.setPdndToken("pdndToken1");
        initiative.setAdditionalInfo(createInitiativeAdditional());
//        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
//        initiative.setLegal(createInitiativeLegal());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral() {
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        initiativeGeneral.setBudget(new BigDecimal(1000000000));
        initiativeGeneral.setEndDate(LocalDate.of(2022, 9, 8));
        initiativeGeneral.setStartDate(LocalDate.of(2022, 8, 8));
        initiativeGeneral.setRankingStartDate(LocalDate.of(2022, 9, 18));
        initiativeGeneral.setRankingEndDate(LocalDate.of(2022, 8, 18));
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

    InitiativeDTO createStep1InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        initiativeDTO = initiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName(INITIATIVE_NAME)
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .pdndCheck(true)
                .pdndToken("pdndToken1")
                .additionalInfo(createInitiativeAdditionalDTO()).build();
        return initiativeDTO;
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

    /*
     * Step 2
     */

    Initiative createStep2Initiative () {
        Initiative initiative = createStep1Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiative.setBeneficiaryRule(initiativeBeneficiaryRule);
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

    InitiativeDTO createStep2InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeDTO.setBeneficiaryRule(initiativeBeneficiaryRuleDTO);
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

    /*
    * Step 3
     */

    Initiative createStep3Initiative () {
        Initiative initiative = createStep2Initiative();
        //TODO ora settato con l'utilizzo dei RewardGroups. Associare un faker booleano per i casi OK, altrimenti separare i 2 casi
        initiative.setRewardRule(createRewardRule(false));
        initiative.setTrxRule(createTrxRuleCondition());
        return initiative;
    }

    private InitiativeRewardRule createRewardRule(boolean isRewardFixedValue) {
        if(isRewardFixedValue){
            //TODO Aggiungere RewardValue
            return null;
        }
        else {
            RewardGroups rewardGroups = new RewardGroups();
            RewardGroups.RewardGroup rewardGroup1 = new RewardGroups.RewardGroup(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
            RewardGroups.RewardGroup rewardGroup2 = new RewardGroups.RewardGroup(BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(40));
            List<RewardGroups.RewardGroup> rewardGroupList = new ArrayList<>();
            rewardGroupList.add(rewardGroup1);
            rewardGroupList.add(rewardGroup2);
            rewardGroups.setRewardGroups(rewardGroupList);
            return rewardGroups;
        }
    }

    private InitiativeTrxConditions createTrxRuleCondition() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();

        List<DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeek.DayConfig dayConfig1 = new DayOfWeek.DayConfig();
        Set<java.time.DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(java.time.DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeek.Interval> intervals = new ArrayList<>();
        DayOfWeek.Interval interval1 = new DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);
        DayOfWeek dayOfWeek = new DayOfWeek(dayConfigs);

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
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
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

    InitiativeDTO createStep3InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        //TODO ora settato con l'utilizzo dei RewardGroups. Associare un faker booleano per i casi OK, altrimenti separare i 2 casi
        initiativeDTO.setRewardRule(createRewardRuleDTO(false));
        initiativeDTO.setTrxRule(createTrxRuleConditionDTO());
        return initiativeDTO;
    }

    private InitiativeRewardRuleDTO createRewardRuleDTO(boolean isRewardFixedValue) {
        if(isRewardFixedValue){
            //TODO Aggiungere RewardValue
            return null;
        }
        else {
            RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
            RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
            RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(40));
            List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
            rewardGroupDTOList.add(rewardGroupDTO1);
            rewardGroupDTOList.add(rewardGroupDTO2);
            rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
            return rewardGroupsDTO;
        }
    }

    private InitiativeTrxConditionsDTO createTrxRuleConditionDTO() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();

        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<java.time.DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(java.time.DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
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
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
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


    Initiative createStep4Initiative () {
        Initiative initiative = createStep3Initiative();
        return initiative;
    }

    InitiativeDTO createStep4InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep5Initiative () {
        Initiative initiative = createStep4Initiative();
        return initiative;
    }

    private InitiativeLegal createInitiativeLegal() {
        InitiativeLegal initiativeLegal = new InitiativeLegal();
        initiativeLegal.setDpiaLink("https://www.google.it");
        initiativeLegal.setPrivacyLink("https://www.google.it");
        initiativeLegal.setRegulationLink("https://www.google.it");
        initiativeLegal.setTcLink("https://www.google.it");
        return initiativeLegal;
    }

    InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    AccumulatedAmount createAccumulatedAmountValid(){
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccomulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThreshold(BigDecimal.valueOf(100000));
        return amount;
    }

    TimeParameter createTimeParameterValid(){
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    AdditionalInfo createAdditionalInfoValid(){
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }

    InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmountValid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }
    InitiativeRefundRule createRefundRuleValidWithTimeParameter(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameterValid());
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }

    Initiative createInitiativeOnlyRefundRule(){
        Initiative initiative = createStep1Initiative();
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }
}
