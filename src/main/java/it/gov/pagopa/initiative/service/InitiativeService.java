package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InitiativeService {

    List<Initiative> retrieveInitiativeSummary(String organizationId);

    Initiative insertInitiative(Initiative initiative);

    Initiative getInitiative(String organizationId, String initiativeId);

    Initiative getInitiativeBeneficiaryView(String initiativeId);

    void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel);

    void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule toBeneficiaryRuleModel);

    void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules);

    void updateInitiativeRefundRules(String organizationId, String initiativeId, Initiative refundRule);

    void sendInitiativeInfoToRuleEngine(InitiativeDTO initiativeDTO);
}
