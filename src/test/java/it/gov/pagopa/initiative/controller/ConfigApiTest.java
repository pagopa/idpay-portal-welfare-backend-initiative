package it.gov.pagopa.initiative.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.initiative.mapper.ConfigStaticModelToDTOMapper;
import it.gov.pagopa.initiative.model.config.ConfigMcc;
import it.gov.pagopa.initiative.model.config.ConfigTrxRule;
import it.gov.pagopa.initiative.service.ConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(value = {
        ConfigApi.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ConfigApiTest {

    @MockBean
    ConfigService configService;

    @MockBean
    ConfigStaticModelToDTOMapper configStaticModelToDTOMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mvc;

    private static final String BASE_URL = "http://localhost:8080/idpay";
    private static final String GET_CONFIG_TRANSACTION_RULE_URL = "/initiative/config/transaction/rule";
    private static final String GET_CONFIG_TRANSACTION_MCC_URL = "/initiative/config/transaction/mcc";
    private List<ConfigMcc> mccConfigList;
    private List<ConfigTrxRule> trxConfigRulesList;

    @BeforeEach
    public void setUp() {
        mccConfigList = setUpMccConfig();
        trxConfigRulesList = setUpTransactionConfigRules();
    }

    private List<ConfigTrxRule> setUpTransactionConfigRules() {
        ConfigTrxRule configTrxRule = ConfigTrxRule.builder()
                .code("code")
                .description("description")
                .checked(true)
                .enabled(false)
                .build();
        return Arrays.asList(configTrxRule, configTrxRule);
    }

    private List<ConfigMcc> setUpMccConfig() {
        ConfigMcc configMcc = ConfigMcc.builder().code("code").description("description").build();
        return Arrays.asList(configMcc, configMcc);
    }

    @Test
    void getMccConfig_ok() throws Exception {

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the Dummy mccConfigList to me anytime I call the same service's function
        when(configService.findAllMcc()).thenReturn(mccConfigList);

        List<ConfigMcc> retrieveInitiativeSummary = configService.findAllMcc();

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", retrieveInitiativeSummary, is(sameInstance(mccConfigList)));

        mvc.perform(
            MockMvcRequestBuilders.get(BASE_URL + GET_CONFIG_TRANSACTION_MCC_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void getTransactionConfigRules_ok() throws Exception {

        // When
        // With this instruction, I instruct the service (via Mockito's when) to always return the Dummy trxConfigRulesList to me anytime I call the same service's function
        when(configService.findAllTrxRules()).thenReturn(trxConfigRulesList);

        List<ConfigTrxRule> allTrxRules = configService.findAllTrxRules();

        // Then
        // you are expecting service to return whatever returned by repo
        assertThat("Reason of result", allTrxRules, is(sameInstance(trxConfigRulesList)));

        //The MVC perform should perform the API by returning the response based on the Service previously mocked.
        mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + GET_CONFIG_TRANSACTION_RULE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();
    }

}