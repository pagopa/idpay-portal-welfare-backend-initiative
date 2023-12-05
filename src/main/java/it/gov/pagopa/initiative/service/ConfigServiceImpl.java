package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.config.ConfigMcc;
import it.gov.pagopa.initiative.model.config.ConfigTrxRule;
import it.gov.pagopa.initiative.repository.ConfigMccRepository;
import it.gov.pagopa.initiative.repository.ConfigTrxRulesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigMccRepository configMccRepository;
    private final ConfigTrxRulesRepository configTrxRulesRepository;

    public ConfigServiceImpl(ConfigMccRepository configMccRepository, ConfigTrxRulesRepository configTrxRulesRepository) {
        this.configMccRepository = configMccRepository;
        this.configTrxRulesRepository = configTrxRulesRepository;
    }

    @Override
    public List<ConfigMcc> findAllMcc() {
        return configMccRepository.findAll();
    }

    @Override
    public List<ConfigTrxRule> findAllTrxRules() {
        return configTrxRulesRepository.findAll();
    }
}
