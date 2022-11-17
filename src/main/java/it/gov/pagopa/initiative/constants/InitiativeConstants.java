package it.gov.pagopa.initiative.constants;


public class InitiativeConstants {

    private InitiativeConstants(){}

    public static final class Status {
        public static final String DRAFT = "DRAFT"; //In bozza
        public static final String IN_REVISION = "IN_REVISION"; //In revisione
        public static final String TO_CHECK = "TO_CHECK"; //Da controllare/revisionare
        public static final String APPROVED = "APPROVED"; //Approvata
        public static final String PUBLISHED = "PUBLISHED"; //In corso / Pubblicata
        public static final String CLOSED = "CLOSED"; //Terminata
        public static final String SUSPENDED = "SUSPENDED"; //Sospesa

        public static final class Validation {
            public static final String [] INITIATIVES_ALLOWED_STATES_TO_BE_EDITABLE_ARRAY = {Status.DRAFT, Status.TO_CHECK, Status.APPROVED};
            public static final String [] INITIATIVE_ALLOWED_STATES_TO_BECOME_PUBLISHED_ARRAY = {Status.APPROVED};
        }
    }

    public static final class Role {
        public static final String ADMIN = "admin";
        public static final String OPE_BASE = "ope_base";
    }

    public static final class Email {
        public static final String TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS = "Email_InitiativeStatus";
        public static final String TEMPLATE_NAME_EMAIL_INITIATIVE_CREATED = "Email_InitiativeCreated";
        public static final String SUBJECT_CHANGE_STATE = "Cambio stato iniziativa per il prodotto IdPay";
        public static final String SUBJECT_INITIATIVE_CREATED = "Nuova Iniziativa";
        public static final String RECIPIENT_ASSISTANCE = "Assistenza.IDPay@Pagopa.it";

    }

    public static final class Exception extends AbstractConstant {

        public static final class ErrorDtoDefaultMsg {
            public static final String ACCUMULATED_AMOUNT_TYPE = "Something wrong with the accumulated refund type selected";
            public static final String SOMETHING_WRONG_WITH_THE_REFUND_TYPE = "Something wrong with the refund type";
        }
        public static final class BadRequest { //400
            public static final String CODE = BASE_CODE + ".bad.request";
            public static final String INITIATIVE_CURRENT_STATUS_NOT_IN_REVISION = "Current initiative status is not IN_REVISION";

            public static final String INITIATIVE_STATUS_NOT_IN_REVISION = "The status of initiative [%s] is not IN_REVISION";
            public static final String INITIATIVE_BY_INITIATIVE_ID_PROPERTIES_NOT_VALID = "Initiative %s properties are not valid for this request";
            public static final String INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID = "Initiative %s unprocessable for status not valid";

            public static final String INITIATIVE_CANNOT_BE_DELETED = "Initiative %s cannot be deleted";
            public static final String PERMISSION_NOT_VALID= "Permission not valid for [%s]";
        }
        public static final class Publish {
            public static final String PUBLISH_CODE = BASE_CODE + ".published";
            public static final class BadRequest { //400
                public static final String CODE = PUBLISH_CODE + ".bad.request";
                public static final String INTEGRATION_FAILED = "Something gone wrong while notify Initiative for publishing";
            }

        }
        public static final class NotFound { //404
            public static final String CODE = BASE_CODE + ".not.found";
            public static final String INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE = "List of Initiatives with organizationId %s not found.";
            public static final String INITIATIVE_BY_INITIATIVE_ID_MESSAGE = "Initiative with initiativeId %s not found.";
            public static final String INITIATIVE_ID_BY_SERVICE_ID_MESSAGE = "Initiative with serviceId %s not found.";
            public static final String PRIMARY_AND_SECONDARY_TOKEN_MESSAGE = "Primary and secondary tokenIO not found for initiativeId %s.";
        }
        public static final class UnprocessableEntity { //422
            public static final String CODE = BASE_CODE + ".unprocessable.entity";
        }
        public static final class InternalServerError { //500 (5xx)
            public static final String CODE = BASE_CODE + ".internal-server-error";
            public static final String INTEGRATION_CODE = CODE + ".integration";
        }
    }
}
