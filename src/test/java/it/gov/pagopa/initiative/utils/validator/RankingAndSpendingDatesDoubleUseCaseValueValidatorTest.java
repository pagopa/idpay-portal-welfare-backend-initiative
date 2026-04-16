package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    }

    @Test
    void when_BeneficiaryBudgetAndBudgetAreValid_thenValidationArePassed(){
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetValid_ok();
        initiativeGeneralDTO.setBudget(BigDecimal.valueOf(1000000));
        initiativeGeneralDTO.setBeneficiaryBudget(BigDecimal.valueOf(100));
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations = validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.plus(11, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO_ko() {
        Map<String, String> lang = new HashMap<>();
        lang.put(Locale.ITALIAN.getLanguage(), "it");

        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.minus(5, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(lang);

        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyRankingStartIsNull_ko() {
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        Instant rankingEndDate = Instant.now();
        Instant startDate = rankingEndDate.plus(10, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant startDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant endDate = rankingEndDate.plus(1, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.plus(10, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        Instant startDate = Instant.now();
        Instant endDate = startDate.minus(1, ChronoUnit.DAYS);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAndEndDateAreNullAndStartAndEndAreValid_ok(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        Instant startDate = Instant.now();
        Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
    private InitiativeGeneralDTO createInitiativeGeneralStartAfterEnd_ko(){
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.plus(2, ChronoUnit.DAYS);
        Instant endDate = rankingEndDate.plus(1, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.minus(1, ChronoUnit.DAYS);
        Instant startDate = rankingStartDate.plus(2, ChronoUnit.DAYS);
        Instant endDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(3, ChronoUnit.DAYS);
        Instant startDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(5, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.plus(11, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(5, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        Instant startDate = Instant.now();
        Instant rankingStartDate = startDate.plus(1, ChronoUnit.DAYS);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant endDate = rankingEndDate.plus(5, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.plus(11, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(5, ChronoUnit.DAYS);
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
        initiativeGeneralDTO.setBeneficiaryBudgetMax(null);
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
         Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.plus(10, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(5, ChronoUnit.DAYS);
        initiativeGeneralDTO.setRankingEnabled(true);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
}
