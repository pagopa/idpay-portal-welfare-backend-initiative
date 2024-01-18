package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.exception.custom.InitiativeStatusNotValidException;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class InitiativeServiceRoot {

    void insertTechnicalData(Initiative initiative){
        initiative.setEnabled(true);
    }

    void isInitiativeAllowedToBeEditableThenThrows(Initiative initiative){
        if(Arrays.asList(InitiativeConstants.Status.Validation.INITIATIVES_ALLOWED_STATES_TO_BE_EDITABLE_ARRAY).contains(initiative.getStatus())){
            return;
        }
        throw new InitiativeStatusNotValidException(
            "Initiative [%s] with status [%s] is unprocessable for status not valid".formatted(initiative.getInitiativeId(), initiative.getStatus())
        );
    }

    void isInitiativeStatusNotInRevisionThenThrow(Initiative initiative, String nextStatus){
        if (initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION)){
            log.info("[UPDATE_TO_{}_STATUS] - Initiative: {}. Current status is valid", nextStatus, initiative.getInitiativeId());
            return;
        }
        log.info("[UPDATE_TO_{}_STATUS] - Initiative: {}. Current status is not IN_REVISION", nextStatus, initiative.getInitiativeId());
        throw new InitiativeStatusNotValidException(
                "The status of initiative [%s] is not IN_REVISION".formatted(initiative.getInitiativeId())
        );
    }

}
