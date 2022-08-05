package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.model.config.ConfigMcc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigMccRepository extends MongoRepository<ConfigMcc, String> {
}
