package it.gov.pagopa.initiative.connector.io_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IOManageBackEndRestConnectorImplNew implements IOManageBackEndRestConnectorNew {

  private final String subscriptionKey;
  private final IOManageBackEndFeignRestClient ioBackEndFeignRestClient;

  public IOManageBackEndRestConnectorImplNew(
          @Value("${rest-client.backend-io-manage.service.subscriptionKey}") String subscriptionKey,
          IOManageBackEndFeignRestClient ioBackEndFeignRestClient) {
    this.subscriptionKey = subscriptionKey;
    this.ioBackEndFeignRestClient = ioBackEndFeignRestClient;
  }

  @Override
  public void deleteService(String serviceId) {
    ioBackEndFeignRestClient.deleteService(serviceId, subscriptionKey);
  }

}
