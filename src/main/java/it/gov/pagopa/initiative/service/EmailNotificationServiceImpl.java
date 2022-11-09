package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.connector.selc.SelcRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.EmailTemplate.EMAIL_INITIATIVE_CREATED;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.EmailTemplate.EMAIL_INITIATIVE_STATUS;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final String SUBJECT_CHANGE_STATE = "Cambio stato iniziativa per il prodotto IdPay";
    private static final String RECIPIENTS_CHANGE_STATE_IN_REVISION = "Assistenza.IDPay@Pagopa.it";
    public static final String COMMA_DELIMITER = ",";
    private final EmailNotificationRestConnector emailNotificationRestConnector;
    private final SelcRestConnector selcRestConnector;

    public EmailNotificationServiceImpl(EmailNotificationRestConnector emailNotificationRestConnector, SelcRestConnector selcRestConnector) {
        this.emailNotificationRestConnector = emailNotificationRestConnector;
        this.selcRestConnector = selcRestConnector;
    }

    /* Invio Notifica e-mail per stati: REVISION, SUSPENDED, PUBLISHED, CLOSED */
    @Override
    public void sendInitiativeEntePagoPA(Initiative initiative, String organizationName) {
        Map<String, String> templateValues = getMap(initiative, organizationName);
        /*e-mail a PagoPA*/
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, EMAIL_INITIATIVE_STATUS, templateValues, SUBJECT_CHANGE_STATE, null, RECIPIENTS_CHANGE_STATE_IN_REVISION);
        /*e-mail ad amministratori Ente*/
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, EMAIL_INITIATIVE_STATUS, templateValues, SUBJECT_CHANGE_STATE, null, getInstitutionProductUsers(initiative.getOrganizationId(), InitiativeConstants.Role.ADMIN));
    }

    /* Invio Notifica e-mail per stati: APPROVED, TO_CHECK, DRAFT, DELETED */
    @Override
    public void sendInitiativeEnte(Initiative initiative, String organizationName) {
        Map<String, String> templateValues = getMap(initiative, organizationName);
        /*e-mail ad amministratori Ente*/
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, EMAIL_INITIATIVE_STATUS, templateValues, SUBJECT_CHANGE_STATE, null, getInstitutionProductUsers(initiative.getOrganizationId(), InitiativeConstants.Role.ADMIN));
    }

    /* Invio Notifica e-mail per stato: CREATED (template diverso) */
    @Override
    public void sendInitiativeEnteCreated(Initiative initiative, String organizationName) {
        Map<String, String> templateValues = getMap(initiative, organizationName);
        /*e-mail ad amministratori Ente*/
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, EMAIL_INITIATIVE_CREATED,
                templateValues, SUBJECT_CHANGE_STATE, null, getInstitutionProductUsers(initiative.getOrganizationId(), InitiativeConstants.Role.ADMIN));
    }

    @NotNull
    private Map<String, String> getMap(Initiative initiative, String organizationName) {
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("initiativeName", initiative.getInitiativeName());
        templateValues.put("orgName", organizationName);
        templateValues.put("status", initiative.getStatus());
        return templateValues;
    }

    private String getInstitutionProductUsers(String organizationId, String role) {
        String ret = StringUtils.EMPTY;
        try {
            List<UserResource> users = this.selcRestConnector.getInstitutionProductUsers(organizationId);
            ret = users.stream().filter(user -> user.getRoles().contains(role)).map(UserResource::getEmail).collect(Collectors.joining(COMMA_DELIMITER));
        } catch (Exception e) {
            log.error("[SELC] - Error retrieving product users for organization: " + organizationId, e);
        }
        return ret;
    }

    private String getInstitutionProductUser(String organizationId, String UUID) {
        String ret = StringUtils.EMPTY;
        try {
            List<UserResource> users = this.selcRestConnector.getInstitutionProductUsers(UUID);
            ret =
                    users.stream().filter(user -> user.getRoles().contains(UUID)).map(UserResource::getEmail).collect(Collectors.joining(COMMA_DELIMITER));
        } catch (Exception e) {
            log.error("[SELC] - Error retrieving product users for organization: " + organizationId, e);
        }
        return ret;
    }

}
