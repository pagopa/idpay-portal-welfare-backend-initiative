package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.connector.selc.SelcRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.stream.Collectors;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Email.RECIPIENT_ASSISTANCE;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final String COMMA_DELIMITER = ",";
    private final EmailNotificationRestConnector emailNotificationRestConnector;
    private final SelcRestConnector selcRestConnector;

    public EmailNotificationServiceImpl(
            EmailNotificationRestConnector emailNotificationRestConnector,
            SelcRestConnector selcRestConnector) {
        this.emailNotificationRestConnector = emailNotificationRestConnector;
        this.selcRestConnector = selcRestConnector;
    }

    @Override
    public void sendInitiativeToCurrentOrganization(Initiative initiative, String templateName, String subject) {
        try {
            log.info("[EMAIL-NOTIFICATION] Send Initiative info to current Organization by Email");
            RequestAttributes requestAttributes = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                    .orElseThrow(() -> new IllegalStateException("[EMAIL-NOTIFICATION]: Request Attributes should not be null"));
            Object organizationUserIdObject = Optional.ofNullable(requestAttributes.getAttribute("organizationUserId", RequestAttributes.SCOPE_REQUEST))
                    .orElseThrow(() -> new IllegalStateException("[EMAIL-NOTIFICATION]: [organizationUserId] Request Attribute should not be null"));
            String organizationUserId = organizationUserIdObject.toString();
            log.trace("[EMAIL-NOTIFICATION] organizationUserId: {}", organizationUserId);

            List<UserResource> institutionProductUsers = getInstitutionProductUsersEmailsByRole(initiative.getOrganizationId());

            UserResource currentUserResource = institutionProductUsers.stream().filter(
                    user -> user.getId().toString().equals(organizationUserId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("[EMAIL-NOTIFICATION]: Current user not found from SelfCare institutional Users"));
            Set<String> institutionProductUsersEmailsByRoleSet = institutionProductUsers.stream().filter(
                            user -> user.getRoles().contains(InitiativeConstants.Role.ADMIN))
                    .map(UserResource::getEmail)
                    .collect(Collectors.toSet());

            Map<String, String> templateValues = getMap(initiative, currentUserResource);
            //Add current user to recipient Set
            institutionProductUsersEmailsByRoleSet.add(currentUserResource.getEmail());
            // Convert the Set of String to String
            String recipients = String.join(COMMA_DELIMITER, institutionProductUsersEmailsByRoleSet);
            emailNotificationRestConnector.notifyInitiativeToEmailNotification(
                        initiative, templateName, templateValues, subject, null, recipients);
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
                    initiative, templateName, templateValues, subject, null, RECIPIENT_ASSISTANCE);
        } catch (Exception exception) {
            log.error("[MS]-[NOTIFICATION-EMAIL] - Error sending email", exception);
        }
    }

    private Map<String, String> getMap(Initiative initiative) {
        return getMap(initiative, null);
    }

    private Map<String, String> getMap(Initiative initiative, UserResource userResource) {
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("initiativeName", initiative.getInitiativeName());
        templateValues.put("orgName", initiative.getOrganizationName());
        templateValues.put("status", initiative.getStatus());
        if (userResource != null) {
            templateValues.put("requesterName", userResource.getName());
            templateValues.put("requesterSurname", userResource.getSurname());
        }
        return templateValues;
    }

    private List<UserResource> getInstitutionProductUsersEmailsByRole(String organizationId) {
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
