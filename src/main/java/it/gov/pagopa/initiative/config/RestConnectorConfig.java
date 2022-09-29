package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.connector.io.service.IOBackEndFeignRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {IOBackEndFeignRestClient.class})
public class RestConnectorConfig {

}
