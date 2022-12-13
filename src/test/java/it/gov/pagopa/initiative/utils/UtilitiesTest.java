package it.gov.pagopa.initiative.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {Utilities.class})
@ExtendWith(SpringExtension.class)
class UtilitiesTest {
    @Autowired
    private Utilities utilities;

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

