package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class DisjointSetOrderedDatesFieldsFromLowestToHighestValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenDatesAndBudgetsAreValid_InitiativeGeneralDTO_thenValidationArePassed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);
//        Then in your test simply call it on the object you require validation on, with what exception you are validating:
//        Set<TheViolation<TheClassYouAreValidating> violations = validator.validate(theInstanceOfTheClassYouAreValidating);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenStartDateEndDateAreEqual_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(2);
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO_ok() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO_ko() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate;
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }


}
