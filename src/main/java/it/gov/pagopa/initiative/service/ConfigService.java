package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.config.ConfigMcc;
import it.gov.pagopa.initiative.model.config.ConfigTrxRule;

import java.util.List;

public interface ConfigService {
    List<ConfigMcc> findAllMcc();

    List<ConfigTrxRule> findAllTrxRules();
}
