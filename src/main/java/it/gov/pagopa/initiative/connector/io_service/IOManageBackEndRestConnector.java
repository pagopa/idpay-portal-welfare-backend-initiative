package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.KeysDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IOManageBackEndRestConnector {

  ServiceResponseDTO createService(@RequestBody @Valid ServiceRequestDTO serviceRequestDTO);
  ResponseEntity<Void> sendLogoIo(String serviceId, LogoIODTO logoDTO);
  ServiceResponseDTO updateService(String serviceId, ServiceRequestDTO serviceRequestDTO);
  ResponseEntity<Void> deleteService(String serviceId);
  KeysDTO getServiceKeys(@PathVariable("serviceId") String serviceId);

}
