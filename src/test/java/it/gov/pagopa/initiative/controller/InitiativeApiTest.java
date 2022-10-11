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
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.CODE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.ACCUMULATED_AMOUNT_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.SOMETHING_WRONG_WITH_THE_REFUND_TYPE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.ADMIN;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.OPE_BASE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.initiative.conditions.notifyRE=true"
        })
@WebMvcTest(value = {
        InitiativeApi.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Slf4j
class InitiativeApiTest {

    public static final String INITIATIVE_ID = "Id1";
    public static final String ORGANIZATION_ID = "O1";
    public static final String SERVICE_ID = "service1";

    private static final String ORGANIZATION_ID_PLACEHOLDER = "%s";
    private static final String INITIATIVE_ID_PLACEHOLDER = "%s";
    private static final String SERVICE_ID_PLACEHOLDER = "%s";

    private static final String BASE_URL = "http://localhost:8080/idpay";
    private static final String GET_INITIATIVES_SUMMARY_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/summary";
    private static final String GET_INITIATIVE_ACTIVE_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER;
    private static final String GET_INITIATIVE_ID_FROM_SERVICE_ID = "/initiative?serviceId=" + SERVICE_ID_PLACEHOLDER;
    private static final String GET_PRIMARY_AND_SECONDARY_TOKEN_FROM_INITIATIVE_ID = "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/token";
    private static final String GET_INITIATIVE_BENEFICIARY_VIEW_URL = "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/beneficiary/view";
    private static final String POST_INITIATIVE_ADDITIONAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/info";

    private static final String PUT_INITIATIVE_ADDITIONAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/info";
    private static final String PUT_INITIATIVE_GENERAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/general";

    private static final String PUT_INITIATIVE_REFUND_RULES_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/refund";
    private static final String PUT_INITIATIVE_BENEFICIARY_RULES_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/beneficiary";
    private static final String PUT_INITIATIVE_STATUS_APPROVED_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/approved";
    private static final String PUT_INITIATIVE_TO_CHECK_STATUS_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/rejected";
    private static final String PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/published";
    private static final String LOGICALLY_DELETE_INITIATIVE_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER;
    private static final String ROLE = "TEST_ROLE";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    private static final String ORGANIZATION_USER_ID = "organizationUserId";
    private static final String ORGANIZATION_USER_ROLE = "organizationUserRole";

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

    @Test
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

    @Test
    void whenOpeBase_getInitiativeSummary_statusOk() throws Exception {

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        Initiative step2Initiative2 = createStep2Initiative(beneficiaryKnown);
        List<Initiative> initiatives = Arrays.asList(step2Initiative, step2Initiative2);

        // Returning something from Repo by using ServiceMock
        when(initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, OPE_BASE)).thenReturn(initiatives);

        // When
        List<Initiative> retrieveInitiativeSummary = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, OPE_BASE);

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

    @Test
    void getInitiativeDetail_statusOk() throws Exception {

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
                MockMvcRequestBuilders.get(BASE_URL + String.format(GET_INITIATIVE_ACTIVE_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
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

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();

        InitiativeAdditionalDTO initiativeAdditionalDTO = createStep1InitiativeAdditionalDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeService.insertInitiative(step1Initiative)).thenReturn(step1Initiative);

        when(initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO)).thenReturn(step1Initiative);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + String.format(POST_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID))
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
        Initiative step1Initiative = createStep2Initiative(beneficiaryKnown);
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO(beneficiaryKnown);

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeGeneralDTO)).thenReturn(step1Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, step1Initiative);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_GENERAL_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(initiativeGeneralDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeAdditionalInfo_statusNoContent() throws Exception{
        objectMapper.registerModule(new JavaTimeModule());

        Boolean beneficiaryKnown = false;
        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();

        // Instruct the Service to update a Dummy Initiative
        when(initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO)).thenReturn(step1Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeAdditionalInfo(ORGANIZATION_ID, INITIATIVE_ID, step1Initiative);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_ADDITIONAL_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
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

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        // Instruct the Service to get a Dummy Initiative
        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID)).thenReturn(step2Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID))
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

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        // Instruct the Service to get a Dummy Initiative
        when(initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID)).thenReturn(step2Initiative);

        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL, ORGANIZATION_ID, INITIATIVE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeRefundRule_statusNoContent() throws Exception{
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeDTOsToModelMapper.toInitiative(refundRuleDTO)).thenReturn(initiative);

        doNothing().when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, true);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(refundRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateRefundRule_PUT_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException() throws Exception{
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
                .when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, true);

        mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(refundRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeRefundRuleDraft_statusNoContent() throws Exception{
        objectMapper.registerModule(new JavaTimeModule());

        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleDTOValidWithAccumulatedAmount();

        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeDTOsToModelMapper.toInitiative(refundRuleDTO)).thenReturn(initiative);

        doNothing().when(initiativeService).updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, false);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_REFUND_RULES_INFO_URL + "/draft", ORGANIZATION_ID, INITIATIVE_ID))
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
        Initiative step2Initiative = createStep2Initiative(beneficiaryKnown);
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO)).thenReturn(initiativeBeneficiaryRule);

        //doNothing only for Void method
        InitiativeBeneficiaryRule initiativeBeneficiaryRule2 = initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        doNothing().when(initiativeService).updateInitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule2);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_BENEFICIARY_RULES_URL + "/draft", ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(initiativeBeneficiaryRuleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeStatusApproved_statusNoContent() throws Exception {
        //doNothing only for Void method
        doNothing().when(initiativeService).updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_STATUS_APPROVED_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateInitiativeToCheckStatus_statusNoContent() throws Exception {
        doNothing().when(initiativeService).updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_CHECK_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
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
        Initiative initiative = createStep5Initiative();
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setStatus(InitiativeConstants.Status.DRAFT);

        doThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION),
                        HttpStatus.BAD_REQUEST)
        ).when(initiativeService).updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID);

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_CHECK_STATUS_URL, initiative.getOrganizationId(), initiative.getInitiativeId()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(print())
                        .andReturn();

        ErrorDTO error = objectMapper.readValue(res.getResponse().getContentAsString(), ErrorDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getResponse().getStatus());
        assertEquals(CODE, error.getCode());
        assertTrue(error.getMessage().contains(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION));
    }

    @Test
    void DEL_logicallyDeleteInitiative_whenCurrentDeletedIsFalse_thenBecomeTrue() throws Exception{
        Initiative initiative = createStep5Initiative();
        initiative.setEnabled(true);


        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + String.format(LOGICALLY_DELETE_INITIATIVE_URL, initiative.getOrganizationId(), initiative.getInitiativeId()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
                .organizationUserId(ORGANIZATION_USER_ID)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO();

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString())).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(anyString(), anyString());
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        when(initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(step5Initiative, initiativeOrganizationInfoDTO)).thenReturn(step5Initiative);


        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
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
                .organizationUserId(ORGANIZATION_USER_ID)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO();
        InitiativeAdditionalDTO additionalInfoDTO = step5InitiativeDTO.getAdditionalInfo();
        additionalInfoDTO.setServiceIO(false);
        step5InitiativeDTO.setAdditionalInfo(additionalInfoDTO);

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString())).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(anyString(), anyString());
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative)).thenReturn(step5InitiativeDTO);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
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
                .organizationUserId(ORGANIZATION_USER_ID)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO();

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString())).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(anyString(), anyString());
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative)).thenReturn(step5InitiativeDTO);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doThrow(
                new KafkaException()
        ).when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
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
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserId(ORGANIZATION_USER_ID)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        //create Dummy Initiative
        Initiative step5Initiative = createStep5Initiative();
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO();

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the DummyInitiative to me anytime I call the same service's function
        when(initiativeService.getInitiative(anyString(), anyString())).thenReturn(step5Initiative);
        Initiative initiative = initiativeService.getInitiative(anyString(), anyString());
        // Expecting same instance
        assertThat("Reason of result", initiative, is(sameInstance(step5Initiative)));

        doNothing().when(initiativeService).isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED);

        // Instruct the Service to insert a Dummy Initiative
        when(initiativeModelToDTOMapper.toInitiativeDTO(initiative)).thenReturn(step5InitiativeDTO);

        doNothing().when(initiativeService).updateInitiative(any(Initiative.class));

        doNothing().when(initiativeService).sendInitiativeInfoToRuleEngine(any(Initiative.class));

        doThrow(
                FeignException.errorStatus("Bad Request", responseStub(400, "Bad Request"))
        ).when(initiativeService).sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(step5Initiative, initiativeOrganizationInfoDTO);

        MvcResult res =
                mvc.perform(MockMvcRequestBuilders.put(BASE_URL + String.format(PUT_INITIATIVE_TO_PUBLISHED_STATUS_URL, ORGANIZATION_ID, INITIATIVE_ID))
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

    private Response responseStub(int status, String reasonExceptionMessage) {
        return Response.builder()
                .request(
                        Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(), new byte[0], Charset.defaultCharset(), new RequestTemplate()))
                .status(status)
                .reason(reasonExceptionMessage)
//                .headers(Collections.emptyMap()
                .build();
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

    private Initiative createStep2Initiative (Boolean beneficiaryKnown) {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral(beneficiaryKnown));
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

    private InitiativeDTO createStep2InitiativeDTO (Boolean beneficiaryKnown) {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO(beneficiaryKnown));
        return initiativeDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(Boolean beneficiaryKnown) {
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

    /*
     * ############### Step 3 ###############
     */

    private Initiative createStep3Initiative (Boolean beneficiaryKnown) {
        Initiative initiative = createStep2Initiative(beneficiaryKnown);
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
        return initiativeBeneficiaryRule;
    }

    private InitiativeDTO createStep3InitiativeDTO (Boolean beneficiaryKnown) {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO(beneficiaryKnown);
        if(beneficiaryKnown) {
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

    /*
     * ############### Step 4 ###############
     */

    private Initiative createStep4Initiative (Boolean beneficiaryKnown) {
        Initiative initiative = createStep3Initiative(beneficiaryKnown);
        return initiative;
    }

    private InitiativeDTO createStep4InitiativeDTO (Boolean beneficiaryKnown) {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO(beneficiaryKnown);
        return initiativeDTO;
    }


    /*
     * ############### Step 5 ###############
     */

    private Initiative createStep5Initiative () {
        Initiative initiative = createStep5Initiative(false);
        initiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        return initiative;
    }

    private Initiative createStep5Initiative (Boolean beneficiaryKnown) {
        Initiative initiative = createStep4Initiative(beneficiaryKnown);
        return initiative;
    }

    private InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO(false);
        initiativeDTO.setRefundRule(createRefundRuleDTOValidWithTimeParameter());
        return initiativeDTO;
    }

    private InitiativeDTO createStep5InitiativeDTO (Boolean beneficiaryKnown) {
        InitiativeDTO initiativeDTO = createStep4InitiativeDTO(beneficiaryKnown);
        return initiativeDTO;
    }

    private AccumulatedAmountDTO createAccumulatedAmountDTOValid(){
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
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

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameter(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTO_Valid());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithAccumulatedAmount(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTOValid());
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private AccumulatedAmount createAccumulatedAmount_Valid(){
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccomulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThreshold(BigDecimal.valueOf(100000));
        return amount;
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

    private InitiativeRefundRuleDTO createRefundWithBothRefundNullNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(null);
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithAccumulatedAmountDTO_NotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTO_NotValid_ko());
        refundRuleDTO.setTimeParameter(null);
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithAccumulatedAmountAndTimeParameter_NotValid(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTO_NotValid_ko());
        refundRuleDTO.setTimeParameter(createTimeParameterDTO_Valid());
        return refundRuleDTO;
    }

    private AccumulatedAmountDTO createAccumulatedAmountDTO_NotValid_ko(){
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.BUDGET_EXHAUSTED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
    }

    private InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmount_Valid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfo_Valid());
        return refundRule;
    }
    private InitiativeRefundRule createRefundRuleValidWithTimeParameter(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameter_Valid());
        refundRule.setAdditionalInfo(createAdditionalInfo_Valid());
        return refundRule;
    }

    private Initiative createInitiativeOnlyRefundRule(){
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }

}