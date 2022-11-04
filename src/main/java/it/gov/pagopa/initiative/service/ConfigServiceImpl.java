package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.config.ConfigMcc;
import it.gov.pagopa.initiative.model.config.ConfigTrxRule;
import it.gov.pagopa.initiative.repository.ConfigMccRepository;
import it.gov.pagopa.initiative.repository.ConfigTrxRulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMccRepository configMccRepository;
    @Autowired
    private ConfigTrxRulesRepository configTrxRulesRepository;
    @Override
    public List<ConfigMcc> findAllMcc() {
        return configMccRepository.findAll();
    }

    @Override
    public List<ConfigTrxRule> findAllTrxRules() {
        return configTrxRulesRepository.findAll();
    }
}
