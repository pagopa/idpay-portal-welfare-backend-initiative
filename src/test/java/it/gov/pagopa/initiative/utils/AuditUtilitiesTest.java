package it.gov.pagopa.initiative.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.exception.InitiativeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {AuditUtilities.class, InetAddress.class})
class AuditUtilitiesTest {
    private static final String SRCIP;

    static {
        try {
            SRCIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private static final String USER_ID = "42";
    private static final String INITIATIVE_ID = "42";
    private static final String ORGANIZATION_ID = "43";
    private static final String MSG = "Error";

    @MockBean
    Logger logger;
    @Autowired
    AuditUtilities auditUtilities;
    @MockBean
    InetAddress inetAddress;
    MemoryAppender memoryAppender;

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
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    @Test
    void testInitiativeApproved() {
        auditUtilities.logInitiativeApproved(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    @Test
    void testInitiativeToCheck() {
        auditUtilities.logInitiativeToCheck(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    @Test
    void testInitiativePublished() {
        auditUtilities.logInitiativePublished(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    @Test
    void testInitiativeInRevision() {
        auditUtilities.logInitiativeInRevision(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();}

    @Test
    void testOnboardingCitizen() {
        auditUtilities.logOnboardingCitizen(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }
    @Test
    void testDetailUser() {
        auditUtilities.logDetailUser(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }
    @Test
    void testEditInitiative() {
        auditUtilities.logEditInitiative(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    @Test
    void testGetInitiative() {
        auditUtilities.logGetInitiative(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    @Test
    void testInitiativeDeleted() {
        auditUtilities.logInitiativeDeleted(USER_ID, INITIATIVE_ID, ORGANIZATION_ID);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    @Test
    void testInitiativeError() {
        auditUtilities.logInitiativeError(USER_ID, INITIATIVE_ID, ORGANIZATION_ID, MSG);
        assertThat(memoryAppender.contains(ch.qos.logback.classic.Level.DEBUG,MSG)).isFalse();
    }

    public static class MemoryAppender extends ListAppender<ILoggingEvent> {
        public void reset() {
            this.list.clear();
        }

        public boolean contains(ch.qos.logback.classic.Level level, String string) {
            return this.list.stream()
                    .anyMatch(event -> event.toString().contains(string)
                            && event.getLevel().equals(level));
        }

        public int countEventsForLogger(String loggerName) {
            return (int) this.list.stream()
                    .filter(event -> event.getLoggerName().contains(loggerName))
                    .count();
        }

        public List<ILoggingEvent> search() {
            return this.list.stream()
                    .filter(event -> event.toString().contains(MSG))
                    .collect(Collectors.toList());
        }

        public List<ILoggingEvent> search(Level level) {
            return this.list.stream()
                    .filter(event -> event.toString().contains(MSG)
                            && event.getLevel().equals(level))
                    .collect(Collectors.toList());
        }

        public int getSize() {
            return this.list.size();
        }

        public List<ILoggingEvent> getLoggedEvents() {
            return Collections.unmodifiableList(this.list);
        }
    }

}

