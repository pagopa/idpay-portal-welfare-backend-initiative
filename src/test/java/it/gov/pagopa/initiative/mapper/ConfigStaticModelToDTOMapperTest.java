package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.config.ConfigMccDTO;
import it.gov.pagopa.initiative.dto.config.ConfigTrxRuleDTO;
import it.gov.pagopa.initiative.model.config.ConfigMcc;
import it.gov.pagopa.initiative.model.config.ConfigTrxRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(value = {
        ConfigStaticModelToDTOMapper.class})
class ConfigStaticModelToDTOMapperTest {
    @Autowired
    ConfigStaticModelToDTOMapper configStaticModelToDTOMapper;
    private List<ConfigMcc> mccConfigListActual;
    private List<ConfigTrxRule> trxConfigRulesListActual;
    private List<ConfigMccDTO> mccConfigDTOListExpected;
    private List<ConfigTrxRuleDTO> trxConfigRulesDTOListExpected;

    @BeforeEach
    public void setUp() {
    }

    private List<ConfigMcc> setUpMccConfigList(boolean doEmpty) {
        ConfigMcc configMcc = ConfigMcc.builder().code("code").description("description").build();
        if(doEmpty)
            return Collections.emptyList();
        else
            return Arrays.asList(configMcc, configMcc);
    }

    private List<ConfigTrxRule> setUpTransactionConfigRulesList(boolean doEmpty) {
        ConfigTrxRule configTrxRule = ConfigTrxRule.builder()
                .code("code")
                .description("description")
                .checked(true)
                .enabled(false)
                .build();
        if(doEmpty)
            return Collections.emptyList();
        else
            return Arrays.asList(configTrxRule, configTrxRule);
    }

    private List<ConfigMccDTO> setUpMccConfigDTOList(boolean doEmpty) {
        ConfigMccDTO configMccDTO = ConfigMccDTO.builder().code("code").description("description").build();
        if(doEmpty)
            return Collections.emptyList();
        else
            return Arrays.asList(configMccDTO, configMccDTO);
    }

    private List<ConfigTrxRuleDTO> setUpTransactionConfigRulesDTOList(boolean doEmpty) {
        ConfigTrxRuleDTO configTrxRuleDTO = ConfigTrxRuleDTO.builder()
                .code("code")
                .description("description")
                .checked(true)
                .enabled(false)
                .build();
        if(doEmpty)
            return Collections.emptyList();
        else
            return Arrays.asList(configTrxRuleDTO, configTrxRuleDTO);
    }

    @Test
    void toMccDTOs_ok(){
        mccConfigListActual = setUpMccConfigList(false);
        mccConfigDTOListExpected = setUpMccConfigDTOList(false);
        List<ConfigMccDTO> configMccDTOSActual = configStaticModelToDTOMapper.toMccDTOs(mccConfigListActual);

        //Check the equality of the results
        assertEquals(mccConfigDTOListExpected, configMccDTOSActual);
    }

    @Test
    void toTrxRulesDTOs_ok(){
        trxConfigRulesListActual = setUpTransactionConfigRulesList(false);
        trxConfigRulesDTOListExpected = setUpTransactionConfigRulesDTOList(false);
        List<ConfigTrxRuleDTO> configTrxRuleDTOSActual = configStaticModelToDTOMapper.toTrxRulesDTOs(trxConfigRulesListActual);

        //Check the equality of the results
        assertEquals(trxConfigRulesDTOListExpected, configTrxRuleDTOSActual);
    }

    @Test
    void toEmptyListMccDTOs_ok(){
        mccConfigListActual = setUpMccConfigList(true);
        mccConfigDTOListExpected = setUpMccConfigDTOList(true);
        List<ConfigMccDTO> configMccDTOSActual = configStaticModelToDTOMapper.toMccDTOs(mccConfigListActual);

        //Check the equality of the results
        assertEquals(mccConfigDTOListExpected, configMccDTOSActual);
    }

    @Test
    void toEmptyListTrxRulesDTOs_ok(){
        trxConfigRulesListActual = setUpTransactionConfigRulesList(true);
        trxConfigRulesDTOListExpected = setUpTransactionConfigRulesDTOList(true);
        List<ConfigTrxRuleDTO> configTrxRuleDTOSActual = configStaticModelToDTOMapper.toTrxRulesDTOs(trxConfigRulesListActual);

        //Check the equality of the results
        assertEquals(trxConfigRulesDTOListExpected, configTrxRuleDTOSActual);
    }

}
