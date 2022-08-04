package it.gov.pagopa.initiative.constants;


public class InitiativeConstants {

    private InitiativeConstants(){}

    public static final class Status {
        public static final String DRAFT = "DRAFT";
        public static final String TO_CHECK = "TO_CHECK";
        public static final String IN_REVISION = "IN_REVISION";
        public static final String APPROVED = "APPROVED";
        public static final String PUBLISHED = "PUBLISHED";
        public static final String CLOSED = "CLOSED";
        public static final String SUSPENDED = "SUSPENDED";
    }

    public static final class Exception {

        public static final class NotFound {
            public static final String CODE = "it.gov.pagopa.initiative.not.found";
            public static final String INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE = "List of Initiatives with organizationId {0} not found.";
            public static final String INITIATIVE_BY_INITIATIVE_ID_MESSAGE = "Initiative with initiativeId {0} not found.";
            public static final String INITIATIVE_BY_INITIATIVE_ID_ORGANIZATION_ID_MESSAGE = "Initiative with organizationId {0} and initiativeId {1} not found.";
        }
        public static final class BadRequest {
            public static final String CODE = "it.gov.pagopa.initiative.bad.request";
            public static final String INITIATIVE_PROPERTIES_NOT_VALID = "Initiative {0} properties are not valid for this request";
        }
    }
}
