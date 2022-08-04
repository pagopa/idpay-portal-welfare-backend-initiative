package it.gov.pagopa.initiative.service;


import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {
        InitiativeService.class})
@Slf4j
class InitiativeServiceTest {

    @Autowired
    InitiativeService initiativeService;

    @MockBean
    InitiativeRepository initiativeRepository;

//    List<Initiative> retrieveInitiativeSummary(String organizationId);
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
        initiativeService.updateInitiativeGeneralInfo("Ente1", "Id1", step2Initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeId(anyString(), anyString());
    }

    @Test
    void updateInitiativeGeneralInfo_ko() throws Exception {
        Initiative fullInitiative = createFullInitiative();
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeGeneralInfo("Ente1", "Id1", fullInitiative);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeBeneficiary_ok() throws Exception {
        Initiative fullInitiative = createFullInitiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeId(anyString(), anyString())).thenReturn(Optional.ofNullable(fullInitiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeBeneficiary("Ente1", "Id1", initiativeBeneficiaryRule);

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
                        MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_ORGANIZATION_ID_MESSAGE, "Ente1", "Id1"),
                        HttpStatus.NOT_FOUND)
        );

        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeBeneficiary("Ente1", "Id1", initiativeBeneficiaryRule);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_ORGANIZATION_ID_MESSAGE, "Ente1", "Id1"), e.getMessage());
        }
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

    Initiative createStep1Initiative () {
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

        initiative.setGeneral(createInitiativeGeneral());
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

    Initiative createStep2Initiative () {
        Initiative initiative = createStep1Initiative();
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
