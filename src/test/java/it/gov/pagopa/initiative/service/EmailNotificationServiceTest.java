package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.email_notification.EmailNotificationRestConnector;
import it.gov.pagopa.initiative.connector.selc.SelcRestConnector;
import it.gov.pagopa.initiative.dto.selc.UserResource;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.trx.InitiativeTrxConditions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EmailNotificationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EmailNotificationServiceTest {
    @MockBean
    private EmailNotificationRestConnector emailNotificationRestConnector;

    @Autowired
    private EmailNotificationServiceImpl emailNotificationServiceImpl;

    @MockBean
    private SelcRestConnector selcRestConnector;

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEntePagoPA(Initiative, String)}
     */
    @Test
    void testSendInitiativeEntePagoPA() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(new ArrayList<>());

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEntePagoPA(initiative, "Organization Name");
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification((Initiative) any(),
                (String) any(), (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEntePagoPA(Initiative, String)}
     */
    @Test
    void testSendInitiativeEntePagoPA2() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenThrow(new IllegalStateException());

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEntePagoPA(initiative, "Organization Name");
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification((Initiative) any(),
                (String) any(), (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEntePagoPA(Initiative, String)}
     */
    @Test
    void testSendInitiativeEntePagoPA3() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());

        UserResource userResource = new UserResource();
        userResource.setEmail("jane.doe@example.org");
        userResource.setId(UUID.randomUUID());
        userResource.setName("initiativeName");
        userResource.setRoles(new ArrayList<>());
        userResource.setSurname("Doe");

        ArrayList<UserResource> userResourceList = new ArrayList<>();
        userResourceList.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(userResourceList);

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEntePagoPA(initiative, "Organization Name");
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification((Initiative) any(),
                (String) any(), (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEntePagoPA(Initiative, String)}
     */
    @Test
    void testSendInitiativeEntePagoPA4() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());

        UserResource userResource = new UserResource();
        userResource.setEmail("jane.doe@example.org");
        userResource.setId(UUID.randomUUID());
        userResource.setName("initiativeName");
        userResource.setRoles(new ArrayList<>());
        userResource.setSurname("Doe");

        UserResource userResource1 = new UserResource();
        userResource1.setEmail("jane.doe@example.org");
        userResource1.setId(UUID.randomUUID());
        userResource1.setName("initiativeName");
        userResource1.setRoles(new ArrayList<>());
        userResource1.setSurname("Doe");

        ArrayList<UserResource> userResourceList = new ArrayList<>();
        userResourceList.add(userResource1);
        userResourceList.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(userResourceList);

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEntePagoPA(initiative, "Organization Name");
        verify(emailNotificationRestConnector, atLeast(1)).notifyInitiativeToEmailNotification((Initiative) any(),
                (String) any(), (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnte(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnte() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(new ArrayList<>());

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnte(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnte(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnte2() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenThrow(new IllegalStateException());

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnte(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnte(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnte3() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());

        UserResource userResource = new UserResource();
        userResource.setEmail("jane.doe@example.org");
        userResource.setId(UUID.randomUUID());
        userResource.setName("initiativeName");
        userResource.setRoles(new ArrayList<>());
        userResource.setSurname("Doe");

        ArrayList<UserResource> userResourceList = new ArrayList<>();
        userResourceList.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(userResourceList);

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnte(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnte(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnte4() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());

        UserResource userResource = new UserResource();
        userResource.setEmail("jane.doe@example.org");
        userResource.setId(UUID.randomUUID());
        userResource.setName("initiativeName");
        userResource.setRoles(new ArrayList<>());
        userResource.setSurname("Doe");

        UserResource userResource1 = new UserResource();
        userResource1.setEmail("jane.doe@example.org");
        userResource1.setId(UUID.randomUUID());
        userResource1.setName("initiativeName");
        userResource1.setRoles(new ArrayList<>());
        userResource1.setSurname("Doe");

        ArrayList<UserResource> userResourceList = new ArrayList<>();
        userResourceList.add(userResource1);
        userResourceList.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(userResourceList);

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnte(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnteCreated(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnteCreated() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(new ArrayList<>());

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnteCreated(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnteCreated(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnteCreated2() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenThrow(new IllegalStateException());

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnteCreated(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnteCreated(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnteCreated3() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());

        UserResource userResource = new UserResource();
        userResource.setEmail("jane.doe@example.org");
        userResource.setId(UUID.randomUUID());
        userResource.setName("initiativeName");
        userResource.setRoles(new ArrayList<>());
        userResource.setSurname("Doe");

        ArrayList<UserResource> userResourceList = new ArrayList<>();
        userResourceList.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(userResourceList);

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnteCreated(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }

    /**
     * Method under test: {@link EmailNotificationServiceImpl#sendInitiativeEnteCreated(Initiative, String)}
     */
    @Test
    void testSendInitiativeEnteCreated4() {
        doNothing().when(emailNotificationRestConnector)
                .notifyInitiativeToEmailNotification((Initiative) any(), (String) any(), (Map<String, String>) any(),
                        (String) any(), (String) any(), (String) any());

        UserResource userResource = new UserResource();
        userResource.setEmail("jane.doe@example.org");
        userResource.setId(UUID.randomUUID());
        userResource.setName("initiativeName");
        userResource.setRoles(new ArrayList<>());
        userResource.setSurname("Doe");

        UserResource userResource1 = new UserResource();
        userResource1.setEmail("jane.doe@example.org");
        userResource1.setId(UUID.randomUUID());
        userResource1.setName("initiativeName");
        userResource1.setRoles(new ArrayList<>());
        userResource1.setSurname("Doe");

        ArrayList<UserResource> userResourceList = new ArrayList<>();
        userResourceList.add(userResource1);
        userResourceList.add(userResource);
        when(selcRestConnector.getInstitutionProductUsers((String) any())).thenReturn(userResourceList);

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        emailNotificationServiceImpl.sendInitiativeEnteCreated(initiative, "Organization Name");
        verify(emailNotificationRestConnector).notifyInitiativeToEmailNotification((Initiative) any(), (String) any(),
                (Map<String, String>) any(), (String) any(), (String) any(), (String) any());
        verify(selcRestConnector).getInstitutionProductUsers((String) any());
    }
}

