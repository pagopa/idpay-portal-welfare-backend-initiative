package it.gov.pagopa.initiative.connector.group;

import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.stereotype.Service;

@Service
public class GroupRestConnectorImpl implements GroupRestConnector {

    private final GroupFeignRestClient groupFeignRestClient;

    public GroupRestConnectorImpl(
            GroupFeignRestClient groupFeignRestClient) {
        this.groupFeignRestClient = groupFeignRestClient;
    }

    @Override
    public void notifyInitiativeToGroup(Initiative initiative) {
        groupFeignRestClient.notifyInitiativeForCitizen(initiative.getInitiativeId(), initiative.getInitiativeName(), initiative.getAdditionalInfo().getServiceId());
    }

}
