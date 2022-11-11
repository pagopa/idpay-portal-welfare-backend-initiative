package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.connector.selc.SelcRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.controller.filter.LoginThreadLocal;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import it.gov.pagopa.initiative.exception.IntegrationException;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final String SUBJECT_CHANGE_STATE = "Cambio stato iniziativa per il prodotto IdPay";
    private static final String RECIPIENTS_CHANGE_STATE_IN_REVISION = "Assistenza.IDPay@Pagopa.it";
    public static final String COMMA_DELIMITER = ",";
    private final EmailNotificationRestConnector emailNotificationRestConnector;
    private final SelcRestConnector selcRestConnector;
    private final LoginThreadLocal loginThreadLocal;

    public EmailNotificationServiceImpl(
            EmailNotificationRestConnector emailNotificationRestConnector,
            SelcRestConnector selcRestConnector,
            LoginThreadLocal LoginThreadLocal) {
        this.emailNotificationRestConnector = emailNotificationRestConnector;
        this.selcRestConnector = selcRestConnector;
        this.loginThreadLocal = LoginThreadLocal;
    }

    @Override
    public void sendInitiativeToCurrentOrganizationAndPagoPA(Initiative initiative, String templateName) {
        String institutionProductUsersEmailsByRole = getInstitutionProductUsersEmailsByRole(initiative.getOrganizationId(), InitiativeConstants.Role.ADMIN);
        UserResource institutionProductCurrentUser = getInstitutionProductCurrentUser(initiative.getOrganizationId());
        Map<String, String> templateValues = getMap(initiative, institutionProductCurrentUser);
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, templateName, templateValues, SUBJECT_CHANGE_STATE, null, RECIPIENTS_CHANGE_STATE_IN_REVISION);
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, templateName,
                templateValues, SUBJECT_CHANGE_STATE, null, String.join(COMMA_DELIMITER, institutionProductUsersEmailsByRole, institutionProductCurrentUser.getEmail()));
    }

    @Override
    public void sendInitiativeToCurrentOrganization(Initiative initiative, String templateName) {
        String institutionProductUsersEmailsByRole = getInstitutionProductUsersEmailsByRole(initiative.getOrganizationId(), InitiativeConstants.Role.ADMIN);
        UserResource institutionProductCurrentUser = getInstitutionProductCurrentUser(initiative.getOrganizationId());
        Map<String, String> templateValues = getMap(initiative, institutionProductCurrentUser);
        emailNotificationRestConnector.notifyInitiativeToEmailNotification(initiative, templateName,
                templateValues, SUBJECT_CHANGE_STATE, null, String.join(COMMA_DELIMITER, institutionProductUsersEmailsByRole, institutionProductCurrentUser.getEmail()));
    }

    @NotNull
    private Map<String, String> getMap(Initiative initiative, UserResource userResource) {
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("initiativeName", initiative.getInitiativeName());
        templateValues.put("orgName", initiative.getOrganizationName());
        templateValues.put("status", initiative.getStatus());
        templateValues.put("requesterName", userResource.getName());
        templateValues.put("requesterSurname", userResource.getSurname());
        return templateValues;
    }

    private String getInstitutionProductUsersEmailsByRole(String organizationId, String role) {
        String ret = StringUtils.EMPTY;
        try {
            List<UserResource> users = this.selcRestConnector.getInstitutionProductUsers(organizationId);
            ret = users.stream().filter(user -> user.getRoles().contains(role)).map(UserResource::getEmail).collect(Collectors.joining(COMMA_DELIMITER));
        } catch (Exception e) {
            log.error("[SELC] - Error retrieving product users for organization: " + organizationId, e);
        }
        return ret;
    }

    private UserResource getInstitutionProductCurrentUser(String organizationId) {
        String organizationUserId = loginThreadLocal.getMyThreadLocal().get().get("organizationUserId");
        log.info("[EMAIL-NOTIFICATION] organizationUserId: {}", organizationUserId);
        UserResource userResource = null;
        try {
            List<UserResource> users = this.selcRestConnector.getInstitutionProductUsers(organizationId);
            log.info("[EMAIL-NOTIFICATION] users SELC: {}", users);
            userResource = users.stream().filter(user -> user.getId().toString().equals(organizationUserId)).findFirst().orElseThrow(() -> new IntegrationException(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (Exception e) {
            log.error("[SELC] - Error retrieving product users for organization: " + organizationId, e);
        }
        return userResource;
    }

}
