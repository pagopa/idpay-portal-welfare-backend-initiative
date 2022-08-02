package it.gov.pagopa.initiative.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.mapper.InitiativeDTOsToModelMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.service.InitiativeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {
        InitiativeApi.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Slf4j
class InitiativeApiTest {

    @MockBean
    InitiativeService initiativeService;

    @MockBean
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @MockBean
    InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mvc;

    private static final String ORGANIZATION_ID_PLACEHOLDER = "{0}";
    private static final String INITIATIVE_0_ID_PLACEHOLDER = "{0}";
    private static final String INITIATIVE_1_ID_PLACEHOLDER = "{1}";

    private static final String BASE_URL = "http://localhost:8080/idpay";
    private static final String GET_INITIATIVES_SUMMARY_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/summary";
    private static final String GET_INITIATIVE_ACTIVE_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_1_ID_PLACEHOLDER;
    private static final String GET_INITIATIVE_BENEFICIARY_VIEW_URL = "/initiative/" + INITIATIVE_0_ID_PLACEHOLDER + "/beneficiary/view";
    private static final String POST_INITIATIVE_GENERAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/general";
    private static final String PUT_INITIATIVE_GENERAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_1_ID_PLACEHOLDER + "/general";
    private static final String PUT_INITIATIVE_BENEFICIARY_RULES_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_1_ID_PLACEHOLDER + "/beneficiary";
    private static final String ROLE = "TEST_ROLE";

    @Test
    void getInitiativeSummary_ok() throws Exception {

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        Initiative step2Initiative2 = createStep2Initiative(beneficiaryKnown);
        List<Initiative> initiatives = Arrays.asList(step2Initiative, step2Initiative2);

        // Returning something from Repo by using ServiceMock
        when(initiativeService.retrieveInitiativeSummary(anyString())).thenReturn(initiatives);

        // When
        List<Initiative> retrieveInitiativeSummary = initiativeService.retrieveInitiativeSummary(anyString());

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", retrieveInitiativeSummary, is(sameInstance(initiatives)));

        mvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + MessageFormat.format(GET_INITIATIVES_SUMMARY_URL, "Ente1"))
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getInitiativeDetail_ok() throws Exception {

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString())).thenReturn(step2Initiative);

        Initiative initiative = initiativeService.getInitiative(anyString(), anyString());

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", initiative, is(sameInstance(step2Initiative)));

        //The MVC perform should perform the API by returning the response based on the Service previously mocked.
        mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + MessageFormat.format(GET_INITIATIVE_ACTIVE_URL, "Ente1", "Id1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void saveInitiativeGeneralInfo_ok() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative(beneficiaryKnown);

        //create Dummy BodyRequest InitiativeInfoDTO
        InitiativeInfoDTO initiativeInfoDTO = createStep1InitiativeInfoDTO();

        // Instruct the Service to insert a Dummy Initiative
//        doNothing().when(initiativeService).insertInitiative(step1Initiative); //doNothing only for Void method
        when(initiativeService.insertInitiative(step1Initiative)).thenReturn(step1Initiative);

        when(initiativeDTOsToModelMapper.toInitiative(initiativeInfoDTO)).thenReturn(step1Initiative);

//        Map<String, Object> body = new HashMap<>();
//        body.put("general", initiativeGeneralDTO);
//        body.put("additionalInfo", initiativeAdditionalDTO);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + MessageFormat.format(POST_INITIATIVE_GENERAL_INFO_URL, "Ente1"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(body))
                .content(objectMapper.writeValueAsString(initiativeInfoDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeGeneralInfo_ok() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative(beneficiaryKnown);
        //create Dummy BodyRequest InitiativeInfoDTO
        InitiativeInfoDTO initiativeInfoDTO = createStep1InitiativeInfoDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeInfoDTO)).thenReturn(step1Initiative);

        //doNothing only for Void method
        Initiative toInitiativeInfoModel = initiativeDTOsToModelMapper.toInitiative(initiativeInfoDTO);
        doNothing().when(initiativeService).updateInitiativeGeneralInfo("Ente1", "Id1", toInitiativeInfoModel);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + MessageFormat.format(PUT_INITIATIVE_GENERAL_INFO_URL, "Ente1", "Id1"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(initiativeInfoDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeBeneficiary_ok() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        //create Dummy BodyRequest InitiativeInfoDTO
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        // Instruct the Service to get a Dummy Initiative
        when(initiativeService.getInitiative("Ente1", "Id1")).thenReturn(step2Initiative);

        //doNothing only for Void method
        InitiativeBeneficiaryRule initiativeBeneficiaryRule2 = initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        doNothing().when(initiativeService).updateInitiativeBeneficiary("Ente1", "Id1", initiativeBeneficiaryRule2);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + MessageFormat.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, "Ente1", "Id1"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeBeneficiary_ko() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = true;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        //create Dummy BodyRequest InitiativeInfoDTO
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        // Instruct the Service to get a Dummy Initiative
        when(initiativeService.getInitiative("Ente1", "Id1")).thenReturn(step2Initiative);

        //doNothing only for Void method
        InitiativeBeneficiaryRule initiativeBeneficiaryRule2 = initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        doNothing().when(initiativeService).updateInitiativeBeneficiary("Ente1", "Id1", initiativeBeneficiaryRule2);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + MessageFormat.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, "Ente1", "Id1"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeBeneficiaryDraft_ok() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        //create Dummy BodyRequest InitiativeInfoDTO
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        //doNothing only for Void method
        InitiativeBeneficiaryRule initiativeBeneficiaryRule2 = initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        doNothing().when(initiativeService).updateInitiativeBeneficiary("Ente1", "Id1", initiativeBeneficiaryRule2);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + MessageFormat.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL + "/draft", "Ente1", "Id1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getInitiativeBeneficiaryView_ok() throws Exception {

        //create Dummy Initiative
        Initiative initiative = createFullInitiative();
        InitiativeDTO initiativeDTO = createFullInitiativeDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative)).thenReturn(initiativeDTO);
        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiativeBeneficiaryView(anyString())).thenReturn(initiative);

        //The MVC perform should perform the API by returning the response based on the Service previously mocked.
        mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + MessageFormat.format(GET_INITIATIVE_BENEFICIARY_VIEW_URL, "Id1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    Initiative createFullInitiative () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        Boolean beneficiaryKnown = false;
        Initiative initiative = createStep2Initiative(beneficiaryKnown);
        return initiative;
    }

    InitiativeDTO createFullInitiativeDTO () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep1Initiative (Boolean beneficiaryKnown) {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId("Id1");
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId("organizationId1");
        initiative.setStatus("DRAFT");
        initiative.setAutocertificationCheck(true);
        initiative.setBeneficiaryRanking(true);
        initiative.setPdndCheck(true);
        initiative.setPdndToken("pdndToken1");
        initiative.setServiceId("serviceId");
        initiative.setGeneral(createInitiativeGeneral(beneficiaryKnown));
        initiative.setAdditionalInfo(createInitiativeAdditional());
//        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
//        initiative.setLegal(createInitiativeLegal());
        return initiative;
    }
    private InitiativeGeneral createInitiativeGeneral(Boolean beneficiaryKnown) {
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
        return initiativeGeneral;
    }

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceId("serviceId");
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setArgument("Argument");
        initiativeAdditional.setDescription("Description");
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
        automatedCriteria.setOperator("Operator");
        automatedCriteria.setValue("value");
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteria);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRule;
    }

    private InitiativeLegal createInitiativeLegal() {
        InitiativeLegal initiativeLegal = new InitiativeLegal();
        initiativeLegal.setDpiaLink("https://www.google.it");
        initiativeLegal.setPrivacyLink("https://www.google.it");
        initiativeLegal.setRegulationLink("https://www.google.it");
        initiativeLegal.setTcLink("https://www.google.it");
        return initiativeLegal;
    }

    InitiativeDTO createStep1InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        initiativeDTO = initiativeDTO.builder()
                .initiativeId("Id1")
                .initiativeName("initiativeName1")
                .organizationId("organizationId1")
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .pdndCheck(true)
                .pdndToken("pdndToken1")
                .general(createInitiativeGeneralDTO()).additionalInfo(createInitiativeAdditionalDTO()).build();
        return initiativeDTO;
    }

    InitiativeInfoDTO createStep1InitiativeInfoDTO() {
        InitiativeInfoDTO initiativeInfoDTO = new InitiativeInfoDTO();
        initiativeInfoDTO = initiativeInfoDTO.builder().general(createInitiativeGeneralDTO()).additionalInfo(createInitiativeAdditionalDTO()).build();
        return initiativeInfoDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(false);
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
        initiativeAdditionalDTO.setServiceId("serviceId");
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setArgument("Argument");
        initiativeAdditionalDTO.setDescription("Description");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.EMAIL);
        channelDTO.setContact("contact");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

    Initiative createStep2Initiative (Boolean beneficiaryKnown) {
        Initiative initiative = createStep1Initiative(beneficiaryKnown);
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiative.setBeneficiaryRule(initiativeBeneficiaryRule);
        return initiative;
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
        automatedCriteriaDTO.setOperator("Operator");
        automatedCriteriaDTO.setValue("value");
        List<AutomatedCriteriaDTO> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaDTO);
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRuleDTO;
    }

    Initiative createStep3Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep3InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep4Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep4InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep5Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep6Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep6InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

}