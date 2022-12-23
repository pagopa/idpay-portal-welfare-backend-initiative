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

  private static final String CEF = String.format("CEF|PagoPa|IDPAY|1.0|7|User interaction|2|event=Initiative srcip=%s srcport=17548 dstip=172.16.128.37 dstport=82",
      SRCIP);
  private static final String MSG = " msg=";
  private static final String USER = "suser=";
  private static final String CS1 = "cs1Label=iniziativeId cs1=";
  final Logger logger = Logger.getLogger("AUDIT");


  private String buildLog(String eventLog, String userId, String initiativeId) {
    return CEF + MSG + eventLog + " " + USER + userId + " " + CS1 + initiativeId;
  }

  public void newInitiative(String userId, String initiativeId) {
    String testLog = this.buildLog("New initiative by the user ", userId,
        initiativeId);
    logger.info(testLog);
  }

  public void initiativeApproved(String userId, String initiativeId) {
    String testLog = this.buildLog("Initiative approved by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }
  public void initiativeToCheck(String userId, String initiativeId) {
    String testLog = this.buildLog("Initiative to check by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

  public void initiativePublished(String userId, String initiativeId) {
    String testLog = this.buildLog("Initiative published by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

  public void initiativeInRevision(String userId, String initiativeId) {
    String testLog = this.buildLog("Initiative in revision by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

  public void onboardingCitizen(String userId, String initiativeId) {
    String testLog = this.buildLog("Get onboarding list by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

  public void stats(String userId, String initiativeId) {
    String testLog = this.buildLog("Get stats by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

  public void editInitiative(String userId, String initiativeId) {
    String testLog = this.buildLog("Initiative edited by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

  public void uploadFile(String userId, String initiativeId) {
    String testLog = this.buildLog("Upload file by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

  public void downloadFile(String userId, String initiativeId) {
    String testLog = this.buildLog("Download file by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }
  public void detailUser(String userId, String initiativeId) {
    String testLog = this.buildLog("Get detail user by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }
  public void getInitiative(String userId, String initiativeId) {
    String testLog = this.buildLog("Get initiative by the user ", userId,
            initiativeId);
    logger.info(testLog);
  }

}