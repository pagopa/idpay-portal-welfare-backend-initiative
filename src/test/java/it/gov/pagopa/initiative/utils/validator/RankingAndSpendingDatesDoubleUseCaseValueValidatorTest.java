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
class RankingAndSpendingDatesDoubleUseCaseValueValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllValidationAreValid_InitiativeGeneralDTO_thenValidationArePassed() {
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

    @Test
    void whenOnlyRankingStartIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyRankingStartIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    void whenOnlyRankingEndIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyRankingEndIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void whenOnlyStartDateIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyStartDateIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }
    @Test
    void whenOnlyEndDateIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyEndDateIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void whenStartAndEndDatesAreNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartAndEndDateAreNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void whenTheRankingDatesAreNullAndStartAndEndDateAreNotValid_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingStartAndEndDateAreNullButStartAndEndAreNotValid_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }
    @Test
    void whenTheRankingDatesAreNullButStartAndEndAreValid_InitiativeGeneralDTO_thenValidationArePassed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingStartAndEndDateAreNullAndStartAndEndAreValid_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertTrue(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void whenStartDateIsAfterEndDate_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartAfterEnd_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }
    @Test
    void whenRankingStartIsAfterRankingEnd_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingStartAfterRankingEnd_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(2);
    }
    @Test
    void whenRankingEndIsAfterStartDate_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingEndAfterStartDate_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }
    @Test
    void whenStartDateBeforeRankingDates_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartDateBeforeRankingDates_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void when_DatesAreALLPresentAndValid_thenValidationArePassed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralAllDatesValid_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertTrue(violations.isEmpty());
        //or
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void when_BeneficiaryBudgetAndBudgetAreValid_thenValidationArePassed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetValid_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO);

        assertTrue(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(0);
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

    private InitiativeGeneralDTO createInitiativeGeneralOnlyRankingStartIsNull_ko() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingEndDate = LocalDate.now();
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyRankingEndIsNull_ko() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate startDate = rankingStartDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyStartDateIsNull_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate endDate = rankingEndDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralOnlyEndDateIsNull_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralStartAndEndDateAreNull_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAndEndDateAreNullButStartAndEndAreNotValid_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAndEndDateAreNullAndStartAndEndAreValid_ok(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralStartAfterEnd_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(2);
        LocalDate endDate = rankingEndDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAfterRankingEnd_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.minusDays(1);
        LocalDate startDate = rankingStartDate.plusDays(2);
        LocalDate endDate = rankingStartDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralRankingEndAfterStartDate_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(3);
        LocalDate startDate = rankingStartDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(5);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralAllDatesValid_ok(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(5);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralStartDateBeforeRankingDates_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate startDate = LocalDate.now();
        LocalDate rankingStartDate = startDate.plusDays(1);
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate endDate = rankingEndDate.plusDays(5);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralBudgetValid_ok(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(5);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        return initiativeGeneralDTO;
    }
}
