package it.gov.pagopa.initiative.utils.validator.initiative.beneficiary;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.dto.InitiativeBeneficiaryRuleDTO;
import it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary.PDNDapiKeyMustExistForAtLeastOneAutoCriteriaConstraint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator implements ConstraintValidator<PDNDapiKeyMustExistForAtLeastOneAutoCriteriaConstraint, InitiativeBeneficiaryRuleDTO> {

    @Override
    public boolean isValid(InitiativeBeneficiaryRuleDTO value, ConstraintValidatorContext context) {
        List<AutomatedCriteriaDTO> automatedCriteriaDTOList = value.getAutomatedCriteria();
        String apiKeyClientId = value.getApiKeyClientId();
        String apiKeyClientAssertion = value.getApiKeyClientAssertion();
        if(CollectionUtils.isEmpty(automatedCriteriaDTOList))
            return StringUtils.isEmpty(apiKeyClientId) && StringUtils.isEmpty(apiKeyClientAssertion);
        else
            return !StringUtils.isEmpty(apiKeyClientId) && !StringUtils.isEmpty(apiKeyClientAssertion);
    }
}
