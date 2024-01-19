package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class AdminPermissionException extends ServiceException {
    public AdminPermissionException(String message) {
        this(BadRequest.INITIATIVE_ADMIN_ROLE_NOT_ALLOWED, message);
    }

    public AdminPermissionException(String code, String message) {
        this(code, message, null,false,null);
    }

    public AdminPermissionException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
