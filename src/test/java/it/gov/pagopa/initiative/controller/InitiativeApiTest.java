package it.gov.pagopa.initiative.controller;

<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.INITIATIVE_INVALID_LOCALE_FORMAT;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.INITIATIVE_ROLLBACK_TO_PREVIOUS_STATUS;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_VALID;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.ACCUMULATED_AMOUNT_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.SOMETHING_WRONG_WITH_THE_REFUND_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.ADMIN;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.PAGOPA_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
=======
>>>>>>> develop
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import it.gov.pagopa.common.web.dto.ErrorDTO;
import it.gov.pagopa.common.web.exception.ClientExceptionWithBody;
import it.gov.pagopa.initiative.config.ServiceExceptionConfig;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
import it.gov.pagopa.initiative.dto.AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems;
import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.dto.BeneficiaryRankingPageDTO;
import it.gov.pagopa.initiative.dto.ChannelDTO;
import it.gov.pagopa.initiative.dto.FilterOperatorEnum;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.dto.InitiativeBeneficiaryRuleDTO;
import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.dto.InitiativeDataDTO;
import it.gov.pagopa.initiative.dto.InitiativeDetailDTO;
import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.InitiativeRewardAndTrxRulesDTO;
import it.gov.pagopa.initiative.dto.InitiativeSummaryDTO;
import it.gov.pagopa.initiative.dto.LogoDTO;
import it.gov.pagopa.initiative.dto.OnboardingDTO;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.dto.SelfCriteriaBoolDTO;
import it.gov.pagopa.initiative.dto.SelfCriteriaMultiDTO;
=======
import it.gov.pagopa.initiative.dto.*;
>>>>>>> develop
import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.exception.custom.InitiativeStatusNotValidException;
import it.gov.pagopa.initiative.exception.custom.OrgPermissionException;
import it.gov.pagopa.initiative.mapper.InitiativeDTOsToModelMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.service.InitiativeService;
import it.gov.pagopa.initiative.service.OrganizationService;
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
=======
>>>>>>> develop
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
=======
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
>>>>>>> develop
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
=======
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.INITIATIVE_INVALID_LOCALE_FORMAT;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.ACCUMULATED_AMOUNT_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.SOMETHING_WRONG_WITH_THE_REFUND_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.ADMIN;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.PAGOPA_ADMIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
>>>>>>> develop

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.initiative.conditions.notifyRE=true",
                "app.initiative.conditions.notifyIO=true",
                "app.initiative.conditions.notifyInternal=true",
                "app.initiative.ranking.gracePeriod=10"
        })
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
@WebMvcTest(
        value = {InitiativeApi.class, ServiceExceptionConfig.class},
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
=======
@WebMvcTest(value = {
        InitiativeApi.class, ServiceExceptionConfig.class}, excludeAutoConfiguration = { UserDetailsServiceAutoConfiguration .class , SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
>>>>>>> develop
class InitiativeApiTest {

    private static final String INITIATIVE_ID = "Id1";
    private static final String ORGANIZATION_ID = "O1";
    private static final String SERVICE_ID = "service1";

    private static final String BASE_URL = "http://localhost:8080/idpay";
    private static final String ROLE = "admin";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    private static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    private static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";

<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
    private static final String GET_INITIATIVES_SUMMARY_URL = "/organization/%s/initiative/summary";
    private static final String GET_INITIATIVES_ISSUER = "/initiatives";
    private static final String GET_INITIATIVES_MIL = "/mil/initiatives";
    private static final String GET_INITIATIVE_ACTIVE_URL = "/organization/%s/initiative/%s";
    private static final String GET_INITIATIVE_ID_FROM_SERVICE_ID = "/initiative?serviceId=%s";
    private static final String GET_INITIATIVE_BENEFICIARY_VIEW_URL = "/initiative/%s/beneficiary/view";
    private static final String POST_INITIATIVE_ADDITIONAL_INFO_URL = "/organization/%s/initiative/info";
    private static final String GET_RANKING_LIST = "/organization/%s/initiative/%s/ranking/exports";
    private static final String PUT_INITIATIVE_ADDITIONAL_INFO_URL = "/organization/%s/initiative/%s/info";
    private static final String PUT_INITIATIVE_GENERAL_INFO_URL = "/organization/%s/initiative/%s/general";
    private static final String PUT_TRX_REWARD_RULE_URL = "/organization/%s/initiative/%s/reward";
    private static final String PUT_INITIATIVE_REFUND_RULES_INFO_URL = "/organization/%s/initiative/%s/refund";
    private static final String PUT_INITIATIVE_BENEFICIARY_RULES_URL = "/organization/%s/initiative/%s/beneficiary?role=%s";
    private static final String PUT_INITIATIVE_BENEFICIARY_RULES_DRAFT_URL = "/organization/%s/initiative/%s/beneficiary/draft";
    private static final String PUT_INITIATIVE_STATUS_APPROVED_URL = "/organization/%s/initiative/%s/approved";
    private static final String PUT_INITIATIVE_TO_CHECK_STATUS_URL = "/organization/%s/initiative/%s/rejected";
    private static final String PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL = "/organization/%s/initiative/%s/published?role=%s";
    private static final String LOGICALLY_DELETE_INITIATIVE_URL = "/organization/%s/initiative/%s";
    private static final String GET_INITIATIVE_BENEFICIARY_DETAIL_URL = "/initiative/%s/detail";
    private static final String DELETE_INITIATIVE_URL = "/initiative/%s";

    @MockitoBean
    private InitiativeService initiativeService;

    @MockitoBean
    private OrganizationService organizationService;

    @MockitoBean
    private InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @MockitoBean
    private InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    @Autowired
    private InitiativeApiController initiativeApiController;
=======
    @MockitoBean
    InitiativeService initiativeService;
    @MockitoBean
    OrganizationService organizationService;

    @Autowired
    InitiativeApiController initiativeApiController;

    @MockitoBean
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @MockitoBean
    InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    @MockitoBean
    InitiativeGeneralDTO initiativeGeneralDTO;
>>>>>>> develop

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @ParameterizedTest
    @ValueSource(strings = {PAGOPA_ADMIN, ADMIN})
    void whenGetInitiativeSummary_thenStatusOk(String role) throws Exception {
        List<Initiative> initiatives = List.of(createStep1Initiative(), createStep1Initiative());
        List<InitiativeSummaryDTO> summaryDTOs = List.of(createStep1InitiativeSummaryDTO(), createStep1InitiativeSummaryDTO());

        when(initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, role)).thenReturn(initiatives);
        when(initiativeModelToDTOMapper.toInitiativeSummaryDTOList(initiatives)).thenReturn(summaryDTOs);

<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        mvc.perform(
                        get(BASE_URL + String.format(GET_INITIATIVES_SUMMARY_URL, ORGANIZATION_ID))
                                .queryParam("role", role)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(summaryDTOs)))
                .andDo(print());
=======
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
>>>>>>> develop
    }

    @Test
    void getInitiativesIssuer_Ok() throws Exception {
        List<Initiative> initiatives = List.of(createStep2Initiative(false), createStep2Initiative(false));
        when(initiativeService.getPublishedInitiativesList()).thenReturn(initiatives);
        when(initiativeModelToDTOMapper.toInitiativeIssuerDTOList(initiatives)).thenReturn(List.of());

        mvc.perform(
                        get(BASE_URL + GET_INITIATIVES_ISSUER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getInitiativesMIL_Ok() throws Exception {
        List<Initiative> initiatives = List.of(createStep2Initiative(false), createStep2Initiative(false));
        when(initiativeService.getPublishedInitiativesList()).thenReturn(initiatives);
        when(initiativeModelToDTOMapper.toInitiativeListMilDTO(initiatives)).thenReturn(List.of());

        mvc.perform(
                        get(BASE_URL + GET_INITIATIVES_MIL)
                                .header("x-user-id", "USER_ID_TEST")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getInitiativeDetail_statusOk() throws Exception {
        Initiative initiative = createStep2Initiative(false);
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, null)).thenReturn(initiative);
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative, true)).thenReturn(initiativeDTO);

        mvc.perform(
                        get(BASE_URL + String.format(GET_INITIATIVE_ACTIVE_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testAddLogo() throws IOException {
        InitiativeService localInitiativeService = mock(InitiativeService.class);
        OrganizationService localOrganizationService = mock(OrganizationService.class);
        InitiativeModelToDTOMapper localModelMapper = mock(InitiativeModelToDTOMapper.class);
        InitiativeDTOsToModelMapper localDtoMapper = mock(InitiativeDTOsToModelMapper.class);

        when(localInitiativeService.storeInitiativeLogo(any(), any(), any(), any(), any())).thenReturn(new LogoDTO());

        InitiativeApiController controller = new InitiativeApiController(
                true,
                true,
                true,
                localInitiativeService,
                localOrganizationService,
                localModelMapper,
                localDtoMapper);

        ResponseEntity<LogoDTO> response = controller.addLogo(
                "42",
                "42",
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAA".getBytes(StandardCharsets.UTF_8))));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        verify(localInitiativeService).storeInitiativeLogo(any(), any(), any(), any(), any());
    }

    @Test
    void getInitiativeIdFromServiceId_statusOk() throws Exception {
        Initiative initiative = createStep1Initiative();
        InitiativeDataDTO dto = new InitiativeDataDTO();
        dto.setInitiativeId(INITIATIVE_ID);

        when(initiativeService.getInitiativeIdFromServiceId(SERVICE_ID)).thenReturn(initiative);
        when(initiativeModelToDTOMapper.toInitiativeDataDTO(any(Initiative.class), any(Locale.class))).thenReturn(dto);

        mvc.perform(
                        get(BASE_URL + String.format(GET_INITIATIVE_ID_FROM_SERVICE_ID, SERVICE_ID))
                                .header("Accept-Language", "it")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getInitiativeIdFromServiceId_Exception() {
        try {
            initiativeApiController.getInitiativeIdFromServiceId(Locale.forLanguageTag("xxxx"), SERVICE_ID);
        } catch (ClientExceptionWithBody e) {
            assertEquals(INITIATIVE_INVALID_LOCALE_FORMAT, e.getCode());
            assertEquals(
                    String.format("The initiative could not be found for the current serviceId [%s], due to invalid locale format", SERVICE_ID),
                    e.getMessage());
        }
    }

    @Test
    void saveInitiativeServiceInfo_statusCreated() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep1Initiative();
        InitiativeAdditionalDTO request = createStep1InitiativeAdditionalDTO();
        InitiativeDTO responseDto = createStep1InitiativeDTO();
=======
        //objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();

        InitiativeAdditionalDTO initiativeAdditionalDTO = createStep1InitiativeAdditionalDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeService.insertInitiative(step1Initiative, ORGANIZATION_ID, ORGANIZATION_NAME, ROLE)).thenReturn(step1Initiative);
>>>>>>> develop

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        when(initiativeService.insertInitiative(initiative, ORGANIZATION_ID, ORGANIZATION_NAME, ROLE)).thenReturn(initiative);
        when(initiativeModelToDTOMapper.toDtoOnlyId(initiative)).thenReturn(responseDto);

        mvc.perform(
                        post(BASE_URL + String.format(POST_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void saveInitiativeServiceInfo_WebUrlContactValid() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep1Initiative();
        initiative.getAdditionalInfo().getChannels().get(0).setType(Channel.TypeEnum.WEB);
        initiative.getAdditionalInfo().getChannels().get(0).setContact("https://www.google.it");
=======

        Initiative step1Initiative = createStep1Initiative();
        step1Initiative.getAdditionalInfo().getChannels().get(0).setType(Channel.TypeEnum.WEB);
        step1Initiative.getAdditionalInfo().getChannels().get(0).setContact("https://www.google.it");

        InitiativeAdditionalDTO initiativeAdditionalDTO = createStep1InitiativeAdditionalDTO();
        initiativeAdditionalDTO.getChannels().get(0).setType(ChannelDTO.TypeEnum.WEB);
        initiativeAdditionalDTO.getChannels().get(0).setContact("https://www.google.it");
>>>>>>> develop

        InitiativeAdditionalDTO request = createStep1InitiativeAdditionalDTO();
        request.getChannels().get(0).setType(ChannelDTO.TypeEnum.WEB);
        request.getChannels().get(0).setContact("https://www.google.it");

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        when(initiativeService.insertInitiative(initiative, ORGANIZATION_ID, ORGANIZATION_NAME, ROLE)).thenReturn(initiative);
        when(initiativeModelToDTOMapper.toDtoOnlyId(initiative)).thenReturn(createStep1InitiativeDTO());

        mvc.perform(
                        post(BASE_URL + String.format(POST_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void saveInitiativeServiceInfo_WebUrlContactNotValid() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        InitiativeAdditionalDTO request = createStep1InitiativeAdditionalDTO();
        request.getChannels().get(0).setType(ChannelDTO.TypeEnum.WEB);
        request.getChannels().get(0).setContact("http://www.google.it");
=======


        InitiativeAdditionalDTO initiativeAdditionalDTO = createStep1InitiativeAdditionalDTO();
        initiativeAdditionalDTO.getChannels().get(0).setType(ChannelDTO.TypeEnum.WEB);
        initiativeAdditionalDTO.getChannels().get(0).setContact("http://www.google.it");
>>>>>>> develop

        mvc.perform(
                        post(BASE_URL + String.format(POST_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateInitiativeGeneralInfo_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep2Initiative(false);
        InitiativeGeneralDTO request = createInitiativeGeneralDTO(false);
=======


        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO(beneficiaryKnown);

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeGeneralDTO)).thenReturn(step2Initiative);
>>>>>>> develop

        when(initiativeDTOsToModelMapper.toInitiative(any(InitiativeGeneralDTO.class))).thenReturn(initiative);
        doNothing().when(initiativeService).updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE, false);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_GENERAL_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateInitiativeAdditionalInfo_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep1Initiative();
        InitiativeAdditionalDTO request = createInitiativeAdditionalDTO();
=======


        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO)).thenReturn(step1Initiative);
>>>>>>> develop

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        doNothing().when(initiativeService).updateInitiativeAdditionalInfo(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateInitiativeGeneralInfoDraft_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep2Initiative(false);
        InitiativeGeneralDTO request = createInitiativeGeneralDTO(false);
=======


        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO)).thenReturn(step1Initiative);
>>>>>>> develop

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        doNothing().when(initiativeService).updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE, true);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_GENERAL_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID) + "/draft")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateInitiativeBeneficiary_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep2Initiative(false);
        InitiativeBeneficiaryRule rule = createInitiativeBeneficiaryRule();
        InitiativeBeneficiaryRuleDTO request = createInitiativeBeneficiaryRuleDTO();

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(any(InitiativeBeneficiaryRuleDTO.class))).thenReturn(rule);
        doNothing().when(initiativeService).updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, rule, ROLE, false);
=======


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

        // Base test
        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    //@Test
    void updateInitiativeBeneficiaryISEEandNumber_statusNoContent() throws Exception {

>>>>>>> develop

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
    void updateInitiativeBeneficiary_statusBadRequest_whenBeneficiaryKnown() throws Exception {
        Initiative initiative = createStep2Initiative(true);
        InitiativeBeneficiaryRuleDTO request = createInitiativeBeneficiaryRuleDTO();
=======
    void updateInitiativeBeneficiary_statusBadRequest() throws Exception {


        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = true;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRuleDTO.setOrganizationName(ORGANIZATION_NAME);
        initiativeBeneficiaryRuleDTO.setOrganizationUserRole(ROLE);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);
>>>>>>> develop

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateInitiativeBeneficiary_statusBadRequest_notValidCodeWithISEEInput() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep2Initiative(false);
        InitiativeBeneficiaryRuleDTO request = createInitiativeBeneficiaryRuleDTO();
        request.getAutomatedCriteria().get(0).setCode("ISEE");
=======

>>>>>>> develop

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateTrxAndRewardRules_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep1Initiative();
        InitiativeRewardAndTrxRulesDTO request = createInitiativeRewardAndTrxRulesDTO();
=======


        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO)).thenReturn(step1Initiative);
>>>>>>> develop

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        doNothing().when(initiativeService).updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE, false);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_TRX_REWARD_RULE_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateTrxAndRewardRulesDraft_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        Initiative initiative = createStep1Initiative();
        InitiativeRewardAndTrxRulesDTO request = createInitiativeRewardAndTrxRulesDTO();
=======


        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO)).thenReturn(step1Initiative);
>>>>>>> develop

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        doNothing().when(initiativeService).updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE, true);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_TRX_REWARD_RULE_URL, ORGANIZATION_ID, INITIATIVE_ID) + "/draft")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateInitiativeRefundRule_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        InitiativeRefundRuleDTO request = createRefundRuleDTOValidWithAccumulatedAmount();
=======


        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

>>>>>>> develop
        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        doNothing().when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, true);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateRefundRule_PUT_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        InitiativeRefundRuleDTO request = createRefundRuleDTOValidWithAccumulatedAmount();
