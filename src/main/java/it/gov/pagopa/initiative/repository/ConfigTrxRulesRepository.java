package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.model.config.ConfigTrxRule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigTrxRulesRepository extends MongoRepository<ConfigTrxRule, String> {
}
