package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

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
class RankingEnabledNotNullForBeneficiaryKnownFalseValidatorTest {

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
        assertThat(violations).hasSize(1);
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO_ok() {
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudgetCents(1000L);
        initiativeGeneralDTO.setBeneficiaryKnown(false);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudgetCents(100000000000L);
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
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudgetCents(1000L);
        initiativeGeneralDTO.setBeneficiaryKnown(false);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudgetCents(100000000000L);
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(10);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
}
