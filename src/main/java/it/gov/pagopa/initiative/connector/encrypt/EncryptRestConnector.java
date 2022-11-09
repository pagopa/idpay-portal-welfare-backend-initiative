package it.gov.pagopa.initiative.connector.encrypt;

import it.gov.pagopa.initiative.dto.CFDTO;
import it.gov.pagopa.initiative.dto.EncryptedCfDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface EncryptRestConnector {

  EncryptedCfDTO upsertToken(@RequestBody CFDTO body);
}
