package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.connector.decrypt.DecryptRest;
import it.gov.pagopa.initiative.connector.encrypt.EncryptRest;
import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestClient;
import it.gov.pagopa.initiative.connector.ranking.RankingRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
    EncryptRest.class,
    DecryptRest.class,
    OnboardingRestClient.class,
    RankingRestClient.class
})
public class RestConnectorConfig {

}
