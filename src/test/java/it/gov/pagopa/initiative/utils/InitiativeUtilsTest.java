package it.gov.pagopa.initiative.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {InitiativeUtils.class})
@ExtendWith(SpringExtension.class)
class InitiativeUtilsTest {
    @Autowired
    private InitiativeUtils initiativeUtils;

    private final String ORGANIZATION_ID = "42";
    private final String INITIATIVE_ID = "42";

    @Test
    void testCreateLogoUrl() {
        assertFalse(initiativeUtils.createLogoUrl(ORGANIZATION_ID, INITIATIVE_ID).isEmpty());
    }

    @Test
    void testGetPathLogo() {
        assertFalse(initiativeUtils.getPathLogo(ORGANIZATION_ID, INITIATIVE_ID).isEmpty());
    }

    @Test
    void testSetString() {
        InitiativeUtils actualInitiativeUtils = new InitiativeUtils();
        assertNull(actualInitiativeUtils.getAllowedInitiativeLogoExtensions());
        assertNull(actualInitiativeUtils.getAllowedInitiativeLogoMimeTypes());
    }
}

