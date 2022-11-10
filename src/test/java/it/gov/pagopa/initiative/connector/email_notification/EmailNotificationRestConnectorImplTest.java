package it.gov.pagopa.initiative.connector.email_notification;

import it.gov.pagopa.initiative.dto.email_notification.EmailMessageDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EmailNotificationRestConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class EmailNotificationRestConnectorImplTest {
    @MockBean
    private EmailNotificationFeignRestClient emailNotificationFeignRestClient;

    @Autowired
    private EmailNotificationRestConnectorImpl emailNotificationRestConnectorImpl;

    /**
     * Method under test: {@link EmailNotificationRestConnectorImpl#notifyInitiativeToEmailNotification(Initiative, String, Map, String, String, String)}
     */
    @Test
    void testNotifyInitiativeToEmailNotification() {
        when(emailNotificationFeignRestClient.notifyInitiativeInfo((EmailMessageDTO) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

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
        emailNotificationRestConnectorImpl.notifyInitiativeToEmailNotification(initiative, "Template Name", new HashMap<>(),
                "Hello from the Dreaming Spires", "Sender", "Recipients");
        verify(emailNotificationFeignRestClient).notifyInitiativeInfo((EmailMessageDTO) any());
    }
}

