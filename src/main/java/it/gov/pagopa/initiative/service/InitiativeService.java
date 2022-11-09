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

    Initiative insertInitiative(Initiative initiative);

    Initiative getInitiative(String organizationId, String initiativeId, String role);

    Initiative getInitiativeBeneficiaryView(String initiativeId);

    void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel);

    void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo);

    void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule toBeneficiaryRuleModel);

    void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules);

    void updateInitiativeRefundRules(String organizationId, String organizationName, String initiativeId, Initiative refundRule, boolean changeInitiativeStatus);

    void updateInitiativeApprovedStatus(String organizationId, String organizationName, String initiativeId);

    void updateInitiativeToCheckStatus(String organizationId, String organizationName, String initiativeId);

    void logicallyDeleteInitiative(String organizationId, String initiativeId);

    void sendInitiativeInfoToRuleEngine(Initiative initiative);

    void sendInitiativeInfoToNotificationManager(Initiative initiative);

    void isInitiativeAllowedToBeNextStatusThenThrows(Initiative initiative, String statusToBeUpdated);

    void updateInitiative(Initiative initiative);

    Initiative sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(Initiative initiative, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO);

    Initiative getInitiativeIdFromServiceId(String serviceId);

    InitiativeAdditional getPrimaryAndSecondaryTokenIO(String initiativeId);
    OnboardingDTO getOnboardingStatusList(String organizationId,String initiativeId, String CF, LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable);
}
