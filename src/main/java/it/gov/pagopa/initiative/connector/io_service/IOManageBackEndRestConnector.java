package it.gov.pagopa.initiative.connector.io_service;

import org.springframework.http.ResponseEntity;

public interface IOManageBackEndRestConnector {
  ResponseEntity<Void> deleteService(String serviceId);

}
