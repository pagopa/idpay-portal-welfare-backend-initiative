package it.gov.pagopa.initiative.connector.io_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IOManageBackEndRestConnectorImpl implements IOManageBackEndRestConnector {

  private final String subscriptionKey;
  private final IOManageBackEndFeignRestClient ioManageBackEndFeignRestClient;

  public IOManageBackEndRestConnectorImpl(
          @Value("${rest-client.backend-io-manage.service.subscriptionKey}") String subscriptionKey, IOManageBackEndFeignRestClient ioManageBackEndFeignRestClient) {
    this.subscriptionKey = subscriptionKey;
    this.ioManageBackEndFeignRestClient = ioManageBackEndFeignRestClient;
  }

  @Override
  public ResponseEntity<Void> deleteService(String serviceId) {
    ioManageBackEndFeignRestClient.deleteService(serviceId, subscriptionKey);
      return null;
  }

}
