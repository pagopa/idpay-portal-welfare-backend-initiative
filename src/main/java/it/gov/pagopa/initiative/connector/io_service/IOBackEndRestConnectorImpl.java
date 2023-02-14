package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class IOBackEndRestConnectorImpl implements IOBackEndRestConnector {

  private final String subscriptionKey;
  private final IOBackEndFeignRestClient ioBackEndFeignRestClient;

  public IOBackEndRestConnectorImpl(
      @Value("${rest-client.backend-io.service.subscriptionKey}") String subscriptionKey,
      IOBackEndFeignRestClient ioBackEndFeignRestClient) {
    this.subscriptionKey = subscriptionKey;
    this.ioBackEndFeignRestClient = ioBackEndFeignRestClient;
  }

  @Override
  public ServiceResponseDTO createService(@Valid ServiceRequestDTO serviceRequestDTO) {
    ResponseEntity<ServiceResponseDTO> responseEntity = ioBackEndFeignRestClient.createService(serviceRequestDTO, subscriptionKey);
    return responseEntity.getBody();
  }

  @Override
  public void sendLogoIo(String serviceId, String primaryKey, LogoIODTO logo) {
    ioBackEndFeignRestClient.sendLogo(serviceId, logo, primaryKey);
  }

  @Override
  public ServiceResponseDTO updateService(@Valid String serviceId,ServiceRequestDTO serviceRequestDTO) {
    ResponseEntity<ServiceResponseDTO> responseEntity = ioBackEndFeignRestClient.updateService(serviceId, serviceRequestDTO, subscriptionKey);
    return responseEntity.getBody();
  }

}
