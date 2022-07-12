package it.gov.pagopa.initiative.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.dto.InitiativeInfoDTO;
import it.gov.pagopa.initiative.mapper.InitiativeMapper;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.service.InitiativeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.MessageFormat;
import java.util.*;

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
    InitiativeMapper initiativeMapper;

    @Autowired
    protected MockMvc mvc;

    private static final String ORGANIZATION_ID_PLACEHOLDER = "{0}";
    private static final String INITIATIVE_ID_PLACEHOLDER = "{1}";

    private static final String BASE_URL = "http://localhost:8080/idpay";
    private static final String GET_INITIATIVES_SUMMARY_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/summary";
    private static final String GET_INITIATIVE_ACTIVE_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER;
    private static final String GET_INITIATIVE_BENEFICIARY_VIEW_URL = "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/beneficiary/view";
    private static final String POST_INITIATIVE_GENERAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/general";
    private static final String PATCH_INITIATIVE_GENERAL_INFO_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/general";
    private static final String PATCH_INITIATIVE_BENEFICIARY_RULES_URL = "/organization/" + ORGANIZATION_ID_PLACEHOLDER + "/initiative/" + INITIATIVE_ID_PLACEHOLDER + "/beneficiary";
    private static final String ROLE = "TEST_ROLE";

    @Test
    public void getInitiativeSummary_ok() throws Exception {

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        Initiative step1Initiative2 = createStep1Initiative();
        List<Initiative> initiatives = Arrays.asList(step1Initiative, step1Initiative2);

        // Returning something from Repo by using ServiceMock
        when(initiativeService.retrieveInitiativeSummary(anyString())).thenReturn(initiatives);

        // When
        List<Initiative> retrieveInitiativeSummary = initiativeService.retrieveInitiativeSummary(anyString());

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", retrieveInitiativeSummary, is(sameInstance(initiatives)));

        mvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + MessageFormat.format(GET_INITIATIVES_SUMMARY_URL, "Iniz123"))
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getInitiativeDetail_ok() throws Exception {

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
        Initiative step1Initiative2 = createStep1Initiative();
        List<Initiative> initiatives = Arrays.asList(step1Initiative, step1Initiative2);

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
    public void saveInitiativeGeneralInfo_ok() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        //create Dummy Initiative
        Initiative step1Initiative = createStep1Initiative();
//        Initiative step1Initiative2 = createStep1Initiative();
//        List<Initiative> initiatives = Arrays.asList(step1Initiative, step1Initiative2);

        InitiativeDTO step1InitiativeDTO = createStep1InitiativeDTO();
        InitiativeInfoDTO initiativeInfoDTO = new InitiativeInfoDTO();
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();

        // Returning something from Repo by using ServiceMock
        doNothing().when(initiativeService).insertInitiative(step1Initiative);

        Map<String, Object> body = new HashMap<>();
        body.put("general", initiativeGeneralDTO);
        body.put("additionalInfo", initiativeAdditionalDTO);

        mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + MessageFormat.format(POST_INITIATIVE_GENERAL_INFO_URL, "Ente1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andDo(print())
                        .andReturn();
    }

    Initiative createFullInitiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createFullInitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep1Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep1InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep2Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep2InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
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