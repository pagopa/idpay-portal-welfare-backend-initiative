package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.ConfigMcc;
import it.gov.pagopa.initiative.model.ConfigTrxRule;

import java.util.List;

public interface ConfigService {
    List<ConfigMcc> findAllMcc();

    List<ConfigTrxRule> findAllTrxRules();
}
