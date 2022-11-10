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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@WebMvcTest(value = {
        InitiativeValidationService.class})
@Slf4j
class InitiativeValidationServiceTest {

    private static final String INITIATIVE_NAME = "initiativeName1";
    private static final String ORGANIZATION_ID = "organizationId1";
    private static final String INITIATIVE_ID = "initiativeId";
    private static final String SERVICE_ID = "serviceId";
    private static final String ANY_ROLE = "ANY_ROLE";
    private static final String ADMIN_ROLE = "admin";
    private static final String OPE_BASE_ROLE = "ope_base";

    @Autowired
    InitiativeValidationService initiativeValidationService;

    @MockBean
    InitiativeRepository initiativeRepository;

    @Test
    void givenAdminRole_whenInitiativeStatusIsValid_thenOk() {
        Initiative step2Initiative = createStep2Initiative();

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
        Initiative step2Initiative = createStep2Initiative();
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
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step2Initiative));

        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, OPE_BASE_ROLE));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, OPE_BASE_ROLE), exception.getMessage());
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
        initiative.setPdndToken("pdndToken1");
        initiative.setAdditionalInfo(createInitiativeAdditional());
//        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
//        initiative.setLegal(createInitiativeLegal());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(boolean beneficiaryKnown) {
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
        initiativeAdditional.setServiceId(SERVICE_ID);
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

    private InitiativeGeneralDTO createInitiativeGeneralDTO(boolean beneficiaryKnown) {
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
        return initiativeGeneralDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId(SERVICE_ID);
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("description");
        initiativeAdditionalDTO.setPrivacyLink("privacy.url.it");;
        initiativeAdditionalDTO.setTcLink("tos.url.it");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.WEB);
        channelDTO.setContact("support.url.it");
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
}
