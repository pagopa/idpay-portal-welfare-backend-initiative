package it.gov.pagopa.initiative.connector.encrypt;

import it.gov.pagopa.initiative.dto.CFDTO;
import it.gov.pagopa.initiative.dto.EncryptedCfDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptRestConnectorImpl implements EncryptRestConnector {
  private final String apikey;
  private final EncryptRest encryptRest;

  public EncryptRestConnectorImpl(@Value("${api.key.encrypt}")String apikey,
      EncryptRest encryptRest) {
    this.apikey = apikey;
    this.encryptRest = encryptRest;
  }

  @Override
  public EncryptedCfDTO upsertToken(CFDTO cfdto) {
    return encryptRest.upsertToken(cfdto,apikey);
  }
}
