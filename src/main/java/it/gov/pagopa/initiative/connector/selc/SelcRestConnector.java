package it.gov.pagopa.initiative.connector.selc;

import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SelcRestConnector {
    List<UserResource> getInstitutionProductUsers(@PathVariable("organizationId") String organizationId);
}
