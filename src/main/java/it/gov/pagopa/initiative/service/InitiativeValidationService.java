package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.model.AutomatedCriteria;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;

import java.util.List;

public interface InitiativeValidationService {

    Initiative getInitiative(String organizationId, String initiativeId, String role);

    void checkPermissionBeforeInsert(String role);

    void checkAutomatedCriteria(Initiative initiative, List<AutomatedCriteria> initiativeBeneficiaryRuleModel);

    void validateAllWizardSteps(InitiativeDTO initiativeDTO);
    void checkRewardRuleAbsolute(Initiative initiative);
    void checkRefundRuleDiscountInitiative(String initiativeRewardType, InitiativeRefundRule refundRule);
    void checkBeneficiaryTypeAndFamilyUnit(Initiative initiative);
}
