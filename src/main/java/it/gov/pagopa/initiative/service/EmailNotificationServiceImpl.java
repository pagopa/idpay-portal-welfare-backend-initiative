package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.connector.selc.SelcRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final String COMMA_DELIMITER = ",";
    private final EmailNotificationRestConnector emailNotificationRestConnector;
    private final SelcRestConnector selcRestConnector;
    private final String emailAssistance;


    public EmailNotificationServiceImpl(
            EmailNotificationRestConnector emailNotificationRestConnector,
            SelcRestConnector selcRestConnector,
            @Value("${app.initiative.email-assistance}") String emailAssistance) {
        this.emailNotificationRestConnector = emailNotificationRestConnector;
        this.selcRestConnector = selcRestConnector;
        this.emailAssistance = emailAssistance;
    }

    @Override
    public void sendInitiativeToCurrentOrganization(Initiative initiative, String templateName, String subject) {
        try {
            List<UserResource> institutionProductUsers = getInstitutionProductUsersEmailsByRole(initiative.getOrganizationId());

            Set<String> institutionProductUsersEmailsByRoleSet = institutionProductUsers.stream().filter(
                            user -> user.getRoles().contains(InitiativeConstants.Role.ADMIN))
                    .map(UserResource::getEmail)
                    .collect(Collectors.toSet());

            Map<String, String> templateValues = getMap(initiative);
            // Convert the Set of String to String
            String recipients = String.join(COMMA_DELIMITER, institutionProductUsersEmailsByRoleSet);
            emailNotificationRestConnector.notifyInitiativeToEmailNotification(
                        templateName, templateValues, subject, null, recipients);
        } catch (IllegalStateException e) {
            log.error("[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
        } catch (Exception exception) {
            log.error("[MS]-[NOTIFICATION-EMAIL] - Error sending email", exception);
        }
    }

    @Override
    public void sendInitiativeToPagoPA(Initiative initiative, String templateName, String subject) {
        log.info("[EMAIL-NOTIFICATION] Send Initiative info to PagoPA by Email");
        Map<String, String> templateValues = getMap(initiative);
        try {
            emailNotificationRestConnector.notifyInitiativeToEmailNotification(
                    templateName, templateValues, subject, null, emailAssistance);
        } catch (Exception exception) {
            log.error("[MS]-[NOTIFICATION-EMAIL] - Error sending email", exception);
        }
    }

    private Map<String, String> getMap(Initiative initiative) {
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("initiativeName", initiative.getInitiativeName());
        templateValues.put("orgName", initiative.getOrganizationName());
        templateValues.put("status", initiative.getStatus());
        return templateValues;
    }

    List<UserResource> getInstitutionProductUsersEmailsByRole(String organizationId) {
        List<UserResource> users = new ArrayList<>();
        try {
            users = this.selcRestConnector.getInstitutionProductUsers(organizationId);
            log.info("[SELC] Retrieve Institution Product Users DONE");
            log.trace("[EMAIL-NOTIFICATION] users SELC: {}", users);
        } catch (Exception exception) {
            log.error("[ExternalService]-[SELC] - Error retrieving product users for organization: " + organizationId, exception);
        }
        return users;
    }

}
