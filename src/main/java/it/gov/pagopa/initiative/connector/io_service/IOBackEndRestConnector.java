package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface IOBackEndRestConnector {

  ServiceResponseDTO createService(@RequestBody @Valid ServiceRequestDTO serviceRequestDTO);
  void sendLogoIo(String serviceId, String primaryKey,  LogoIODTO logo);

  ServiceResponseDTO updateService(String serviceId, ServiceRequestDTO serviceRequestDTO);
}
