package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants;

public class CommandProducerException extends ServiceException {
    public CommandProducerException(String message) {
        this(InitiativeConstants.Exception.InternalServerError.INITIATIVE_GENERIC_ERROR, message);
    }

    public CommandProducerException(String code, String message) {
        this(code, message, null,false,null);
    }

    public CommandProducerException
            (String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
