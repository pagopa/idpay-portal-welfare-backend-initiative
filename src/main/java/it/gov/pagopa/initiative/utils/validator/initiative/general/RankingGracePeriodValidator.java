package it.gov.pagopa.initiative.utils.validator.initiative.general;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.initiative.general.RankingGracePeriodConstraint;
import org.springframework.beans.factory.annotation.Value;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RankingGracePeriodValidator implements ConstraintValidator<RankingGracePeriodConstraint, InitiativeGeneralDTO> {

    @Value("${app.initiative.ranking.gracePeriod}")
    private long gracePeriod;

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        Boolean rankingEnabled = value.getRankingEnabled();
        if(rankingEnabled != null && rankingEnabled){
            Instant rankingEndDate = value.getRankingEndDate();
            Instant startDate = value.getStartDate();
            if (startDate != null && rankingEndDate != null) {
                return rankingEndDate.plus(gracePeriod, ChronoUnit.DAYS).isBefore(startDate);
            }
        }
        return true;
    }
}
