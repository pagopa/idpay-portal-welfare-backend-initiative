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

    @Test
    void whenAllValidationAreValid_InitiativeGeneralDTO_thenValidationArePassed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenStartDateEndDateAreEqual_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenOnlyRankingStartIsNull_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyRankingStartIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenOnlyRankingEndIsNull_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyRankingEndIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenOnlyStartDateIsNull_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyStartDateIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void whenOnlyEndDateIsNull_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralOnlyEndDateIsNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void whenStartAndEndDatesAreNull_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartAndEndDateAreNull_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(3);
    }

    @Test
    void whenTheRankingDatesAreNullAndStartAndEndDateAreNotValid_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO =
                createInitiativeGeneralRankingStartAndEndDateAreNullButStartAndEndAreNotValid_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenTheRankingDatesAreNullButStartAndEndAreValid_InitiativeGeneralDTO_thenValidationArePassed() {
        InitiativeGeneralDTO initiativeGeneralDTO =
                createInitiativeGeneralRankingStartAndEndDateAreNullAndStartAndEndAreValid_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenStartDateIsAfterEndDate_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartAfterEnd_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenRankingStartIsAfterRankingEnd_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingStartAfterRankingEnd_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenRankingEndIsAfterStartDate_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralRankingEndAfterStartDate_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void whenStartDateBeforeRankingDates_InitiativeGeneralDTO_thenValidationAreFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralStartDateBeforeRankingDates_ko();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void when_DatesAreALLPresentAndValid_thenValidationArePassed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralAllDatesValid_ok();
        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void when_BeneficiaryBudgetAndBudgetAreValid_thenValidationArePassed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetValid_ok();
        initiativeGeneralDTO.setBudget(BigDecimal.valueOf(1_000_000L));
        initiativeGeneralDTO.setBeneficiaryBudgetFixed(new BigDecimal(1000));

        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void when_beneficiaryBudgetIsNull_thenValidationPassed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetValid_ok();
        initiativeGeneralDTO.setBeneficiaryBudgetFixed(new BigDecimal(1000));

        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void when_budgetIsNull_thenValidationFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetValid_ok();
        initiativeGeneralDTO.setBudget(null);

        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
    }

    @Test
    void when_beneficiaryBudgetAndBudgetAreNull_thenValidationFailed() {
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralBudgetValid_ok();
        initiativeGeneralDTO.setBudget(null);
        initiativeGeneralDTO.setBeneficiaryBudgetFixed(new BigDecimal(1000));

        Set<ConstraintViolation<InitiativeGeneralDTO>> violations =
                validator.validate(initiativeGeneralDTO, ValidationApiEnabledGroup.class);

        assertFalse(violations.isEmpty());
    }

    private InitiativeGeneralDTO createBaseValidInitiativeGeneralDTO() {
        InitiativeGeneralDTO dto = new InitiativeGeneralDTO();

        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");

        dto.setBeneficiaryKnown(true);
        dto.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);

        dto.setBudget(BigDecimal.valueOf(1_000_000_000L));
        dto.setBeneficiaryBudgetFixed(BigDecimal.valueOf(1_000L));

        dto.setRankingEnabled(true);
        dto.setDescriptionMap(language);

        LocalDate rankingStartDate = LocalDate.now().plusDays(1);
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(11);
        LocalDate endDate = startDate.plusDays(1);

        dto.setRankingStartDate(rankingStartDate);
        dto.setRankingEndDate(rankingEndDate);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);

        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO_ok() {
        return createBaseValidInitiativeGeneralDTO();
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        dto.setRankingEndDate(dto.getRankingStartDate());
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyRankingStartIsNull_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        dto.setRankingStartDate(null);
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyRankingEndIsNull_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        dto.setRankingEndDate(null);
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyStartDateIsNull_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        dto.setStartDate(null);
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralOnlyEndDateIsNull_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        dto.setEndDate(null);
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralStartAndEndDateAreNull_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        dto.setStartDate(null);
        dto.setEndDate(null);
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAndEndDateAreNullButStartAndEndAreNotValid_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = startDate.minusDays(1);

        dto.setRankingStartDate(null);
        dto.setRankingEndDate(null);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);

        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAndEndDateAreNullAndStartAndEndAreValid_ok() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = startDate.plusDays(1);

        dto.setRankingStartDate(null);
        dto.setRankingEndDate(null);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);

        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralStartAfterEnd_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        LocalDate startDate = dto.getRankingEndDate().plusDays(12);
        LocalDate endDate = dto.getRankingEndDate().plusDays(11);

        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralRankingStartAfterRankingEnd_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        LocalDate rankingStartDate = LocalDate.now().plusDays(3);
        LocalDate rankingEndDate = LocalDate.now().plusDays(2);

        dto.setRankingStartDate(rankingStartDate);
        dto.setRankingEndDate(rankingEndDate);
        dto.setStartDate(rankingStartDate.plusDays(11));
        dto.setEndDate(rankingStartDate.plusDays(12));

        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralRankingEndAfterStartDate_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        LocalDate rankingStartDate = LocalDate.now().plusDays(1);
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate rankingEndDate = LocalDate.now().plusDays(3);
        LocalDate endDate = startDate.plusDays(5);

        dto.setRankingStartDate(rankingStartDate);
        dto.setRankingEndDate(rankingEndDate);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);

        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralStartDateBeforeRankingDates_ko() {
        InitiativeGeneralDTO dto = createBaseValidInitiativeGeneralDTO();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate rankingStartDate = LocalDate.now().plusDays(2);
        LocalDate rankingEndDate = LocalDate.now().plusDays(3);
        LocalDate endDate = LocalDate.now().plusDays(10);

        dto.setStartDate(startDate);
        dto.setRankingStartDate(rankingStartDate);
        dto.setRankingEndDate(rankingEndDate);
        dto.setEndDate(endDate);

        return dto;
    }

    private InitiativeGeneralDTO createInitiativeGeneralAllDatesValid_ok() {
        return createBaseValidInitiativeGeneralDTO();
    }

    private InitiativeGeneralDTO createInitiativeGeneralBudgetValid_ok() {
        return createBaseValidInitiativeGeneralDTO();
    }
}