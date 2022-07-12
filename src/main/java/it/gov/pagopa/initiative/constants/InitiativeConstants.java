package it.gov.pagopa.initiative.constants;


public class InitiativeConstants {
    private InitiativeConstants(){}

    public static final class Status {
        public static final String DRAFT = "DRAFT";
    }

    public static final class Exception {
        public static final String NOT_FOUND_MESSAGE = "Initiative with initiativeId %s not found.";
        public static final String NOT_FOUND = "it.gov.pagopa.initiative.not.found";
    }
}
