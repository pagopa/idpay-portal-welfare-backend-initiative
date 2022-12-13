package it.gov.pagopa.initiative.utils.validator.initiative.general;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.initiative.general.RankingGracePeriodConstraint;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RankingGracePeriodValidator implements ConstraintValidator<RankingGracePeriodConstraint, InitiativeGeneralDTO> {

    @Value("${app.initiative.ranking.gracePeriod}")
    private long gracePeriod;

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        Boolean rankingEnabled = value.getRankingEnabled();
        if(rankingEnabled != null && rankingEnabled){
            LocalDate rankingEndDate = value.getRankingEndDate();
            LocalDate startDate = value.getStartDate();
            if (startDate != null && rankingEndDate != null) {
                return rankingEndDate.plusDays(gracePeriod).isBefore(startDate);
            }
        }
        return true;
    }
}
