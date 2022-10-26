package it.gov.pagopa.initiative.connector.selc;

import it.gov.pagopa.initiative.dto.selc.UserResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelcRestConnectorImpl implements SelcRestConnector {

  private final String subscriptionKey;
  private final String selfCareUid;
  private final SelcFeignRestClient selcRestClient;

  public SelcRestConnectorImpl(
          @Value("${rest-client.selc.service.subscriptionKey}") String subscriptionKey,
          @Value("${rest-client.selc.service.selfCareUid}") String selfCareUid,
          SelcFeignRestClient selcRestClient) {
    this.subscriptionKey = subscriptionKey;
    this.selfCareUid = selfCareUid;
    this.selcRestClient = selcRestClient;
  }

  @Override
  public List<UserResource> getInstitutionProductUsers(String organizationId) {
    return selcRestClient.getInstitutionProductUsers(organizationId, subscriptionKey, selfCareUid);
  }

}
