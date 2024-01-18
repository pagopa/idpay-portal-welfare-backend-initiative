package it.gov.pagopa.initiative.constants;


import java.util.List;
@SuppressWarnings("java:S1118")
public class InitiativeConstants {

    private InitiativeConstants(){}

    public static final class Status {
        public static final List<String> INITIATIVE_STATUS_LIST_FOR_PAGOPA_ADMIN_OPERATOR = List.of(InitiativeConstants.Status.IN_REVISION, InitiativeConstants.Status.TO_CHECK, InitiativeConstants.Status.APPROVED, InitiativeConstants.Status.PUBLISHED);
        public static final List<String> INITIATIVE_STATUS_LIST_FOR_ADMIN_OPERATOR = List.of(InitiativeConstants.Status.DRAFT, InitiativeConstants.Status.IN_REVISION, InitiativeConstants.Status.TO_CHECK, InitiativeConstants.Status.APPROVED, InitiativeConstants.Status.PUBLISHED, InitiativeConstants.Status.SUSPENDED, InitiativeConstants.Status.CLOSED);
        public static final String DRAFT = "DRAFT"; //In bozza
        public static final String IN_REVISION = "IN_REVISION"; //In revisione
        public static final String TO_CHECK = "TO_CHECK"; //Da controllare/revisionare
        public static final String APPROVED = "APPROVED"; //Approvata
        public static final String PUBLISHED = "PUBLISHED"; //In corso / Pubblicata
        public static final String CLOSED = "CLOSED"; //Terminata
        public static final String SUSPENDED = "SUSPENDED"; //Sospesa

        public static final class Validation {
            public static final String REWARD_PERCENTAGE = "PERCENTAGE";
            public static final String REWARD_ABSOLUTE = "ABSOLUTE";
            public static final String [] INITIATIVES_ALLOWED_STATES_TO_BE_EDITABLE_ARRAY = {Status.DRAFT, Status.TO_CHECK, Status.APPROVED};
            public static final String [] INITIATIVE_ALLOWED_STATES_TO_BECOME_PUBLISHED_ARRAY = {Status.APPROVED};
        }
    }

    public static final class Logo {
        public static final String LOGO_PATH_TEMPLATE = "assets/logo/%s/%s/%s";
        public static final String LOGO_NAME = "logo.png";
    }

    public static final class Role {
        public static final String ADMIN = "admin";
        public static final String PAGOPA_ADMIN = "pagopa_admin";
    }

    public static final class Email {
        public static final String TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS = "Email_InitiativeStatus";
        public static final String TEMPLATE_NAME_EMAIL_INITIATIVE_CREATED = "Email_InitiativeCreated";
        public static final String SUBJECT_CHANGE_STATE = "Cambio stato iniziativa per il prodotto IdPay";
        public static final String SUBJECT_INITIATIVE_CREATED = "Nuova Iniziativa";
    }

    public static final class CtaConstant {
        public static final String START = "---\n";
        public static final String IT = "it:\n    ";
        public static final String CTA_1_IT = "cta_1: \n        ";
        public static final String TEXT_IT = "text: \"Richiedi il Bonus\"\n        ";
        public static final String ACTION_IT = "action: \"ioit://idpay/onboarding/";
        public static final String EN = "\"\nen:\n    ";
        public static final String CTA_1_EN = "cta_1: \n        ";
        public static final String TEXT_EN = "text: \"Request Bonus\"\n        ";
        public static final String ACTION_EN = "action: \"ioit://idpay/onboarding/";
        public static final String END = "\"\n---";
    }

    public static final class FamilyUnitCompositionConstant {
        public static final String INPS = "INPS";
        public static final String ANPR = "ANPR";
    }

    public static final class Exception extends AbstractConstant {

        public static final class ErrorDtoDefaultMsg {
            public static final String ACCUMULATED_AMOUNT_TYPE = "Something wrong with the accumulated refund type selected";
            public static final String SOMETHING_WRONG_WITH_THE_REFUND_TYPE = "Something wrong with the refund type";
        }

        public static final class BadRequest { //400
            public static final String INITIATIVE_ROLLBACK_TO_PREVIOUS_STATUS = "INITIATIVE_ROLLBACK_TO_PREVIOUS_STATUS";
            public static final String INITIATIVE_WHITELIST_INVALID_PROPERTIES = "INITIATIVE_WHITELIST_INVALID_PROPERTIES";
            public static final String INITIATIVE_INVALID_LOCALE_FORMAT = "INITIATIVE_INVALID_LOCALE_FORMAT";
            public static final String INITIATIVE_ITALIAN_LANGUAGE_REQUIRED_FOR_DESCRIPTION = "INITIATIVE_ITALIAN_LANGUAGE_REQUIRED_FOR_DESCRIPTION";
            public static final String INITIATIVE_CANNOT_BE_DELETED = "INITIATIVE_CANNOT_BE_DELETED";
            public static final String INITIATIVE_ADMIN_ROLE_NOT_ALLOWED = "INITIATIVE_ADMIN_ROLE_NOT_ALLOWED";
            public static final String INITIATIVE_STATUS_NOT_VALID = "INITIATIVE_STATUS_NOT_VALID";
            public static final String INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_ORDER_DIRECTION_MISSING = "INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_ORDER_DIRECTION_MISSING";
            public static final String INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_EQUALS_OPERATOR = "INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_EQUALS_OPERATOR";
            public static final String INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_ISEE_MISSING = "INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_ISEE_MISSING";
            public static final String INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_TYPOLOGY_ISEE_MISSING = "INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_TYPOLOGY_ISEE_MISSING";
            public static final String INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_BENEFICIARY_NF_ISEE_MISSING = "INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_BENEFICIARY_NF_ISEE_MISSING";
            public static final String INITIATIVE_VALIDATION_WIZARD_VIOLATIONS = "INITIATIVE_VALIDATION_WIZARD_VIOLATIONS";
            public static final String INITIATIVE_REWARD_RULES_NOT_VALID = "INITIATIVE_REWARD_RULES_NOT_VALID";
            public static final String INITIATIVE_REFUND_RULES_NOT_VALID = "INITIATIVE_REFUND_RULES_NOT_VALID";
            public static final String INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID = "INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID";
            public static final String INITIATIVE_START_DATE_AND_END_DATE_NOT_VALID = "INITIATIVE_START_DATE_AND_END_DATE_NOT_VALID";
            public static final String INITIATIVE_YEAR_VALUE_NOT_VALID = "INITIATIVE_YEAR_VALUE_NOT_VALID";
            public static final String INITIATIVE_INVALID_REQUEST = "INITIATIVE_INVALID_REQUEST";
            public static final String INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_NOT_VALID_END_DATE = "INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_NOT_VALID_END_DATE";
        }

        public static final class NotFound { //404
            public static final String INITIATIVE_NOT_FOUND = "INITIATIVE_NOT_FOUND";
            public static final String INITIATIVE_NOT_FOUND_MESSAGE = "Initiative with initiativeId [%s] not found";
        }
        public static final class UnprocessableEntity { //422
            public static final String CODE = BASE_CODE + ".unprocessable.entity";
        }
        public static final class InternalServerError { //500 (5xx)
            public static final String INITIATIVE_LOGO_ERROR = "INITIATIVE_LOGO_ERROR";
            public static final String INITIATIVE_GENERIC_ERROR = "INITIATIVE_GENERIC_ERROR";
        }
    }
}
