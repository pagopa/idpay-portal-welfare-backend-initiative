package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.connector.selc.SelcRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.EmailTemplate.EMAIL_INITIATIVE_STATUS;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final String SUBJECT_CHANGE_STATE = "Cambio stato iniziativa per il prodotto IdPay";
    private static final String RECIPIENTS_CHANGE_STATE_IN_REVISION = "Assistenza.IDPay@Pagopa.it";
    public static final String COMMA_DELIMITER = ";";
    private final EmailNotificationRestConnector emailNotificationRestConnector;
    private final SelcRestConnector selcRestConnector;

    public EmailNotificationServiceImpl(
            EmailNotificationRestConnector emailNotificationRestConnector,
            SelcRestConnector selcRestConnector
    ) {
        this.emailNotificationRestConnector = emailNotificationRestConnector;
        this.selcRestConnector = selcRestConnector;
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
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative,
                EMAIL_INITIATIVE_STATUS, templateValues, SUBJECT_CHANGE_STATE,
                getInstitutionProductUsers(initiative.getOrganizationId(), InitiativeConstants.Role.ADMIN)
        );
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
           ret = users.stream().filter(user -> user.getRoles().contains(role))
                   .map(UserResource::getEmail)
                   .collect(Collectors.joining(COMMA_DELIMITER));
        } catch (Exception e) {
            log.error("[SELC] - Error retrieving product users for organization: " + organizationId, e);
        }
        return ret;
    }

}
