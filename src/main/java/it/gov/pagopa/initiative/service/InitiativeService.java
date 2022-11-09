package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.OnboardingDTO;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InitiativeService {

    List<Initiative> retrieveInitiativeSummary(String organizationId, String role);

//    Initiative insertInitiative(Initiative initiative, String organizationId);
    Initiative insertInitiative(Initiative initiative, String organizationId, String organizationName, String role);

    Initiative getInitiative(String organizationId, String initiativeId, String role);

    Initiative getInitiativeBeneficiaryView(String initiativeId);

    void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel, String role);

    void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo, String role);

    void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule toBeneficiaryRuleModel, String role);

    void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules, String role);

    void updateInitiativeRefundRules(String organizationId, String organizationName, String initiativeId, String role, Initiative refundRule, boolean changeInitiativeStatus);

    void updateInitiativeApprovedStatus(String organizationId, String organizationName, String initiativeId, String role);

    void updateInitiativeToCheckStatus(String organizationId, String organizationName, String initiativeId, String role);

    void logicallyDeleteInitiative(String organizationId, String initiativeId, String role);

    void sendInitiativeInfoToRuleEngine(Initiative initiative);

    void sendInitiativeInfoToNotificationManager(Initiative initiative);

    void isInitiativeAllowedToBeNextStatusThenThrows(Initiative initiative, String statusToBeUpdated, String role);

    void updateInitiative(Initiative initiative);

    Initiative sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(Initiative initiative, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO);

    Initiative getInitiativeIdFromServiceId(String serviceId);

    InitiativeAdditional getPrimaryAndSecondaryTokenIO(String initiativeId);
    OnboardingDTO getOnboardingStatusList(String initiativeId, String CF, LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable);
}
