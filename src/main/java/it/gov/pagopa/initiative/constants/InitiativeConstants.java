package it.gov.pagopa.initiative.constants;


import java.util.List;

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

        public static final class GeneralError { //500
            public static final String CODE = BASE_CODE + ".general.error";
        }

        public static final class BadRequest { //400
            public static final String CODE = BASE_CODE + ".bad.request";
            public static final String INITIATIVE_BENEFICIARY_RANKING_ENABLED_AUTOMATED_CRITERIA_ORDER_OPERATION_ISEE_EQ_OP_NOT_VALID = "Automated criteria not valid. OrderDirection not feasible. When Ranking enabled, It must be used ISEE only with non 'Equals' operator";
            public static final String INITIATIVE_BENEFICIARY_RANKING_ENABLED_AUTOMATED_CRITERIA_ORDER_OPERATION_MISSING_NOT_VALID = "Automated criteria not valid. OrderDirection is Missing. Please check it when Ranking is enabled";
            public static final String INITIATIVE_BENEFICIARY_RANKING_ENABLED_AUTOMATED_CRITERIA_ISEE_MISSING_NOT_VALID = "Automated criteria not valid. ISEE is Missing. Please check it when Ranking is enabled";
            public static final String INITIATIVE_BENEFICIARY_TYPE_NF_ENABLED_AUTOMATED_CRITERIA_ISEE_MISSING_NOT_VALID = "Automated criteria not valid. ISEE is Missing. Please check it when beneficiary type 'NF' is selected";
            public static final String INITIATIVE_BENEFICIARY_FIELD_YEAR_VALUE_WRONG = "The value must contain 4 numbers and the year cannot be less than 150 years";
            public static final String INITIATIVE_STATUS_NOT_IN_REVISION = "The status of initiative [%s] is not IN_REVISION";
            public static final String INITIATIVE_BY_INITIATIVE_ID_PROPERTIES_NOT_VALID = "Initiative %s properties are not valid for this request";
            public static final String INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID = "Initiative %s unprocessable for status not valid";
            public static final String INVALID_LOCALE_FORMAT = "Invalid locale format: %s";

            public static final String INITIATIVE_CANNOT_BE_DELETED = "Initiative %s cannot be deleted";
            public static final String PERMISSION_NOT_VALID= "Permission not valid for [%s]";
            public static final String INITIATIVE_DESCRIPTION_LANGUAGE_MESSAGE = "Italian language is required for initiative description";
            public static final String INITIATIVE_GENERAL_FAMILY_COMPOSITION_MESSAGE = "Family unit composition must be set as 'INPS' or 'ANPR'";
            public static final String INITIATIVE_GENERAL_FAMILY_COMPOSITION_WRONG_BENEFICIARY_TYPE = "Family unit composition must be unset for this beneficiary type";
            public static final String INITIATIVE_GENERAL_START_DATE_END_DATE_WRONG = "The startDate and endDate cannot be less than today";
            public static final String WIZARD_VALIDATION = "Error on Validation caused by: %s";
            public static final String REWARD_TYPE = "REWARD INVALID";
            public static final String REFUND_RULE_INVALID = "REFUND RULE INVALID";
            public static final String ISEE_TYPES_NOT_VALID = "Automated criteria not valid. ISEE typology is missing.";
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
            public static final String NO_RANKING = "Initiative without ranking";
            public static final String INTEGRATION_CODE = CODE + ".integration";
        }
    }
}
