package it.gov.pagopa.initiative.service;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.connector.selc.SelcRestConnector;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EmailNotificationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmailNotificationServiceTest {

    private static final String INITIATIVE_NAME = "initiativeName1";
    private static final String ORGANIZATION_ID = "organizationId1";
    private static final String INITIATIVE_ID = "initiativeId";
    private static final String SERVICE_ID = "serviceId";
    private static final String ANY_ROLE = "ANY_ROLE";
    private static final String ADMIN_ROLE = "admin";
    private static final String PAGOPA_ADMIN_ROLE = "pagopa_admin";
    private static final String TEMPLATE_NAME = "templateName";
    private static final String SUBJECT = "SUBJECT";
    private static final String EXCEPTION_MESSAGE = "Exception Message";
    public static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    public static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";

    @MockBean
    private EmailNotificationRestConnector emailNotificationRestConnector;

    @Autowired
    private EmailNotificationServiceImpl emailNotificationServiceImpl;

    @MockBean
    private SelcRestConnector selcRestConnector;

    @Test
    void testSendInitiativePagoPA_success() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification(any(), any(),
                        any(), any(), any());

        Initiative step2Initiative = createStep2Initiative();
        emailNotificationServiceImpl.sendInitiativeToPagoPA(step2Initiative, TEMPLATE_NAME, SUBJECT);
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification(any(),
                any(), any(), any(), any());
    }

    @Test
    void testSendInitiativePagoPA_FeignException() {
        Initiative step2Initiative = createStep2Initiative();
        Request request = Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(), null, new RequestTemplate());
        //doThrow FeignException
        doThrow(new FeignException.InternalServerError(EXCEPTION_MESSAGE, request, null, null))
                .when(emailNotificationRestConnector).notifyInitiativeToEmailNotification(any(), any(), any(),
                        any(), any());

        emailNotificationServiceImpl.sendInitiativeToPagoPA(step2Initiative, TEMPLATE_NAME, SUBJECT);

        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification(any(),
                any(), any(), any(), any());
    }

    @Test
    void testSendInitiativeCurrentOrganization_success() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification(any(), any(), any(),
                        any(), any());
        UUID uuid = UUID.fromString("a0a00000-0aaa-00aa-0000-0000000a0000");
        List<UserResource> userResources = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        roles.add(ADMIN_ROLE);
        UserResource userResource = UserResource.builder()
                .id(uuid)
                .name("name")
                .surname("surname")
                .email("email")
                .roles(roles)
                .build();
        userResources.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers(any())).thenReturn(userResources);

        Initiative step2Initiative = createStep2Initiative();
        emailNotificationServiceImpl.sendInitiativeToCurrentOrganization(step2Initiative, TEMPLATE_NAME, SUBJECT);
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification(any(),
                any(), any(), any(), any());
        verify(selcRestConnector, times(1)).getInstitutionProductUsers(any());
    }

    @Test
    void testSendInitiativeCurrentOrganization_IllegalStateException() {
        Initiative step2Initiative = createStep2Initiative();
        emailNotificationServiceImpl.sendInitiativeToCurrentOrganization(step2Initiative, TEMPLATE_NAME, SUBJECT);
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification(any(),
                any(), any(), any(), any());
        verify(selcRestConnector, times(1)).getInstitutionProductUsers(any());
    }

    /**
     * Method under test: {@link EmailNotificationService#sendInitiativeToPagoPA(Initiative, String, String)}
     */
    @Test
    void testSendInitiativeCurrentOrganization_FeignException() {
        Request request = Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(), null, new RequestTemplate());
        //doThrow FeignException
        doThrow(new FeignException.InternalServerError(EXCEPTION_MESSAGE, request, null, null))
                .when(emailNotificationRestConnector).notifyInitiativeToEmailNotification(any(), any(), any(),
                        any(), any());

        UUID uuid = UUID.fromString("a0a00000-0aaa-00aa-0000-0000000a0000");
        List<UserResource> userResources = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        roles.add(ADMIN_ROLE);
        UserResource userResource = UserResource.builder()
                .id(uuid)
                .name("name")
                .surname("surname")
                .email("email")
                .roles(roles)
                .build();
        userResources.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers(any())).thenReturn(userResources);

        Initiative step2Initiative = createStep2Initiative();
        emailNotificationServiceImpl.sendInitiativeToCurrentOrganization(step2Initiative, TEMPLATE_NAME, SUBJECT);
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification(any(),
                any(), any(), any(), any());
        verify(selcRestConnector, times(1)).getInstitutionProductUsers(any());
    }

    @Test
    void getInstitutionProductUsersEmailsByRole_Exception() {
        when(selcRestConnector.getInstitutionProductUsers(ORGANIZATION_ID));
        try {
            emailNotificationServiceImpl.getInstitutionProductUsersEmailsByRole(ORGANIZATION_ID);
        } catch (Exception exception) {
            Assertions.fail(String.valueOf(exception));
        }
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
        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(boolean beneficiaryKnown) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudgetCents(1000L);
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        initiativeGeneral.setBudgetCents(100000000000L);
        initiativeGeneral.setEndDate(LocalDate.of(2022, 9, 8));
        initiativeGeneral.setStartDate(LocalDate.of(2022, 8, 8));
        initiativeGeneral.setRankingStartDate(LocalDate.of(2022, 9, 18));
        initiativeGeneral.setRankingEndDate(LocalDate.of(2022, 8, 18));
        initiativeGeneral.setDescriptionMap(language);
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
        new InitiativeDTO();
        InitiativeDTO initiativeDTO;
        initiativeDTO = InitiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName(INITIATIVE_NAME)
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .additionalInfo(createInitiativeAdditionalDTO()).build();
        return initiativeDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(boolean beneficiaryKnown) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
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
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId(SERVICE_ID);
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("description");
        initiativeAdditionalDTO.setPrivacyLink("privacy.url.it");
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
        initiativeBeneficiaryRule.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRule.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
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
        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRuleDTO;
    }
}

