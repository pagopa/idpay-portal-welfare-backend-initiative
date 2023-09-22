package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.connector.io_service.IOBackEndFeignRestClient;
import it.gov.pagopa.initiative.connector.io_service.IOManageBackEndFeignRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {IOBackEndFeignRestClient.class, IOManageBackEndFeignRestClient.class})
public class IOBackEndRestConnectorConfig {

}
