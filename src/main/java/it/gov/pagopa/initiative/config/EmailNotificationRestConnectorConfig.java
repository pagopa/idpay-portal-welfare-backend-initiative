package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationFeignRestClient;
import it.gov.pagopa.initiative.connector.selc.SelcFeignRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {EmailNotificationFeignRestClient.class, SelcFeignRestClient.class})
public class EmailNotificationRestConnectorConfig {

}
