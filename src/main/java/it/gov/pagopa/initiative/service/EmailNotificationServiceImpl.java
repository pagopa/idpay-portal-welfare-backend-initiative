package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.model.Initiative;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.EmailTemplate.EMAIL_INITIATIVE_STATUS;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final String SUBJECT_CHANGE_STATE = "Cambio stato iniziativa per il prodotto IdPay";
    private static final String RECIPIENTS_CHANGE_STATE_IN_REVISION = "Assistenza.IDPay@Pagopa.it";
    private final EmailNotificationRestConnector emailNotificationRestConnector;

    public EmailNotificationServiceImpl(
            EmailNotificationRestConnector emailNotificationRestConnector
    ) {
        this.emailNotificationRestConnector = emailNotificationRestConnector;
    }

    @Override
    @Async
    public void sendInitiativeInRevision(Initiative initiative, String organizationName) {
        Map<String, String> templateValues = getMap(initiative, organizationName);
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, EMAIL_INITIATIVE_STATUS, templateValues, SUBJECT_CHANGE_STATE, RECIPIENTS_CHANGE_STATE_IN_REVISION);
    }

    @Override
    @Async
    public void sendInitiativeApprovedAndRejected(Initiative initiative, String organizationName) {
        Map<String, String> templateValues = getMap(initiative, organizationName);
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, EMAIL_INITIATIVE_STATUS, templateValues, SUBJECT_CHANGE_STATE, "TODO");
    }

    @NotNull
    private static Map<String, String> getMap(Initiative initiative, String organizationName) {
        Map<String,String> templateValues = new HashMap<>();
        templateValues.put("initiativeName", initiative.getInitiativeName());
        templateValues.put("orgName", organizationName);
        templateValues.put("status", initiative.getStatus());
        return templateValues;
    }
}
