package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.SelfCriteriaMultiConsentValueDTO;
import it.gov.pagopa.initiative.exception.custom.InitiativeStatusNotValidException;
import it.gov.pagopa.initiative.exception.custom.SelfCriteriaNotValidException;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.model.SelfCriteriaMultiConsent;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class InitiativeServiceRoot {

    void insertTechnicalData(Initiative initiative){
        initiative.setEnabled(true);
    }

    void isInitiativeAllowedToBeEditableThenThrows(Initiative initiative){
        if(Arrays.asList(InitiativeConstants.Status.Validation.INITIATIVES_ALLOWED_STATES_TO_BE_EDITABLE_ARRAY).contains(initiative.getStatus())){
            return;
        }
        throw new InitiativeStatusNotValidException(
            "Initiative [%s] with status [%s] is unprocessable for status not valid".formatted(initiative.getInitiativeId(), initiative.getStatus())
        );
    }


    void checkSelfDeclarationCriteria(Initiative initiative, InitiativeBeneficiaryRule beneficiaryRule) {
        Long fixedBudget = (initiative.getGeneral() != null) ? initiative.getGeneral().getBeneficiaryBudgetFixedCents() : null;
        String initiativeId = initiative.getInitiativeId();

        List<SelfCriteriaMultiConsent> multiConsentCriteria = getMultiConsentCriteria(beneficiaryRule);

        int criteriaWithBudgetCount = validateMultiConsentCriteria(multiConsentCriteria, initiativeId);
        boolean hasMultiConsentBudget = criteriaWithBudgetCount > 0;

        if (criteriaWithBudgetCount > 1) {
            throwSelfDeclarationCriteriaException(initiativeId, "only one MultiConsent criteria can have budget values defined.");
        }

        validateBudgetExclusivity(fixedBudget, hasMultiConsentBudget, initiativeId);
    }

    private List<SelfCriteriaMultiConsent> getMultiConsentCriteria(InitiativeBeneficiaryRule rule) {
        return Optional.ofNullable(rule.getSelfDeclarationCriteria())
                .orElse(Collections.emptyList())
                .stream()
                .filter(SelfCriteriaMultiConsent.class::isInstance)
                .map(SelfCriteriaMultiConsent.class::cast)
                .toList();
    }

    private int validateMultiConsentCriteria(List<SelfCriteriaMultiConsent> criteriaList, String initiativeId) {
        int count = 0;
        for (SelfCriteriaMultiConsent criteria : criteriaList) {
            if (hasBudgetConfigured(criteria, initiativeId)) {
                count++;
            }
        }
        return count;
    }

    private boolean hasBudgetConfigured(SelfCriteriaMultiConsent criteria, String initiativeId) {
        List<SelfCriteriaMultiConsentValueDTO> values = Optional.ofNullable(criteria.getValue()).orElse(Collections.emptyList());
        if (values.isEmpty()) return false;

        long valuesWithBudget = values.stream()
                .filter(v -> v.getBeneficiaryBudgetCentsMin() != null || v.getBeneficiaryBudgetCentsMax() != null)
                .count();

        if (valuesWithBudget == 0) return false;

        if (valuesWithBudget != values.size()) {
            throwSelfDeclarationCriteriaException(initiativeId, "if one choice in a MultiConsent criteria has a budget, all other choices in the same criteria must also have min/max values defined.");
        }
        values.forEach(v -> validateRange(v, initiativeId));

        return true;
    }

    private void validateRange(SelfCriteriaMultiConsentValueDTO value, String initiativeId) {
        Long min = value.getBeneficiaryBudgetCentsMin();
        Long max = value.getBeneficiaryBudgetCentsMax();
        boolean blockingVerify = value.isBlockingVerify();

        if (min == null || max == null) {
            throwSelfDeclarationCriteriaException(initiativeId, "both min and max budgets must be present for all choices in the MultiConsent criteria.");
        }
        if (!blockingVerify && min == 0) {
            throwSelfDeclarationCriteriaException(initiativeId, "if blockingVerify is false, beneficiaryBudgetCentsMin cannot be 0.");
        }
        if (blockingVerify && min != 0) {
            throwSelfDeclarationCriteriaException(initiativeId, "if blockingVerify is true, beneficiaryBudgetCentsMin must be 0.");
        }
        if (max < min) {
            throwSelfDeclarationCriteriaException(initiativeId, "beneficiaryBudgetCentsMax must be greater than beneficiaryBudgetCentsMin.");
        }
    }

    private void validateBudgetExclusivity(Long fixedBudget, boolean hasMultiConsentBudget, String initiativeId) {
        if (fixedBudget != null && hasMultiConsentBudget) {
            throwSelfDeclarationCriteriaException(initiativeId, "when beneficiaryBudgetFixedCents is provided, all MultiConsent min/max budgets must be null.");
        }
        if (fixedBudget == null && !hasMultiConsentBudget) {
            throwSelfDeclarationCriteriaException(initiativeId, "beneficiaryBudgetFixedCents is null, so at least one MultiConsent range must be provided.");
        }
    }

    private void throwSelfDeclarationCriteriaException(String initiativeId, String message) {
        throw new SelfCriteriaNotValidException(
                "Initiative [%s] - Self declaration criteria not valid: %s".formatted(initiativeId, message)
        );
    }

    void isInitiativeStatusNotInRevisionThenThrow(Initiative initiative, String nextStatus){
        if (initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION)){
            log.info("[UPDATE_TO_{}_STATUS] - Initiative: {}. Current status is valid", nextStatus, initiative.getInitiativeId());
            return;
        }
        log.info("[UPDATE_TO_{}_STATUS] - Initiative: {}. Current status is not IN_REVISION", nextStatus, initiative.getInitiativeId());
        throw new InitiativeStatusNotValidException(
                "The status of initiative [%s] is not IN_REVISION".formatted(initiative.getInitiativeId())
        );
    }

}
