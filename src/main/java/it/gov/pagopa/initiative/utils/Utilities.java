package it.gov.pagopa.initiative.utils;

import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;
import it.gov.pagopa.initiative.exception.InitiativeException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Utilities {
  private static final String SRCIP;

  static {
    try {
      SRCIP = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      throw new InitiativeException(
              BadRequest.CODE,
              String.format(e.getMessage()),
              HttpStatus.BAD_REQUEST);
    }
  }

  private static final String CEF = String.format("CEF:0|PagoPa|IDPAY|1.0|7|User interaction|2| event=Initiative dstip=%s", SRCIP);
  private static final String MSG = " msg=";
  private static final String USER = "suser=";
  private static final String INITIATIVE_ID = "cs1Label=initiativeId cs1=";
  private static final String ORGANIZATION_ID = "cs2Label=organizationId cs2=";

  final Logger logger = Logger.getLogger("AUDIT");


  private String buildLog(String eventLog, String userId, String initiativeId, String organizationId) {
    return CEF + MSG + eventLog + " " + USER + userId + " " + INITIATIVE_ID + initiativeId + " " + ORGANIZATION_ID + organizationId;
  }

  public void logNewInitiative(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("New initiative inserted by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }

  public void logInitiativeApproved(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Initiative approved by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }
  public void logInitiativeToCheck(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Initiative has to be checked by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }

  public void logInitiativePublished(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Initiative published by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }

  public void logInitiativeInRevision(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Initiative in revision by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }

  public void logOnboardingCitizen(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Get onboarding list by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }
  public void logDetailUser(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Get detail user by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }

  public void logEditInitiative(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Initiative edited by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }
  public void logGetInitiative(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Get initiative by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }
  public void logInitiativeDeleted(String userId, String initiativeId, String organizationId) {
    String testLog = this.buildLog("Initiative deleted by the user ", userId, initiativeId, organizationId);
    logger.info(testLog);
  }
  public void logInitiativeError(String userId, String initiativeId, String organizationId, String msg){
    String testLog = this.buildLog("Error: "+ msg, userId, initiativeId, organizationId);
    logger.info(testLog);
  }


}