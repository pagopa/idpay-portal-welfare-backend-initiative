package it.gov.pagopa.initiative.utils.validator.initiative.beneficiary;

import static it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary.PDNDapiKeyMustExistForAtLeastOneAutoCriteriaConstraint.MESSAGE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import it.gov.pagopa.initiative.dto.*;

import java.util.ArrayList;
import java.util.Set;
import javax.validation.*;

import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidatorTest {
    public static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    public static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAutomatedCriteriaIsEmptyAndApiKeysExist_thenTestIsValidIsFalse() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }

    @Test
    void whenAutomatedCriteriaIsEmptyAndApiKeyAssertionExists_thenTestIsValidIsFalse() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

//        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }

    @Test
    void whenAutomatedCriteriaIsEmptyAndApiKeyIdExists_thenTestIsValidIsFalse() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
//        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }

    @Test
    void whenAutomatedCriteriaIsEmptyAndApiKeysNotExist_thenTestIsValidIsFalse() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

//        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
//        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }

    @Test
    void whenAutomatedCriteriaExistAndApiKeysExist_thenTestIsValidIsTrue() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

        ArrayList<AutomatedCriteriaDTO> automatedCriteriaDTOList = new ArrayList<>();
        automatedCriteriaDTOList.add(new AutomatedCriteriaDTO());
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaDTOList);

        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
//        IsValid is True then it does not throw related error MESSAGE. Returned only with "false"
        assertFalse(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }

    @Test
    void whenAutomatedCriteriaExistAndApiKeysNull_thenTestIsValidIsFalse() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

        ArrayList<AutomatedCriteriaDTO> automatedCriteriaDTOList = new ArrayList<>();
        automatedCriteriaDTOList.add(new AutomatedCriteriaDTO());
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaDTOList);

//        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
//        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }

    @Test
    void whenAutomatedCriteriaExistAndApiKeyAssertionExists_thenTestIsValidIsFalse() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

        ArrayList<AutomatedCriteriaDTO> automatedCriteriaDTOList = new ArrayList<>();
        automatedCriteriaDTOList.add(new AutomatedCriteriaDTO());
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaDTOList);

//        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }

    @Test
    void whenAutomatedCriteriaExistAndApiKeyClientExists_thenTestIsValidIsFalse() {
        PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator pdnDapiKeyMustExistForAtLeastOneAutoCriteriaValidator = new PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator();

        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();

        ArrayList<AutomatedCriteriaDTO> automatedCriteriaDTOList = new ArrayList<>();
        automatedCriteriaDTOList.add(new AutomatedCriteriaDTO());
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaDTOList);

        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
//        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);

        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violations = validator.validate(initiativeBeneficiaryRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.stream().anyMatch(initiativeBeneficiaryRuleDTOConstraintViolation -> initiativeBeneficiaryRuleDTOConstraintViolation.getMessage().equals(MESSAGE)));
    }
}