=======


        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

>>>>>>> develop
        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        doThrow(new InitiativeStatusNotValidException(
                "Initiative [%s] with status [%s] is unprocessable for status not valid"
                        .formatted(initiative.getInitiativeId(), initiative.getStatus())))
                .when(initiativeService)
                .updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, true);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateInitiativeRefundRuleDraft_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        InitiativeRefundRuleDTO request = createRefundRuleDTOValidWithAccumulatedAmount();
=======


        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

>>>>>>> develop
        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeDTOsToModelMapper.toInitiative(request)).thenReturn(initiative);
        doNothing().when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, false);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID) + "/draft")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateInitiativeBeneficiaryDraft_statusNoContent() throws Exception {
<<<<<<< UPBE-682-design-review-verifica-gestione-isee_analisi
        InitiativeBeneficiaryRuleDTO request = createInitiativeBeneficiaryRuleDTO();
        InitiativeBeneficiaryRule rule = createInitiativeBeneficiaryRule();
=======


        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        createStep2Initiative(beneficiaryKnown);
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);
>>>>>>> develop

        when(initiativeDTOsToModelMapper.toBeneficiaryRule(request)).thenReturn(rule);
        doNothing().when(initiativeService).updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, rule, ROLE, true);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_DRAFT_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateInitiativeStatusApproved_statusNoContent() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();
        doNothing().when(initiativeService).updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_STATUS_APPROVED_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void updateInitiativeStatusApproved_roleException() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();

        doThrow(new OrgPermissionException("Message"))
                .when(initiativeService)
                .updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_STATUS_APPROVED_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void updateInitiativeStatusToCheck_roleException() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();

        doThrow(new OrgPermissionException("Message"))
                .when(initiativeService)
                .updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_TO_CHECK_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void updateInitiativeToCheckStatus_statusNoContent() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();

        doNothing().when(initiativeService).updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_TO_CHECK_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void getInitiativeBeneficiaryView_statusOk() throws Exception {
        Initiative initiative = createStep5Initiative();
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO();

        when(initiativeService.getInitiativeBeneficiaryView(INITIATIVE_ID)).thenReturn(initiative);
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative, false)).thenReturn(initiativeDTO);

        mvc.perform(
                        get(BASE_URL + String.format(GET_INITIATIVE_BENEFICIARY_VIEW_URL, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void anyUpdate_PUT_whenBodyRequestIsNotValid_then400BadRequest_MethodArgumentNotValidException() throws Exception {
        InitiativeRefundRuleDTO request = createRefundRuleWithAccumulatedAmountDTO_NotValid_ko();

        MvcResult res = mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals("INVALID_REQUEST", error.getCode());
        assertTrue(error.getMessage().contains(ACCUMULATED_AMOUNT_TYPE));
    }

    @Test
    void PUT_updateInitiativeStatusToCheck_whenStatusIsNotInRevision() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();

        doThrow(new InitiativeStatusNotValidException(
                "The status of initiative [%s] is not IN_REVISION".formatted(INITIATIVE_ID)))
                .when(initiativeService)
                .updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        MvcResult res = mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_TO_CHECK_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(INITIATIVE_STATUS_NOT_VALID, error.getCode());
        assertEquals("The status of initiative [%s] is not IN_REVISION".formatted(INITIATIVE_ID), error.getMessage());
    }

    @Test
    void DEL_logicallyDeleteInitiative_whenCurrentDeletedIsFalse_thenNoContent() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();
        doNothing().when(initiativeService).logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        mvc.perform(
                        delete(BASE_URL + String.format(LOGICALLY_DELETE_INITIATIVE_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void anyUpdate_PUT_whenBodyRequestIsNotValid_then400BadRequest_MethodArgumentNotValidExceptionElseCase() throws Exception {
        InitiativeRefundRuleDTO request = createRefundRuleWithAccumulatedAmountAndTimeParameter_NotValid();

        MvcResult res = mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals("INVALID_REQUEST", error.getCode());
        assertTrue(error.getMessage().contains(SOMETHING_WRONG_WITH_THE_REFUND_TYPE));
    }

    @Test
    void when_PUT_updateInitiativePublishedStatus_ToIO_then204() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();
        Initiative initiative = createStep5Initiative();

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);
        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));
        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));
        when(initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(any(Initiative.class), any(InitiativeOrganizationInfoDTO.class)))
                .thenReturn(initiative);
        doNothing().when(initiativeService).sendEmailToPagoPA(any(), anyString(), anyString());
        doNothing().when(initiativeService).sendEmailToCurrentOrg(any(), anyString(), anyString());
        doNothing().when(initiativeService).initializeStatistics(INITIATIVE_ID, ORGANIZATION_ID);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void when_PUT_updateInitiativePublishedStatus_NotToIO_then204() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();
        Initiative initiative = createStep5Initiative();
        initiative.getAdditionalInfo().setServiceIO(false);

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);
        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));
        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));
        doNothing().when(initiativeService).sendEmailToPagoPA(any(), anyString(), anyString());
        doNothing().when(initiativeService).sendEmailToCurrentOrg(any(), anyString(), anyString());
        doNothing().when(initiativeService).initializeStatistics(INITIATIVE_ID, ORGANIZATION_ID);

        mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void givenRuleEngineError_when_PUT_updateInitiativePublishedStatus_thenThrowException() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();
        Initiative initiative = createStep5Initiative();

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);
        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));
        doThrow(new KafkaException()).when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        MvcResult res = mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(INITIATIVE_ROLLBACK_TO_PREVIOUS_STATUS, error.getCode());
    }

    @Test
    void givenIOBackEndError_when_PUT_updateInitiativePublishedStatus_thenThrowException() throws Exception {
        InitiativeOrganizationInfoDTO request = createInitiativeOrganizationInfoDTO();
        Initiative initiative = createStep5Initiative();

        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);
        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));
        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));
        doThrow(FeignException.errorStatus("Bad Request", responseStub()))
                .when(initiativeService)
                .sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(any(Initiative.class), any(InitiativeOrganizationInfoDTO.class));

        MvcResult res = mvc.perform(
                        put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID, ROLE))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(INITIATIVE_ROLLBACK_TO_PREVIOUS_STATUS, error.getCode());
        assertEquals("Something gone wrong while notify Initiative [%s] for publishing".formatted(initiative.getInitiativeId()), error.getMessage());
    }

    @Test
    void getListOfOrganization() throws Exception {
        List<OrganizationDTO> organizations = createOrganizationDTOList();

        when(organizationService.getOrganizationList(ADMIN)).thenReturn(organizations);
        when(organizationService.getOrganizationList(PAGOPA_ADMIN)).thenReturn(organizations);
        when(organizationService.getOrganizationList("default")).thenReturn(null);

        MvcResult resultAdmin = mvc.perform(
                        get(BASE_URL + "/organizations")
                                .param("role", ADMIN)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult resultPagoPaAdmin = mvc.perform(
                        get(BASE_URL + "/organizations")
                                .param("role", PAGOPA_ADMIN)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult resultDefault = mvc.perform(
                        get(BASE_URL + "/organizations")
                                .param("role", "default")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(organizations);
        assertEquals(expected, resultAdmin.getResponse().getContentAsString());
        assertEquals(expected, resultPagoPaAdmin.getResponse().getContentAsString());
        assertEquals("", resultDefault.getResponse().getContentAsString());
    }

    @Test
    void getOrganization() throws Exception {
        OrganizationDTO organization = createOrganizationDTO(1);
        when(organizationService.getOrganization(organization.getOrganizationId())).thenReturn(organization);

        MvcResult result = mvc.perform(
                        get(BASE_URL + "/organizations/{organizationId}", organization.getOrganizationId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(organization), result.getResponse().getContentAsString());
    }

    @Test
    void testGetRankingList() throws Exception {
        when(initiativeService.getRankingList(anyString(), anyString(), any(), any(), any()))
                .thenReturn(new BeneficiaryRankingPageDTO());

        mvc.perform(
                        get(BASE_URL + String.format(GET_RANKING_LIST, ORGANIZATION_ID, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getOnboardingList() throws Exception {
        when(initiativeService.getOnboardingStatusList(anyString(), anyString(), any(), any(), any(), any(), any()))
                .thenReturn(new OnboardingDTO());

        mvc.perform(
                        get(BASE_URL + String.format(GET_INITIATIVE_ACTIVE_URL, ORGANIZATION_ID, INITIATIVE_ID + "/onboardings"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getInitiativeBeneficiaryDetail_statusOk() throws Exception {
        InitiativeDetailDTO detailDTO = createInitiativeDetailDTO();

        when(initiativeService.getInitiativeBeneficiaryDetail(anyString(), any(), anyBoolean())).thenReturn(detailDTO);

        mvc.perform(
                        get(BASE_URL + String.format(GET_INITIATIVE_BENEFICIARY_DETAIL_URL, INITIATIVE_ID))
                                .header("Accept-Language", "it")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteInitiative() throws Exception {
        doNothing().when(initiativeService).deleteInitiative(INITIATIVE_ID);

        mvc.perform(
                        delete(BASE_URL + String.format(DELETE_INITIATIVE_URL, INITIATIVE_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    private InitiativeSummaryDTO createStep1InitiativeSummaryDTO() {
        return InitiativeSummaryDTO.builder()
                .initiativeId("initiativeId")
                .initiativeName("initiativeName")
                .status(InitiativeConstants.Status.PUBLISHED)
                .build();
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
        InitiativeOrganizationInfoDTO dto = new InitiativeOrganizationInfoDTO();
        dto.setOrganizationName(ORGANIZATION_NAME);
        dto.setOrganizationVat(ORGANIZATION_VAT);
        dto.setOrganizationUserRole(ROLE);
        return dto;
    }

    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO() {
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .type("rewardValue")
                .build();
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOValid() {
        InitiativeTrxConditionsDTO dto = new InitiativeTrxConditionsDTO();

        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig = new DayOfWeekDTO.DayConfig();

        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.MONDAY);
        daysOfWeek.add(DayOfWeek.THURSDAY);
        dayConfig.setDaysOfWeek(daysOfWeek);

        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval = new DayOfWeekDTO.Interval();
        interval.setStartTime(LocalTime.of(6, 0, 0));
        interval.setEndTime(LocalTime.of(12, 0, 0));
        intervals.add(interval);
        dayConfig.setIntervals(intervals);
        dayConfigs.add(dayConfig);

        dto.setDaysOfWeek(new DayOfWeekDTO(dayConfigs));

        ThresholdDTO threshold = new ThresholdDTO();
        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);
        dto.setThreshold(threshold);

        TrxCountDTO trxCount = new TrxCountDTO();
        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);
        dto.setTrxCount(trxCount);

        MccFilterDTO mccFilter = new MccFilterDTO();
        mccFilter.setAllowedList(true);
        mccFilter.setValues(Set.of("123", "456"));
        dto.setMccFilter(mccFilter);

        RewardLimitsDTO daily = new RewardLimitsDTO();
        daily.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        daily.setRewardLimit(BigDecimal.valueOf(100));

        RewardLimitsDTO monthly = new RewardLimitsDTO();
        monthly.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        monthly.setRewardLimit(BigDecimal.valueOf(3000));

        dto.setRewardLimits(List.of(daily, monthly));
        return dto;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTO() {
        InitiativeRewardAndTrxRulesDTO dto = new InitiativeRewardAndTrxRulesDTO();
        dto.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        dto.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        dto.setTrxRule(createInitiativeTrxConditionsDTOValid());
        dto.setOrganizationUserRole(ROLE);
        return dto;
    }

    private Response responseStub() {
        return Response.builder()
                .request(Request.create(
                        Request.HttpMethod.POST,
                        "url",
                        Collections.emptyMap(),
                        new byte[0],
                        Charset.defaultCharset(),
                        new RequestTemplate()))
                .status(400)
                .reason("Bad Request")
                .build();
    }

    private Initiative createStep1Initiative() {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setOrganizationName(ORGANIZATION_NAME);
        initiative.setStatus("DRAFT");
        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional additional = new InitiativeAdditional();
        additional.setServiceIO(true);
        additional.setServiceId(SERVICE_ID);
        additional.setServiceName("serviceName");
        additional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        additional.setDescription("Description");
        additional.setPrivacyLink("https://www.google.it");
        additional.setTcLink("https://www.google.it");

        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.EMAIL);
        channel.setContact("contact");
        additional.setChannels(new ArrayList<>(List.of(channel)));

        return additional;
    }

    private InitiativeDTO createStep1InitiativeDTO() {
        return InitiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName("initiativeName1")
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .additionalInfo(createInitiativeAdditionalDTO())
                .build();
    }

    private InitiativeAdditionalDTO createStep1InitiativeAdditionalDTO() {
        return createInitiativeAdditionalDTO();
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO dto = new InitiativeAdditionalDTO();
        dto.setOrganizationName(ORGANIZATION_NAME);
        dto.setOrganizationUserRole(ROLE);
        dto.setServiceIO(true);
        dto.setServiceId("serviceId");
        dto.setServiceName("serviceName");
        dto.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        dto.setDescription("Description");
        dto.setPrivacyLink("https://www.google.it");
        dto.setTcLink("https://www.google.it");

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.EMAIL);
        channelDTO.setContact("contact");
        dto.setChannels(new ArrayList<>(List.of(channelDTO)));

        return dto;
    }

    private Initiative createStep2Initiative(Boolean beneficiaryKnown) {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral(beneficiaryKnown));
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(Boolean beneficiaryKnown) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");

        InitiativeGeneral general = new InitiativeGeneral();
        general.setBeneficiaryKnown(beneficiaryKnown);
        general.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        general.setBudgetCents(100000000000L);

        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);

        general.setRankingStartDate(rankingStartDate);
        general.setRankingEndDate(rankingEndDate);
        general.setStartDate(startDate);
        general.setEndDate(endDate);
        general.setRankingEnabled(false);
        general.setDescriptionMap(language);
        return general;
    }

    private InitiativeDTO createStep2InitiativeDTO() {
        InitiativeDTO dto = createStep1InitiativeDTO();
        dto.setGeneral(createInitiativeGeneralDTO(false));
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(Boolean beneficiaryKnown) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");

        InitiativeGeneralDTO dto = new InitiativeGeneralDTO();
        dto.setOrganizationUserRole(ROLE);
        dto.setBeneficiaryKnown(beneficiaryKnown);
        dto.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);

        dto.setBudget(BigDecimal.valueOf(1000000000L));

        dto.setBeneficiaryBudgetFixed(new BigDecimal(1000));

        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);

        dto.setRankingStartDate(rankingStartDate);
        dto.setRankingEndDate(rankingEndDate);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setRankingEnabled(false);
        dto.setDescriptionMap(language);

        return dto;
    }

    private Initiative createStep3Initiative() {
        Initiative initiative = createStep2Initiative(false);
        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
        return initiative;
    }

    private InitiativeBeneficiaryRule createInitiativeBeneficiaryRule() {
        InitiativeBeneficiaryRule rule = new InitiativeBeneficiaryRule();

        SelfCriteriaBool selfCriteriaBool = new SelfCriteriaBool();
        selfCriteriaBool.set_type(TypeBoolEnum.BOOLEAN);
        selfCriteriaBool.setCode("B001");
        selfCriteriaBool.setDescription("Desc_bool");
        selfCriteriaBool.setValue(true);

        SelfCriteriaMulti selfCriteriaMulti = new SelfCriteriaMulti();
        selfCriteriaMulti.set_type(TypeMultiEnum.MULTI);
        selfCriteriaMulti.setCode("B001");
        selfCriteriaMulti.setDescription("Desc_Multi");
        selfCriteriaMulti.setValue(List.of("valore1", "valore2"));

        List<ISelfDeclarationCriteria> selfDeclarationCriteria = new ArrayList<>();
        selfDeclarationCriteria.add(selfCriteriaBool);
        selfDeclarationCriteria.add(selfCriteriaMulti);
        rule.setSelfDeclarationCriteria(selfDeclarationCriteria);

        AutomatedCriteria automatedCriteria = new AutomatedCriteria();
        automatedCriteria.setAuthority("Authority_ISEE");
        automatedCriteria.setCode("Code_ISEE");
        automatedCriteria.setField("true");
        automatedCriteria.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteria.setValue("value");

        rule.setAutomatedCriteria(new ArrayList<>(List.of(automatedCriteria)));
        rule.setApiKeyClientId(API_KEY_CLIENT_ID);
        rule.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        return rule;
    }

    private InitiativeDTO createStep3InitiativeDTO() {
        InitiativeDTO dto = createStep2InitiativeDTO();
        dto.setBeneficiaryRule(createInitiativeBeneficiaryRuleDTO());
        return dto;
    }

    private InitiativeBeneficiaryRuleDTO createInitiativeBeneficiaryRuleDTO() {
        InitiativeBeneficiaryRuleDTO dto = new InitiativeBeneficiaryRuleDTO();
        dto.setOrganizationName(ORGANIZATION_NAME);
        dto.setOrganizationUserRole(ROLE);
        dto.setSelfDeclarationCriteria(createAnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems());
        dto.setAutomatedCriteria(createAutomatedCriteriaDTO());
        dto.setApiKeyClientId(API_KEY_CLIENT_ID);
        dto.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return dto;
    }

    private List<AutomatedCriteriaDTO> createAutomatedCriteriaDTO() {
        AutomatedCriteriaDTO automatedCriteriaDTO = new AutomatedCriteriaDTO();
        automatedCriteriaDTO.setAuthority("Authority_ISEE");
        automatedCriteriaDTO.setCode("Code_ISEE");
        automatedCriteriaDTO.setField("true");
        automatedCriteriaDTO.setOperator(FilterOperatorEnum.EQ);
        automatedCriteriaDTO.setValue("value");
        return new ArrayList<>(List.of(automatedCriteriaDTO));
    }

    private List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> createAnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems() {
        SelfCriteriaBoolDTO selfCriteriaBoolDTO = new SelfCriteriaBoolDTO();
        selfCriteriaBoolDTO.setType(it.gov.pagopa.initiative.dto.TypeBoolEnum.BOOLEAN);
        selfCriteriaBoolDTO.setCode("B001");
        selfCriteriaBoolDTO.setDescription("Desc_bool");
        selfCriteriaBoolDTO.setSubDescription("SubDesc_bool");
        selfCriteriaBoolDTO.setValue(true);

        SelfCriteriaMultiDTO selfCriteriaMultiDTO = new SelfCriteriaMultiDTO();
        selfCriteriaMultiDTO.setType(it.gov.pagopa.initiative.dto.TypeMultiEnum.MULTI);
        selfCriteriaMultiDTO.setCode("B001");
        selfCriteriaMultiDTO.setDescription("Desc_Multi");
        selfCriteriaMultiDTO.setSubDescription("SubDesc_multi");
        selfCriteriaMultiDTO.setValue(List.of("valore1", "valore2"));

        List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> list = new ArrayList<>();
        list.add(selfCriteriaBoolDTO);
        list.add(selfCriteriaMultiDTO);
        return list;
    }

    private Initiative createStep4Initiative() {
        return createStep3Initiative();
    }

    private InitiativeDTO createStep4InitiativeDTO() {
        return createStep3InitiativeDTO();
    }

    private Initiative createStep5Initiative() {
        Initiative initiative = createStep4Initiative();
        initiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        initiative.setUpdateDate(LocalDateTime.now());
        return initiative;
    }

    private InitiativeDTO createStep5InitiativeDTO() {
        InitiativeDTO dto = createStep4InitiativeDTO();
        dto.setRefundRule(createRefundRuleDTOValidWithTimeParameter());
        return dto;
    }

    private AccumulatedAmountDTO createAccumulatedAmountDTOValid() {
        AccumulatedAmountDTO dto = new AccumulatedAmountDTO();
        dto.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED);
        dto.setRefundThreshold(BigDecimal.valueOf(100000));
        return dto;
    }

    private TimeParameterDTO createTimeParameterDTO_Valid() {
        TimeParameterDTO dto = new TimeParameterDTO();
        dto.setTimeType(TimeParameterDTO.TimeTypeEnum.CLOSED);
        return dto;
    }

    private RefundAdditionalInfoDTO createAdditionalInfoDTOValid() {
        RefundAdditionalInfoDTO dto = new RefundAdditionalInfoDTO();
        dto.setIdentificationCode("B002");
        return dto;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameter() {
        InitiativeRefundRuleDTO dto = new InitiativeRefundRuleDTO();
        dto.setOrganizationName(ORGANIZATION_NAME);
        dto.setOrganizationUserRole(ROLE);
        dto.setAccumulatedAmount(null);
        dto.setTimeParameter(createTimeParameterDTO_Valid());
        dto.setAdditionalInfo(createAdditionalInfoDTOValid());
        return dto;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithAccumulatedAmount() {
        InitiativeRefundRuleDTO dto = new InitiativeRefundRuleDTO();
        dto.setOrganizationName(ORGANIZATION_NAME);
        dto.setOrganizationUserRole(ROLE);
        dto.setAccumulatedAmount(createAccumulatedAmountDTOValid());
        dto.setTimeParameter(null);
        dto.setAdditionalInfo(createAdditionalInfoDTOValid());
        return dto;
    }

    private AccumulatedAmount createAccumulatedAmount_Valid() {
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccumulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThresholdCents(10000000L);
        return amount;
    }

    private TimeParameter createTimeParameter_Valid() {
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    private AdditionalInfo createAdditionalInfo_Valid() {
        AdditionalInfo info = new AdditionalInfo();
        info.setIdentificationCode("B002");
        return info;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithAccumulatedAmountDTO_NotValid_ko() {
        InitiativeRefundRuleDTO dto = new InitiativeRefundRuleDTO();
        dto.setAccumulatedAmount(createAccumulatedAmountDTO_NotValid_ko());
        dto.setTimeParameter(null);
        return dto;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithAccumulatedAmountAndTimeParameter_NotValid() {
        InitiativeRefundRuleDTO dto = new InitiativeRefundRuleDTO();
        dto.setAccumulatedAmount(createAccumulatedAmountDTO_NotValid_ko());
        dto.setTimeParameter(createTimeParameterDTO_Valid());
        return dto;
    }

    private AccumulatedAmountDTO createAccumulatedAmountDTO_NotValid_ko() {
        AccumulatedAmountDTO dto = new AccumulatedAmountDTO();
        dto.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.BUDGET_EXHAUSTED);
        dto.setRefundThreshold(BigDecimal.valueOf(100000));
        return dto;
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
        initiative.setOrganizationName(ORGANIZATION_NAME);
        initiative.setStatus("DRAFT");
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }

    private InitiativeDetailDTO createInitiativeDetailDTO() {
        InitiativeDetailDTO dto = new InitiativeDetailDTO();
        dto.setInitiativeName("TEST");
        dto.setStatus("APPROVED");
        dto.setDescription("test test");
        dto.setOnboardingStartDate(LocalDate.now().minusDays(25));
        dto.setOnboardingEndDate(LocalDate.now());
        dto.setFruitionStartDate(LocalDate.now());
        dto.setFruitionEndDate(LocalDate.now().plusDays(40));
        dto.setRewardRule(createRewardRuleDTO(false));
        dto.setRefundRule(null);
        dto.setPrivacyLink("privacy.it");
        dto.setTcLink("tc.it");
        dto.setLogoURL("logo.png");
        dto.setUpdateDate(LocalDateTime.now());
        dto.setServiceId("SERVICE_ID");
        return dto;
    }

    private InitiativeRewardRuleDTO createRewardRuleDTO(boolean isRewardFixedValue) {
        if (isRewardFixedValue) {
            return null;
        }

        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 =
                new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 =
                new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(40));
        rewardGroupsDTO.setRewardGroups(List.of(rewardGroupDTO1, rewardGroupDTO2));
        return rewardGroupsDTO;
    }
}