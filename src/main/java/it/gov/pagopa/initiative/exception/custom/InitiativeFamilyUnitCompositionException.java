package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativeFamilyUnitCompositionException extends ServiceException {
    public InitiativeFamilyUnitCompositionException(String message) {
        this(BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, message);
    }

    public InitiativeFamilyUnitCompositionException(String code, String message) {
        this(code, message, null,false,null);
    }

    public InitiativeFamilyUnitCompositionException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
