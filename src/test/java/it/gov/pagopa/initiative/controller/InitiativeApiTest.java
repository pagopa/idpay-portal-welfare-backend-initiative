package it.gov.pagopa.initiative.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeDTOsToModelMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.service.InitiativeService;
import it.gov.pagopa.initiative.service.OrganizationService;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.CODE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.ACCUMULATED_AMOUNT_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.SOMETHING_WRONG_WITH_THE_REFUND_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.ADMIN;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.PAGOPA_ADMIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.initiative.conditions.notifyRE=true",
                "app.initiative.conditions.notifyIO=true",
                "app.initiative.conditions.notifyInternal=true",
                "app.initiative.ranking.gracePeriod=10"
        })
@WebMvcTest(value = {
        InitiativeApi.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class InitiativeApiTest {

    public static final String INITIATIVE_ID = "Id1";
    public static final String ORGANIZATION_ID = "O1";
    public static final String SERVICE_ID = "service1";

    private static final String ORGANIZATION_ID_PLACEHOLDER = "%s";
    private static final String INITIATIVE_ID_PLACEHOLDER = "%s";
    private static final String SERVICE_ID_PLACEHOLDER = "%s";

    private static final String ROLE_PLACEHOLDER = "%s";

    private static final String ROLE = "admin";

    private static final String ROLE_QUERY_PARAMETER_PLACEHOLDER = "?role=";

    private static final String BASE_URL = "http://localhost:8080/idpay";
    private static final String GET_INITIATIVES_SUMMARY_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/summary";
    private static final String GET_INITIATIVES_ISSUER = "/initiatives";
    private static final String GET_INITIATIVE_ACTIVE_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER;
    private static final String GET_INITIATIVE_ID_FROM_SERVICE_ID = "/initiative?serviceId=" + SERVICE_ID_PLACEHOLDER;
    private static final String GET_PRIMARY_AND_SECONDARY_TOKEN_FROM_INITIATIVE_ID = "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/token";
    private static final String GET_INITIATIVE_BENEFICIARY_VIEW_URL = "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/beneficiary/view";
    private static final String POST_INITIATIVE_ADDITIONAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/info";
    private static final String GET_RANKING_LIST = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/ranking/exports";

    private static final String PUT_INITIATIVE_ADDITIONAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/info";
    private static final String PUT_INITIATIVE_GENERAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/general";
    private static final String PUT_LOGO_URL =
            "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/logo";
    private static final String PUT_TRX_REWARD_RULE_URL =
            "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/reward";
    private static final String PUT_INITIATIVE_REFUND_RULES_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/refund";
    private static final String PUT_INITIATIVE_BENEFICIARY_RULES_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/beneficiary" + ROLE_QUERY_PARAMETER_PLACEHOLDER + ROLE_PLACEHOLDER;
    private static final String PUT_INITIATIVE_BENEFICIARY_RULES_DRAFT_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/beneficiary";
    private static final String PUT_INITIATIVE_STATUS_APPROVED_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/approved";
    private static final String PUT_INITIATIVE_TO_CHECK_STATUS_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/rejected";
    private static final String PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/published" + ROLE_QUERY_PARAMETER_PLACEHOLDER + ROLE_PLACEHOLDER;
    private static final String LOGICALLY_DELETE_INITIATIVE_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER;
    //    private static final String ROLE = "TEST_ROLE";
    private static final String GET_INITIATIVE_BENEFICIARY_DETAIL_URL = "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/detail";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    public static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    public static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";

    @MockBean
    InitiativeService initiativeService;
    @MockBean
    OrganizationService organizationService;

    @Autowired
    InitiativeApiController initiativeApiController;

    @MockBean
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @MockBean
    InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    @MockBean
    InitiativeGeneralDTO initiativeGeneralDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mvc;

    /* @Test
    void whenAdmin_getInitiativeSummary_statusOk() throws Exception {

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        Initiative step2Initiative2 = createStep2Initiative(beneficiaryKnown);
        List<Initiative> initiatives = Arrays.asList(step2Initiative, step2Initiative2);

        // Returning something from Repo by using ServiceMock
        when(initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, ADMIN)).thenReturn(initiatives);

        // When
        List<Initiative> retrieveInitiativeSummary = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, ADMIN);

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", retrieveInitiativeSummary, is(sameInstance(initiatives)));

        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVES_SUMMARY_URL, ORGANIZATION_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }
     */

    //@Test
    @ParameterizedTest
    @ValueSource(strings = {PAGOPA_ADMIN, ADMIN})

    void whenPagoPaAdmin_getInitiativeSummary_statusOk(String role) throws Exception {

        Boolean beneficiaryKnown = false;

        Initiative step1Initiative = createStep1Initiative();
        Initiative step1Initiative2 = createStep1Initiative();
        List<Initiative> initiatives = Arrays.asList(step1Initiative, step1Initiative2);

        InitiativeSummaryDTO step1InitiativeSummaryDTO = createStep1InitiativeSummaryDTO();
        InitiativeSummaryDTO step1InitiativeSummary2DTO = createStep1InitiativeSummaryDTO();
        List<InitiativeSummaryDTO> initiativeSummaryDTOs = Arrays.asList(step1InitiativeSummaryDTO, step1InitiativeSummary2DTO);


        when(initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, role)).thenReturn(initiatives);

        // When
        List<Initiative> retrieveInitiativeSummary = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, role);
        when(initiativeModelToDTOMapper.toInitiativeSummaryDTOList(retrieveInitiativeSummary)).thenReturn(initiativeSummaryDTOs);
        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", retrieveInitiativeSummary, is(sameInstance(initiatives)));

         mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVES_SUMMARY_URL, ORGANIZATION_ID))
                                .queryParam("role",role)
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"initiativeId\":\"initiativeId\",\"initiativeName\":\"initiativeName\",\"status\":\"PUBLISHED\"},{\"initiativeId\":\"initiativeId\",\"initiativeName\":\"initiativeName\",\"status\":\"PUBLISHED\"}]"))
                .andDo(print())
                .andReturn();
    }

    private InitiativeSummaryDTO createStep1InitiativeSummaryDTO() {
        return InitiativeSummaryDTO.builder()
                .initiativeId("initiativeId")
                .initiativeName("initiativeName")
                .status(InitiativeConstants.Status.PUBLISHED)
                .build();
    }

    @Test
    void getInitiativesIssuer_Ok() throws Exception {

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        Initiative step2Initiative2 = createStep2Initiative(beneficiaryKnown);
        List<Initiative> initiatives = Arrays.asList(step2Initiative, step2Initiative2);

        // Returning something from Repo by using ServiceMock
        when(initiativeService.getInitiativesIssuerList()).thenReturn(initiatives);

        // When
        List<Initiative> initiativesIssuer = initiativeService.getInitiativesIssuerList();

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", initiativesIssuer, is(sameInstance(initiatives)));
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + GET_INITIATIVES_ISSUER)
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getInitiativeDetail_statusOk() throws Exception {

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString(), anyString())).thenReturn(step2Initiative);

        Initiative initiative = initiativeService.getInitiative(anyString(), anyString(), anyString());

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", initiative, is(sameInstance(step2Initiative)));

        //The MVC perform should perform the API by returning the response based on the Service previously mocked.
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVE_ACTIVE_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void testAddLogo() throws IOException {

        InitiativeService initiativeService = mock(InitiativeService.class);
        when(initiativeService.storeInitiativeLogo(any(), any(), any(), any(),
                any())).thenReturn(new LogoDTO());
        OrganizationService organizationService = mock(OrganizationService.class);
        InitiativeModelToDTOMapper initiativeModelToDTOMapper = new InitiativeModelToDTOMapper();
        InitiativeDTOsToModelMapper initiativeDTOsToModelMapper = new InitiativeDTOsToModelMapper();
        InitiativeApiController initiativeApiController = new InitiativeApiController(
                true,
                true,
                true,
                initiativeService,
                organizationService,
                initiativeModelToDTOMapper,
                initiativeDTOsToModelMapper
        );
        ResponseEntity<LogoDTO> actualAddLogoResult = initiativeApiController.addLogo("42", "42",
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAA".getBytes("UTF-8"))));
        assertTrue(actualAddLogoResult.hasBody());
        assertTrue(actualAddLogoResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualAddLogoResult.getStatusCode());
        verify(initiativeService).storeInitiativeLogo(any(), any(), any(), any(),
                any());
    }

    @Test
    void getInitiativeIdFromServiceId_statusOk() throws Exception {
        Initiative step1Initiative = createStep1Initiative();
        when(initiativeService.getInitiativeIdFromServiceId(SERVICE_ID)).thenReturn(step1Initiative);
        Initiative initiative = initiativeService.getInitiativeIdFromServiceId(SERVICE_ID);
        assertThat("Reason of result", initiative, is(sameInstance(step1Initiative)));
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVE_ID_FROM_SERVICE_ID, ORGANIZATION_ID, SERVICE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getInitiativeIdFromServiceId_Exception() {
        try {
            initiativeApiController.getInitiativeIdFromServiceId(Locale.forLanguageTag("xxxx"), SERVICE_ID);
        } catch (InitiativeException e) {
            assertEquals(CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.BadRequest.INVALID_LOCALE_FORMAT, "xxxx"), e.getMessage());
        }
    }

    @Test
    void getPrimaryAndSecondaryTokenIO_statusOk() throws Exception {
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        when(initiativeService.getPrimaryAndSecondaryTokenIO(INITIATIVE_ID)).thenReturn(initiativeAdditional);
        InitiativeAdditional additional = initiativeService.getPrimaryAndSecondaryTokenIO(INITIATIVE_ID);
        assertThat("Reason of result", additional, is(sameInstance(initiativeAdditional)));
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_PRIMARY_AND_SECONDARY_TOKEN_FROM_INITIATIVE_ID, SERVICE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void saveInitiativeServiceInfo_statusCreated() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();

        InitiativeAdditionalDTO initiativeAdditionalDTO = createStep1InitiativeAdditionalDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeService.insertInitiative(step1Initiative, ORGANIZATION_ID, ORGANIZATION_NAME, ROLE)).thenReturn(step1Initiative);

        when(initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO)).thenReturn(step1Initiative);

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL + String.format(POST_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeAdditionalDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeGeneralInfo_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO(beneficiaryKnown);

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeGeneralDTO)).thenReturn(step2Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative, ROLE, true);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_GENERAL_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeGeneralDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeAdditionalInfo_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO)).thenReturn(step1Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeAdditionalInfo(ORGANIZATION_ID, INITIATIVE_ID, step1Initiative, ROLE);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeAdditionalDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeGeneralInfoDraft_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO)).thenReturn(step1Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeAdditionalInfo(ORGANIZATION_ID, INITIATIVE_ID, step1Initiative, ROLE);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_GENERAL_INFO_URL + "/draft",
                                ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeAdditionalDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeBeneficiary_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRuleDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeBeneficiaryRuleDTO.setOrganizationUserRole(ROLE);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        // Instruct the Service to get a Dummy Initiative
        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule, ROLE, false);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeBeneficiary_statusBadRequest() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = true;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRuleDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeBeneficiaryRuleDTO.setOrganizationUserRole(ROLE);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        // Instruct the Service to get a Dummy Initiative
        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule, ROLE, false);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateTrxAndRewardRules_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO)).thenReturn(step1Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, step1Initiative, ROLE, false);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_TRX_REWARD_RULE_URL,
                                ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(rewardAndTrxRulesDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateTrxAndRewardRulesDraft_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO)).thenReturn(step1Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, step1Initiative, ROLE, true);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_TRX_REWARD_RULE_URL + "/draft",
                                ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(rewardAndTrxRulesDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeRefundRule_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeDTOsToModelMapper.toInitiative(refundRuleDTO)).thenReturn(initiative);

        doNothing().when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, true);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(refundRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateRefundRule_PUT_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

        Initiative initiative = createInitiativeOnlyRefundRule();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(refundRuleDTO)).thenReturn(initiative);

        //doThrow InitiativeException for Void method
        doThrow(new InitiativeException(
                CODE,
                String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()),
                HttpStatus.BAD_REQUEST))
                .when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, true);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(refundRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeRefundRuleDraft_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeDTOsToModelMapper.toInitiative(refundRuleDTO)).thenReturn(initiative);

        doNothing().when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, false);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL + "/draft", ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(refundRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeBeneficiaryDraft_statusNoContent() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        createStep2Initiative(beneficiaryKnown);
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        //doNothing only for Void method
        InitiativeBeneficiaryRule initiativeBeneficiaryRule2 = initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        doNothing().when(initiativeService).updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule2, ROLE, true);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_DRAFT_URL + "/draft", ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeStatusApproved_statusNoContent() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = new InitiativeOrganizationInfoDTO();
        initiativeOrganizationInfoDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeOrganizationInfoDTO.setOrganizationUserRole(ROLE);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_STATUS_APPROVED_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeToCheckStatus_statusNoContent() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = new InitiativeOrganizationInfoDTO();
        initiativeOrganizationInfoDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeOrganizationInfoDTO.setOrganizationUserRole(ROLE);

        doNothing().when(initiativeService).updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_CHECK_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getInitiativeBeneficiaryView_statusOk() throws Exception {

        //create Dummy Initiative
        Initiative initiative = createStep5Initiative();
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative)).thenReturn(initiativeDTO);
        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiativeBeneficiaryView(anyString())).thenReturn(initiative);

        //The MVC perform should perform the API by returning the response based on the Service previously mocked.
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVE_BENEFICIARY_VIEW_URL, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void anyUpdate_PUT_whenBodyRequestIsNotValid_then400BadRequest_MethodArgumentNotValidException() throws Exception {
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleWithAccumulatedAmountDTO_NotValid_ko();

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(refundRuleDTO))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(print())
                        .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(CODE, error.getCode());
        assertTrue(error.getMessage().contains(ACCUMULATED_AMOUNT_TYPE));
    }

    @Test
    void PUT_updateInitiativeStatusToCheck_whenStatusIsNotInRevision() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = new InitiativeOrganizationInfoDTO();
        initiativeOrganizationInfoDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeOrganizationInfoDTO.setOrganizationUserRole(ROLE);

        doThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION, INITIATIVE_ID),
                        HttpStatus.BAD_REQUEST)
        ).when(initiativeService).updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_CHECK_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(print())
                        .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(CODE, error.getCode());
        assertTrue(error.getMessage().contains(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION.formatted(INITIATIVE_ID)));
    }

    @Test
    void DEL_logicallyDeleteInitiative_whenCurrentDeletedIsFalse_thenBecomeTrue() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = new InitiativeOrganizationInfoDTO();
        initiativeOrganizationInfoDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeOrganizationInfoDTO.setOrganizationUserRole(ROLE);

        Initiative initiative = createStep5Initiative();
        initiative.setEnabled(true);

        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + String.format(LOGICALLY_DELETE_INITIATIVE_URL, initiative.getOrganizationId(), initiative.getInitiativeId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
//        assertEquals(true, initiative.getDeleted());
    }

    @Test
    void anyUpdate_PUT_whenBodyRequestIsNotValid_then400BadRequest_MethodArgumentNotValidExceptionElseCase() throws Exception {
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleWithAccumulatedAmountAndTimeParameter_NotValid();

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(refundRuleDTO))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(print())
                        .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(CODE, error.getCode());
        assertTrue(error.getMessage().contains(SOMETHING_WRONG_WITH_THE_REFUND_TYPE));
    }

    @Test
    void when_PUT_updateInitiativePublishedStatus_ToIO_then204() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ROLE)
                .build();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        createStep5InitiativeDTO();

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString(), anyString())).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(anyString(), anyString(), anyString());
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        when(initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(step5Initiative, initiativeOrganizationInfoDTO)).thenReturn(step5Initiative);


        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void when_PUT_updateInitiativePublishedStatus_NotToIO_then204() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ROLE)
                .build();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        InitiativeAdditional additionalInfo = step5Initiative.getAdditionalInfo();
        additionalInfo.setServiceIO(false);
        step5Initiative.setAdditionalInfo(additionalInfo);

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString(), anyString())).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(anyString(), anyString(), anyString());
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void givenRuleEngineError_when_PUT_updateInitiativePublishedStatus_thenThrowException() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ROLE)
                .build();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO();

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative)).thenReturn(step5InitiativeDTO);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doThrow(
                new KafkaException()
        ).when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(print())
                        .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
    }

    @Test
    void givenIOBackEndError_when_PUT_updateInitiativePublishedStatus_thenThrowException() throws Exception {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = createInitiativeOrganizationInfoDTO();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO();

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative)).thenReturn(step5InitiativeDTO);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        doThrow(
                FeignException.errorStatus("Bad Request", responseStub())
        ).when(initiativeService).sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(step5Initiative, initiativeOrganizationInfoDTO);

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(initiativeOrganizationInfoDTO))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(print())
                        .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(InitiativeConstants.Exception.Publish.BadRequest.CODE, error.getCode());
        assertTrue(error.getMessage().contains(InitiativeConstants.Exception.Publish.BadRequest.INTEGRATION_FAILED));
    }

    @Test
    void getListOfOrganization() throws Exception {
        List<OrganizationDTO> organizationDTOList = createOrganizationDTOList();
        when(organizationService.getOrganizationList(ADMIN)).thenReturn(organizationDTOList);
        when(organizationService.getOrganizationList(PAGOPA_ADMIN)).thenReturn(organizationDTOList);
        when(organizationService.getOrganizationList("default")).thenReturn(null);

        MvcResult resultAdmin = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/organizations")
                        .param("role", ADMIN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult resultPagoPaAdmin = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/organizations")
                        .param("role", PAGOPA_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult resultDefault = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/organizations")
                        .param("role", "default")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String expectedResultContent = "[{\"organizationId\":\"organizationId_0\",\"organizationName\":\"organizationName_0\"},{\"organizationId\":\"organizationId_1\",\"organizationName\":\"organizationName_1\"},{\"organizationId\":\"organizationId_2\",\"organizationName\":\"organizationName_2\"},{\"organizationId\":\"organizationId_3\",\"organizationName\":\"organizationName_3\"}]";
        assertEquals(expectedResultContent, resultAdmin.getResponse().getContentAsString());
        assertEquals(expectedResultContent, resultPagoPaAdmin.getResponse().getContentAsString());
        assertEquals("", resultDefault.getResponse().getContentAsString());
    }

    @Test
    void getOrganization() throws Exception {
        OrganizationDTO organization = createOrganizationDTO(1);
        when(organizationService.getOrganization(organization.getOrganizationId())).thenReturn(organization);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL + "/organizations/{organizationId}", organization.getOrganizationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        String expectedResultContent = "{\"organizationId\":\"organizationId_1\",\"organizationName\":\"organizationName_1\"}";
        assertEquals(expectedResultContent, result.getResponse().getContentAsString());
    }

    @Test
    void testGetRankingList() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_RANKING_LIST,
                                        ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getOnboardingList() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVE_ACTIVE_URL, ORGANIZATION_ID, INITIATIVE_ID + "/onboardings"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getInitiativeBeneficiaryDetail_statusOk() throws Exception {

        //create Dummy Initiative
        Initiative initiative = createStep5Initiative();
        InitiativeDetailDTO initiativeDetailDTO = createInitiativeDetailDTO();
        Locale acceptLanguage = Locale.ITALIAN;

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDetailDTO(initiative,acceptLanguage)).thenReturn(initiativeDetailDTO);
        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiativeBeneficiaryDetail(anyString(),any())).thenReturn(initiativeDetailDTO);

        //The MVC perform should perform the API by returning the response based on the Service previously mocked.
        mvc.perform(
                        MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVE_BENEFICIARY_DETAIL_URL, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    private List<OrganizationDTO> createOrganizationDTOList() {
        return IntStream.range(0, 4).mapToObj(this::createOrganizationDTO).toList();
    }

    private OrganizationDTO createOrganizationDTO(int bias) {
        return OrganizationDTO.builder()
                .organizationId("organizationId_%d".formatted(bias))
                .organizationName("organizationName_%d".formatted(bias))
                .build();
    }

    private InitiativeOrganizationInfoDTO createInitiativeOrganizationInfoDTO() {
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = new InitiativeOrganizationInfoDTO();
        initiativeOrganizationInfoDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeOrganizationInfoDTO.setOrganizationVat(ORGANIZATION_VAT);
        initiativeOrganizationInfoDTO.setOrganizationUserRole(ROLE);
        return initiativeOrganizationInfoDTO;
    }

    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO() {
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .type("rewardValue")
                .build();
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOValid() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
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

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTO() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(initiativeRewardRuleDTO);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = createInitiativeTrxConditionsDTOValid();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    private Response responseStub() {
        return Response.builder()
                .request(
                        Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(), new byte[0], Charset.defaultCharset(), new RequestTemplate()))
                .status(400)
                .reason("Bad Request")
//                .headers(Collections.emptyMap()
                .build();
    }

    /*
     * ############### Step 1 ###############
     */

    private Initiative createStep1Initiative() {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setStatus("DRAFT");
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

    InitiativeDTO createStep1InitiativeDTO() {
        return InitiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName("initiativeName1")
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    InitiativeAdditionalDTO createStep1InitiativeAdditionalDTO() {
        return createInitiativeAdditionalDTO();
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

    private Initiative createStep2Initiative(Boolean beneficiaryKnown) {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral(beneficiaryKnown));
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(Boolean beneficiaryKnown) {
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
        initiativeGeneral.setRankingEnabled(false);
        initiativeGeneral.setDescriptionMap(language);
        return initiativeGeneral;
    }

    private InitiativeDTO createStep2InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO(false));
        return initiativeDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(Boolean beneficiaryKnown) {
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
        initiativeGeneralDTO.setRankingEnabled(false);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    /*
     * ############### Step 3 ###############
     */

    private Initiative createStep3Initiative() {
        Initiative initiative = createStep2Initiative(false);
        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
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

    private InitiativeDTO createStep3InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        initiativeDTO.setBeneficiaryRule(createInitiativeBeneficiaryRuleDTO());
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

    /*
     * ############### Step 4 ###############
     */

    private Initiative createStep4Initiative() {
        return createStep3Initiative();
    }

    private InitiativeDTO createStep4InitiativeDTO() {
        return createStep3InitiativeDTO();
    }


    /*
     * ############### Step 5 ###############
     */

    private Initiative createStep5Initiative() {
        Initiative initiative = createStep4Initiative();
        initiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        return initiative;
    }

    private InitiativeDTO createStep5InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep4InitiativeDTO();
        initiativeDTO.setRefundRule(createRefundRuleDTOValidWithTimeParameter());
        return initiativeDTO;
    }

    private AccumulatedAmountDTO createAccumulatedAmountDTOValid() {
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
    }

    private TimeParameterDTO createTimeParameterDTO_Valid() {
        TimeParameterDTO timeParameterDTO = new TimeParameterDTO();
        timeParameterDTO.setTimeType(TimeParameterDTO.TimeTypeEnum.CLOSED);
        return timeParameterDTO;
    }

    private RefundAdditionalInfoDTO createAdditionalInfoDTOValid() {
        RefundAdditionalInfoDTO refundAdditionalInfoDTO = new RefundAdditionalInfoDTO();
        refundAdditionalInfoDTO.setIdentificationCode("B002");
        return refundAdditionalInfoDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameter() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();

        refundRuleDTO.setOrganizationName(ORGANIZATION_NAME);
        refundRuleDTO.setOrganizationUserRole(ROLE);

        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTO_Valid());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithAccumulatedAmount() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();

        refundRuleDTO.setOrganizationName(ORGANIZATION_NAME);
        refundRuleDTO.setOrganizationUserRole(ROLE);

        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTOValid());
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private AccumulatedAmount createAccumulatedAmount_Valid() {
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccumulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThreshold(BigDecimal.valueOf(100000));
        return amount;
    }

    private TimeParameter createTimeParameter_Valid() {
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    private AdditionalInfo createAdditionalInfo_Valid() {
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }

    private InitiativeRefundRuleDTO createRefundWithBothRefundNullNotValid_ko() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(null);
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithAccumulatedAmountDTO_NotValid_ko() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTO_NotValid_ko());
        refundRuleDTO.setTimeParameter(null);
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithAccumulatedAmountAndTimeParameter_NotValid() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTO_NotValid_ko());
        refundRuleDTO.setTimeParameter(createTimeParameterDTO_Valid());
        return refundRuleDTO;
    }

    private AccumulatedAmountDTO createAccumulatedAmountDTO_NotValid_ko() {
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.BUDGET_EXHAUSTED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
    }

    private InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmount_Valid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfo_Valid());
        return refundRule;
    }

    private InitiativeRefundRule createRefundRuleValidWithTimeParameter() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameter_Valid());
        refundRule.setAdditionalInfo(createAdditionalInfo_Valid());
        return refundRule;
    }

    private Initiative createInitiativeOnlyRefundRule() {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }

    private InitiativeDetailDTO createInitiativeDetailDTO() {
        InitiativeDetailDTO initiativeDetailDTO = new InitiativeDetailDTO();
        initiativeDetailDTO.setInitiativeName("TEST");
        initiativeDetailDTO.setStatus("APPROVED");
        initiativeDetailDTO.setDescription("test test");
        initiativeDetailDTO.setEndDate(LocalDate.now());
        initiativeDetailDTO.setRankingStartDate(LocalDate.now());
        initiativeDetailDTO.setRankingEndDate(LocalDate.now().plusDays(40));
        initiativeDetailDTO.setRewardRule(createRewardRuleDTO(false));
        initiativeDetailDTO.setRefundRule(null);
        initiativeDetailDTO.setPrivacyLink("privacy.it");
        initiativeDetailDTO.setTcLink("tc.it");
        initiativeDetailDTO.setLogoURL("logo.png");
        initiativeDetailDTO.setUpdateDate(LocalDateTime.now());
        initiativeDetailDTO.setServiceId("SERVICE_ID");
        return initiativeDetailDTO;
    }

    private InitiativeRewardRuleDTO createRewardRuleDTO(boolean isRewardFixedValue) {
        if (isRewardFixedValue) {
            //TODO Aggiungere RewardValue
            return null;
        } else {
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

}