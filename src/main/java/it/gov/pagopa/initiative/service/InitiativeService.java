package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public interface InitiativeService {

    List<Initiative> retrieveInitiativeSummary(String organizationId, String role);
    List<Initiative> getInitiativesIssuerList();

    Initiative insertInitiative(Initiative initiative, String organizationId, String organizationName, String role);

    Initiative getInitiative(String organizationId, String initiativeId, String role);

    Initiative getInitiativeBeneficiaryView(String initiativeId);

    void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel, String role, boolean isDraft);

    void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo, String role);

    void updateStep3InitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule toBeneficiaryRuleModel, String role, boolean isDraft);

    void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules, String role, boolean isDraft);

    void updateInitiativeRefundRules(String organizationId, String initiativeId, String role, Initiative refundRule, boolean changeInitiativeStatus);

    void updateInitiativeApprovedStatus(String organizationId, String initiativeId, String role);

    void updateInitiativeToCheckStatus(String organizationId, String initiativeId, String role);

    void logicallyDeleteInitiative(String organizationId, String initiativeId, String role);

    void sendInitiativeInfoToRuleEngine(Initiative initiative);

    void sendInitiativeInfoToNotificationManager(Initiative initiative);

    void isInitiativeAllowedToBeNextStatusThenThrows(Initiative initiative, String statusToBeUpdated, String role);

    void updateInitiative(Initiative initiative);
    LogoDTO storeInitiativeLogo(String organizationId, String initiativeId, InputStream logo, String contentType, String fileName);

    Initiative sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(Initiative initiative, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO);

    void sendEmailToCurrentOrg(Initiative initiative, String template,  String subject);
    void sendEmailToPagoPA(Initiative initiative, String template,  String subject);

    Initiative getInitiativeIdFromServiceId(String serviceId);

    InitiativeAdditional getPrimaryAndSecondaryTokenIO(String initiativeId);
    OnboardingDTO getOnboardingStatusList(String organizationId, String initiativeId, String cf, LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable);
    BeneficiaryRankingPageDTO getRankingList(String organizationId, String initiativeId,  Pageable pageable, String beneficiary, String state);
    void validate(String contentType, String fileName);
    InitiativeDetailDTO getInitiativeBeneficiaryDetail(String initiativeId, Locale acceptLanguage);

    void deleteInitiative(String initiativeId);
}
