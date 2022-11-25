package it.gov.pagopa.initiative.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.initiative.model.config.ConfigMcc;
import it.gov.pagopa.initiative.model.config.ConfigTrxRule;
import it.gov.pagopa.initiative.repository.ConfigMccRepository;
import it.gov.pagopa.initiative.repository.ConfigTrxRulesRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ConfigServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ConfigServiceTest {
    @MockBean
    private ConfigMccRepository configMccRepository;

    @Autowired
    private ConfigServiceImpl configServiceImpl;

    @MockBean
    private ConfigTrxRulesRepository configTrxRulesRepository;

    @Test
    void testFindAllMcc() {
        ArrayList<ConfigMcc> configMccList = new ArrayList<>();
        when(configMccRepository.findAll()).thenReturn(configMccList);
        List<ConfigMcc> actualFindAllMccResult = configServiceImpl.findAllMcc();
        assertSame(configMccList, actualFindAllMccResult);
        assertTrue(actualFindAllMccResult.isEmpty());
        verify(configMccRepository).findAll();
    }

    @Test
    void testFindAllTrxRules() {
        ArrayList<ConfigTrxRule> configTrxRuleList = new ArrayList<>();
        when(configTrxRulesRepository.findAll()).thenReturn(configTrxRuleList);
        List<ConfigTrxRule> actualFindAllTrxRulesResult = configServiceImpl.findAllTrxRules();
        assertSame(configTrxRuleList, actualFindAllTrxRulesResult);
        assertTrue(actualFindAllTrxRulesResult.isEmpty());
        verify(configTrxRulesRepository).findAll();
    }
}

