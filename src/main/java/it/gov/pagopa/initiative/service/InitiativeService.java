package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InitiativeService {

    List<Initiative> retrieveInitiativeSummary(String organizationId);

    Initiative insertInitiative(Initiative initiative);

    Initiative getInitiative(String organizationId, String initiativeId);

    void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel);

    void updateInitiativeBeneficiary(String organizationId, String initiativeId, Initiative toBeneficiaryRuleModel);
}
