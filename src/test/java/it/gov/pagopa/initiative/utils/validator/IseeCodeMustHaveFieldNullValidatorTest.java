package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.dto.FilterOperatorEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IseeCodeMustHaveFieldNullValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void automatedCriteriaDTOWithISEEAndField_thenValidationIsFailed(){
        AutomatedCriteriaDTO automatedCriteriaDTO = createAutomatedCriteriaDTOwithISEEAndField();
        Set<ConstraintViolation<AutomatedCriteriaDTO>> violations = validator.validate(automatedCriteriaDTO, ValidationOnGroup.class);

        assertThat(violations).hasSize(1);
    }

    @Test
    void automatedCriteriaDTOValid_thenValidationIsPassed(){
        AutomatedCriteriaDTO automatedCriteriaDTO = createAutomatedCriteriaDTOValid();
        Set<ConstraintViolation<AutomatedCriteriaDTO>> violations = validator.validate(automatedCriteriaDTO, ValidationOnGroup.class);

        assertTrue(violations.isEmpty());
    }


    private AutomatedCriteriaDTO createAutomatedCriteriaDTOwithISEEAndField(){
        AutomatedCriteriaDTO automatedCriteriaDTO = new AutomatedCriteriaDTO();
        automatedCriteriaDTO.setAuthority("INPS");
        automatedCriteriaDTO.setCode("ISEE");
        automatedCriteriaDTO.setOperator(FilterOperatorEnum.BTW_CLOSED);
        automatedCriteriaDTO.setValue("5.21");
        automatedCriteriaDTO.setValue2("6.98");
        automatedCriteriaDTO.setField("FIELD");
        return automatedCriteriaDTO;
    }

    private AutomatedCriteriaDTO createAutomatedCriteriaDTOValid(){
        AutomatedCriteriaDTO automatedCriteriaDTO = new AutomatedCriteriaDTO();
        automatedCriteriaDTO.setAuthority("INPS");
        automatedCriteriaDTO.setCode("ISEE");
        automatedCriteriaDTO.setOperator(FilterOperatorEnum.BTW_CLOSED);
        automatedCriteriaDTO.setValue("5.21");
        automatedCriteriaDTO.setValue2("6.98");
        return automatedCriteriaDTO;
    }

}
