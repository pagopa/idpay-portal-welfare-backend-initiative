package it.gov.pagopa.initiative.utils;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitiativeUtils {

    @Value("${app.initiative.logo.url}")
    private String logoUrl;

    public String createLogoUrl(String organizationId, String initiativeId){
        if(logoUrl==null){
            logoUrl="https://idpaydinitiativestorage.blob.core.windows.net/logo/";
        }
        return this.logoUrl+String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE, organizationId,initiativeId, InitiativeConstants.Logo.LOGO_NAME);
    }


}
