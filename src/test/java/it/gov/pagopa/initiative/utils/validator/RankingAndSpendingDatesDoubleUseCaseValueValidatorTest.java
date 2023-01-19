package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.initiative.ranking.gracePeriod=10"
        })
@SpringJUnitConfig
@ImportAutoConfiguration(ValidationAutoConfiguration.class)
class RankingAndSpendingDatesDoubleUseCaseValueValidatorTest {

    @Autowired
    private Validator validator;
    private final Map<String, String> language = new HashMap<>();

    @Test
    void whenAllValidationAreValid_InitiativeGeneralDTO_thenValidationArePassed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenStartDateEndDateAreEqual_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(3);
    }

    @Test
    void whenOnlyRankingStartIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyRankingStartIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(2);
    }

    @Test
    void whenOnlyRankingEndIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyRankingEndIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenOnlyStartDateIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyStartDateIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(2);
    }

    @Test
    void whenOnlyEndDateIsNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyEndDateIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(3);
    }

    @Test
    void whenStartAndEndDatesAreNull_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartAndEndDateAreNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(3);
    }

    @Test
    void whenTheRankingDatesAreNullAndStartAndEndDateAreNotValid_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingStartAndEndDateAreNullButStartAndEndAreNotValid_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(1);
    }
    @Test
    void whenTheRankingDatesAreNullButStartAndEndAreValid_InitiativeGeneralDTO_thenValidationArePassed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingStartAndEndDateAreNullAndStartAndEndAreValid_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
        //or
        assertThat(0).isZero();
    }

    @Test
    void whenStartDateIsAfterEndDate_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartAfterEnd_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(2);
    }
    @Test
    void whenRankingStartIsAfterRankingEnd_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingStartAfterRankingEnd_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(3);
    }
    @Test
    void whenRankingEndIsAfterStartDate_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingEndAfterStartDate_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(2);
    }
    @Test
    void whenStartDateBeforeRankingDates_InitiativeGeneralDTO_thenValidationAreFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartDateBeforeRankingDates_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        //or
        assertThat(violations).hasSize(2);
    }

    @Test
    void when_DatesAreALLPresentAndValid_thenValidationArePassed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralAllDatesValid_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
        //or
        assertThat(0).isZero();
    }

    @Test
    void when_BeneficiaryBudgetAndBudgetAreValid_thenValidationArePassed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetValid_ok();
        initiativeGeneralDTO.setBudget(BigDecimal.valueOf(1000000));
        initiativeGeneralDTO.setBeneficiaryBudget(BigDecimal.valueOf(100));
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
        assertThat(0).isZero();
    }

    @Test
    void when_beneficiaryBudgetIsNull_thenValidationFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetNotValid_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void when_budgetIsNull_thenValidationFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetNotValid_ko();
        initiativeGeneralDTO.setBeneficiaryBudget(null);
        initiativeGeneralDTO.setBudget(null);
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void when_beneficiaryBudgetAndBudgetAreNull_thenValidationFailed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetNotValid_ko();
        initiativeGeneralDTO.setBeneficiaryBudget(BigDecimal.valueOf(100000));
        initiativeGeneralDTO.setBudget(null);
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
    }
    private InitiativeGeneralDTO createInitiativeGeneralDTO_ok() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(11);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO_ko() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate startDate = rankingStartDate.plusDays(10);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingStartDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyRankingStartIsNull_ko() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingEndDate = LocalDate.now();
        LocalDate startDate = rankingEndDate.plusDays(10);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyRankingEndIsNull_ko() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate startDate = rankingStartDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyStartDateIsNull_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate endDate = rankingEndDate.plusDays(1);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralOnlyEndDateIsNull_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(10);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralStartAndEndDateAreNull_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAndEndDateAreNullButStartAndEndAreNotValid_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAndEndDateAreNullAndStartAndEndAreValid_ok(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralStartAfterEnd_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(2);
        LocalDate endDate = rankingEndDate.plusDays(1);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAfterRankingEnd_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.minusDays(1);
        LocalDate startDate = rankingStartDate.plusDays(2);
        LocalDate endDate = rankingStartDate.plusDays(1);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralRankingEndAfterStartDate_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(3);
        LocalDate startDate = rankingStartDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(5);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralAllDatesValid_ok(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(11);
        LocalDate endDate = startDate.plusDays(5);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralStartDateBeforeRankingDates_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate startDate = LocalDate.now();
        LocalDate rankingStartDate = startDate.plusDays(1);
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate endDate = rankingEndDate.plusDays(5);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralBudgetValid_ok(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(11);
        LocalDate endDate = startDate.plusDays(5);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralBudgetNotValid_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(null);
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(10);
        LocalDate endDate = startDate.plusDays(5);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
}
