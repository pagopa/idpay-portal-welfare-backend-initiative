package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.connector.group.GroupFeignRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {GroupFeignRestClient.class})
public class GroupRestConnectorConfig {

}
