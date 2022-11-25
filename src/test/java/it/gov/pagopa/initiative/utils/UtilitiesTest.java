package it.gov.pagopa.initiative.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {Utilities.class})
@ExtendWith(SpringExtension.class)
class UtilitiesTest {
    @Autowired
    private Utilities utilities;

    private static String SRCIP;
    private static final String CEF = String.format("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2|event=Initiative srcip=%s srcport=17548 dstip=172.16.128.37 dstport=82",
            SRCIP);
    private static final String MSG = " msg=";
    private static final String USER = "suser=";
    private static final String CS1 = "cs1Label=iniziativeId cs1=";
    final Logger logger = Logger.getLogger("AUDIT");

    private final String USER_ID = "42";
    private final String INITIATIVE_ID = "42";

    @Test
    void testNewInitiative() {
        utilities.newInitiative(USER_ID, INITIATIVE_ID);
    }

    @Test
    void testInitiativeApproved() {
        utilities.initiativeApproved(USER_ID, INITIATIVE_ID);
    }

    @Test
    void testInitiativeToCheck() {
        utilities.initiativeToCheck(USER_ID, INITIATIVE_ID);
    }

    @Test
    void testInitiativePublished() {
        utilities.initiativePublished(USER_ID, INITIATIVE_ID);
    }

    @Test
    void testInitiativeInRevision() {
        utilities.initiativeInRevision(USER_ID, INITIATIVE_ID);

    }

    @Test
    void testOnboardingCitizen() {
        utilities.onboardingCitizen(USER_ID, INITIATIVE_ID);

    }

    @Test
    void testStats() {
        utilities.stats(USER_ID, INITIATIVE_ID);

    }

    @Test
    void testEditInitiative() {
        utilities.editInitiative(USER_ID, INITIATIVE_ID);

    }

    @Test
    void testUploadFile() {
        utilities.uploadFile(USER_ID, INITIATIVE_ID);

    }

    @Test
    void testDownloadFile() {
        utilities.downloadFile(USER_ID, INITIATIVE_ID);

    }

    @Test
    void testDetailUser() {
        utilities.detailUser(USER_ID, INITIATIVE_ID);

    }

    @Test
    void testGetInitiative() {
        utilities.getInitiative(USER_ID, INITIATIVE_ID);

    }
}

