package it.gov.pagopa.initiative.utils;

import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class AuditUtilitiesTest {
    private static final String USER_ID = "TEST_USER_ID";
    private static final String INITIATIVE_ID = "TEST_INITIATIVE_ID";
    private static final String ORGANIZATION_ID = "TEST_ORGANIZATION_ID";
    private static final String MSG = "MSG";

    private MemoryAppender memoryAppender;
    private final AuditUtilities auditUtilities = new AuditUtilities();

    @BeforeEach
    public void setup() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("AUDIT");
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @Test
    void testNewInitiative() {
        auditUtilities.logNewInitiative(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=New initiative inserted by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testInitiativeApproved() {
        auditUtilities.logInitiativeApproved(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Initiative approved by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testInitiativeToCheck() {
        auditUtilities.logInitiativeToCheck(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Initiative has to be checked by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testInitiativePublished() {
        auditUtilities.logInitiativePublished(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Initiative published by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testInitiativeInRevision() {
        auditUtilities.logInitiativeInRevision(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Initiative in revision by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testOnboardingCitizen() {
        auditUtilities.logOnboardingCitizen(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Get onboarding list by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }
    @Test
    void testDetailUser() {
        auditUtilities.logDetailUser(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Get detail users by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }
    @Test
    void testEditInitiative() {
        auditUtilities.logEditInitiative(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Initiative edited by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testGetInitiative() {
        auditUtilities.logGetInitiative(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Get initiative by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testInitiativeDeleted() {
        auditUtilities.logInitiativeDeleted(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Initiative deleted by the user" +
                        " suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testInitiativeError() {
        auditUtilities.logInitiativeError(USER_ID, INITIATIVE_ID, ORGANIZATION_ID, MSG);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Initiative error:" +
                        " %s suser=%s cs1Label=initiativeId cs1=%s cs2Label=organizationId cs2=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                MSG,
                                USER_ID,
                                INITIATIVE_ID,
                                ORGANIZATION_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }

    @Test
    void testLogDeletedInitiative() {
        auditUtilities.logDeletedInitiative(INITIATIVE_ID);

        Assertions.assertEquals(
                ("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s msg=Deleted initiative:" +
                        " cs1Label=initiativeId cs1=%s")
                        .formatted(
                                AuditUtilities.SRCIP,
                                INITIATIVE_ID
                        ),
                memoryAppender.getLoggedEvents().get(0).getFormattedMessage()
        );
    }
}

