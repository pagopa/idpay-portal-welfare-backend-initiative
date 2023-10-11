package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.KeysDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import jakarta.validation.Valid;
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
  public ServiceResponseDTO createService(@Valid ServiceRequestDTO serviceRequestDTO) {
    ResponseEntity<ServiceResponseDTO> responseEntity = ioManageBackEndFeignRestClient.createService(serviceRequestDTO, subscriptionKey);
    return responseEntity.getBody();
  }

  @Override
  public void sendLogoIo(String serviceId, LogoIODTO logoDTO) {
    ioManageBackEndFeignRestClient.sendLogo(serviceId, logoDTO, subscriptionKey);
  }

  @Override
  public ServiceResponseDTO updateService(String serviceId, ServiceRequestDTO serviceRequestDTO) {
    ResponseEntity<ServiceResponseDTO> responseEntity = ioManageBackEndFeignRestClient.updateService(serviceId, serviceRequestDTO, subscriptionKey);
    return responseEntity.getBody();
  }

  @Override
  public void deleteService(String serviceId) {
    ioManageBackEndFeignRestClient.deleteService(serviceId, subscriptionKey);
  }

  @Override
  public KeysDTO getServiceKeys(String serviceId) {
    ResponseEntity<KeysDTO> responseEntity =  ioManageBackEndFeignRestClient.getServiceKeys(serviceId, subscriptionKey);
    return  responseEntity.getBody();
  }

}
