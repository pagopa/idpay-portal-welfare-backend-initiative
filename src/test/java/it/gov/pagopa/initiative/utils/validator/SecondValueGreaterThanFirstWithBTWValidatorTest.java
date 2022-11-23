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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecondValueGreaterThanFirstWithBTWValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Helper method for working with {@link Set>}s, as there's no i.e. <code>.get(0)</code>.
     *
     * @param set the set
     * @return the first element
     */
    private ConstraintViolation<AutomatedCriteriaDTO> first(Set<ConstraintViolation<AutomatedCriteriaDTO>> set) {
        return set.iterator().next();
    }

    @Test
    void whenAllValidationAreValid_AutomatedCriteriaDTO_thenValidationArePassed() {
        AutomatedCriteriaDTO automatedCriteriaDTO = createAutomatedCriteriaDTO(FilterOperatorEnum.LE, "Roma", null);
        Set<ConstraintViolation<AutomatedCriteriaDTO>> violations = validator.validate(automatedCriteriaDTO, ValidationOnGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenSecondValueIsLessEqualThanFirst_AutomatedCriteriaDTO_thenValidationAreFailed() {
        AutomatedCriteriaDTO automatedCriteriaDTO = createAutomatedCriteriaDTO(FilterOperatorEnum.BTW_CLOSED, "10.56", "10.56");
        Set<ConstraintViolation<AutomatedCriteriaDTO>> violations = validator.validate(automatedCriteriaDTO, ValidationOnGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(1);
//        assertThat(first(violations).getMessage()).isEqualTo("...");
    }

    @Test
    void whenSecondValueisGraterThanFirst_AutomatedCriteriaDTO_thenValidationArePassed(){
        AutomatedCriteriaDTO automatedCriteriaDTO = createAutomatedCriteriaDTO(FilterOperatorEnum.BTW_OPEN, "10.56", "25.65");
        Set<ConstraintViolation<AutomatedCriteriaDTO>> violations = validator.validate(automatedCriteriaDTO, ValidationOnGroup.class);

        assertTrue(violations.isEmpty());
    }

    private AutomatedCriteriaDTO createAutomatedCriteriaDTO(FilterOperatorEnum operator, String value, String value2) {
        return AutomatedCriteriaDTO.builder()
                .code("code")
                .authority("authority")
                .field("field")
                .operator(operator)
                .value(value)
                .value2(value2)
                .build();
    }
}
