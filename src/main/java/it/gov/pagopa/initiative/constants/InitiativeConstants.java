package it.gov.pagopa.initiative.constants;


public class InitiativeConstants {
    private InitiativeConstants(){}

    public static final class Status {
        public static final String DRAFT = "DRAFT";
    }

    public static final class Exception {
        public static final String NOT_FOUND_MESSAGE = "Initiative with initiativeId %s not found.";
        public static final String NOT_FOUND = "it.gov.pagopa.initiative.not.found";
        public static final class NotFound {
            public static final String CODE_PACKAGE = "it.gov.pagopa.initiative.not.found";
            public static final String INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE = "List of Initiatives with organizationId {0} not found.";
            public static final String INITIATIVE_BY_INITIATIVE_ID_MESSAGE = "Initiative with initiativeId {0} not found.";
            public static final String INITIATIVE_BY_INITIATIVE_ID_ORGANIZATION_ID_MESSAGE = "Initiative with organizationId {0} and initiativeId {1} not found.";
        }
        public static final class BadRequest {
            public static final String CODE_PACKAGE = "it.gov.pagopa.initiative.bad.request";
            public static final String INITIATIVE_PROPERTIES_NOT_VALID = "Initiative {0} properties are not valid for this request";
        }
    }
}
