package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InitiativeService {

    List<Initiative> retrieveInitiativeSummary(String organizationId, String role);

    Initiative insertInitiative(Initiative initiative);

    Initiative getInitiative(String organizationId, String initiativeId);

    Initiative getInitiativeBeneficiaryView(String initiativeId);

    void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel);

    void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo);

    void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule toBeneficiaryRuleModel);

    void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules);

    void updateInitiativeRefundRules(String organizationId, String initiativeId, Initiative refundRule, boolean changeInitiativeStatus);

    void updateInitiativeApprovedStatus(String organizationId, String initiativeId);

    void updateInitiativeToCheckStatus(String organizationId, String initiativeId);

    void logicallyDeleteInitiative(String organizationId, String initiativeId);

    void sendInitiativeInfoToRuleEngine(Initiative initiative);

    void isInitiativeAllowedToBeNextStatusThenThrows(Initiative initiative, String statusToBeUpdated);

    void updateInitiative(Initiative initiative);

    Initiative sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(Initiative initiative, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO);

    Initiative getInitiativeIdFromServiceId(String serviceId);

    InitiativeAdditional getPrimaryAndSecondaryTokenIO(String organizationId, String initiativeId);
}
