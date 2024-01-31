package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.Forbidden;

public class OrgPermissionException extends ServiceException {
    public OrgPermissionException(String message) {
        this(Forbidden.INITIATIVE_ORG_ROLE_NOT_ALLOWED, message);
    }

    public OrgPermissionException(String code, String message) {
        this(code, message, null,false,null);
    }

    public OrgPermissionException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
