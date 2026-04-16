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
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(false);
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
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryBudgetMax(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(false);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        Instant rankingStartDate = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant rankingEndDate = rankingStartDate.plus(1, ChronoUnit.DAYS);
        Instant startDate = rankingEndDate.plus(10, ChronoUnit.DAYS);
        Instant endDate = startDate.plus(1, ChronoUnit.DAYS);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }
}
