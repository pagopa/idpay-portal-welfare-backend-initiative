package it.gov.pagopa.initiative.utils;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class InitiativeUtils {

    @Value("${app.initiative.logo.url}")
    private String logoUrl;

    @Value("${app.initiative.logo.allowed-mime-types}")
    private Set<String> allowedInitiativeLogoMimeTypes;

    @Value("${app.initiative.logo.allowed-extensions}")
    private Set<String> allowedInitiativeLogoExtensions;


    public String createLogoUrl(String organizationId, String initiativeId){
        return this.logoUrl+this.getPathLogo(organizationId,initiativeId);
    }

    public String getPathLogo(String organizationId, String initiativeId){
        return String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE, organizationId,initiativeId, InitiativeConstants.Logo.LOGO_NAME);
    }

    public Set<String> getAllowedInitiativeLogoMimeTypes(){
        return allowedInitiativeLogoMimeTypes;
    }

    public Set<String> getAllowedInitiativeLogoExtensions(){
        return allowedInitiativeLogoExtensions;
    }

    public static String checkEndDateToSetStatus(Initiative initiative) {
        if (InitiativeConstants.Status.PUBLISHED.equals(initiative.getStatus()) &&
                LocalDate.now().isAfter(initiative.getGeneral().getEndDate())) {
            return InitiativeConstants.Status.CLOSED;
        }
        return initiative.getStatus();
    }

}
