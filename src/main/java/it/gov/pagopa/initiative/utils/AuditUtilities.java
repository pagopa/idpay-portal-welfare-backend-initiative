package it.gov.pagopa.initiative.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j(topic = "AUDIT")
public class AuditUtilities {
  public static final String SRCIP;

  static {
    String srcIp;
    try {
      srcIp = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.error("Cannot determine the ip of the current host", e);
      srcIp="UNKNOWN";
    }

    SRCIP = srcIp;
  }

  private static final String CEF = String.format("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s", SRCIP);
  private static final String CEF_PATTERN = CEF + " msg={} suser={} cs1Label=initiativeId cs1={} cs2Label=organizationId cs2={}";

  private void logAuditString(String... parameters) {
    log.info(AuditUtilities.CEF_PATTERN, (Object[]) parameters);
  }

  public void logNewInitiative(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "New initiative inserted by the user", userId, initiativeId, organizationId
    );
  }
  public void logInitiativeApproved(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Initiative approved by the user", userId, initiativeId, organizationId
    );
  }
  public void logInitiativeToCheck(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Initiative has to be checked by the user", userId, initiativeId, organizationId
    );
  }
  public void logInitiativePublished(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Initiative published by the user", userId, initiativeId, organizationId
    );
  }
  public void logInitiativeInRevision(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Initiative in revision by the user", userId, initiativeId, organizationId
    );
  }
  public void logOnboardingCitizen(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Get onboarding list by the user", userId, initiativeId, organizationId
    );
  }
  public void logDetailUser(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Get detail users by the user", userId, initiativeId, organizationId
    );
  }
  public void logEditInitiative(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Initiative edited by the user", userId, initiativeId, organizationId
    );
  }
  public void logGetInitiative(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Get initiative by the user", userId, initiativeId, organizationId
    );
  }
  public void logInitiativeDeleted(String userId, String initiativeId, String organizationId) {
    logAuditString(
            "Initiative deleted by the user", userId, initiativeId, organizationId
    );
  }
  public void logInitiativeError(String userId, String initiativeId, String organizationId, String msg){
    logAuditString(
            "Initiative error: " + msg, userId, initiativeId, organizationId
    );
  }
}