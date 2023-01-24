package it.gov.pagopa.initiative.connector.group;

import it.gov.pagopa.initiative.dto.group.InitiativeNotificationDTO;
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
        InitiativeNotificationDTO initiativeNotificationDTO = InitiativeNotificationDTO.builder()
                .initiativeName(initiative.getInitiativeName())
                .serviceId(initiative.getAdditionalInfo().getServiceId())
                .build();
        groupFeignRestClient.notifyInitiativeForCitizen(initiative.getInitiativeId(), initiativeNotificationDTO);
    }

    @Override
    public void setGroupStatusToValidated(String initiativeId) {
        groupFeignRestClient.setGroupStatusToValidated(initiativeId);
    }

}
