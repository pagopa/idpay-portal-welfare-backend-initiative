package it.gov.pagopa.initiative.connector.decrypt;

import it.gov.pagopa.initiative.dto.DecryptCfDTO;
import org.springframework.stereotype.Service;

@Service
public interface DecryptRestConnector {

  DecryptCfDTO getPiiByToken(String token);
}
