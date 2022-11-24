package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.RankingAndSpendingDatesDoubleUseCaseValue;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Return true only in cases where:
 *         - all dates are present and are also compatible with each other.
 *         - there are no ranking dates but the start and end dates are present and compatible with each other.
 * for all other cases, false.
 */
@Slf4j
public class RankingAndSpendingDatesDoubleUseCaseValidator implements ConstraintValidator<RankingAndSpendingDatesDoubleUseCaseValue, InitiativeGeneralDTO> {

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        LocalDate rankingStartDate = value.getRankingStartDate();
        LocalDate rankingEndDate = value.getRankingEndDate();
        LocalDate startDate = value.getStartDate();
        LocalDate endDate = value.getEndDate();

        if (startDate != null && endDate != null){//if both start and end buy dates are not present, then Violation! return false.
            log.debug("start and end date not null");
            if (rankingStartDate != null){//if dates are all present, they are checked.
                if (rankingEndDate != null){
                    return rankingStartDate.isBefore(rankingEndDate) && rankingEndDate.isBefore(startDate) && startDate.isBefore(endDate);
                }
            } else { //if we have only start and end buy dates, those are checked.
                if (rankingEndDate == null){
                    return startDate.isBefore(endDate);
                }
            }
        }
        return false;

    }
}
