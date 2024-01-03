package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class ValidationWizardException extends ServiceException {
    public ValidationWizardException(String message) {
        this(BadRequest.INITIATIVE_VALIDATION_WIZARD_VIOLATIONS, message);
    }

    public ValidationWizardException(String code, String message) {
        this(code, message, null,false,null);
    }

    public ValidationWizardException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
