package it.gov.pagopa.initiative.connector.group;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.trx.InitiativeTrxConditions;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GroupRestConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class GroupRestConnectorTest {
    @MockBean
    private GroupFeignRestClient groupFeignRestClient;

    @Autowired
    private GroupRestConnectorImpl groupRestConnectorImpl;

    @Test
    void testNotifyInitiativeToGroup() {
        when(groupFeignRestClient.notifyInitiativeForCitizen(any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(new InitiativeAdditional());
        initiative.setBeneficiaryRule(new InitiativeBeneficiaryRule());
        initiative.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        initiative.setCreationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setEnabled(true);
        initiative.setGeneral(new InitiativeGeneral());
        initiative.setInitiativeId("42");
        initiative.setInitiativeName("Initiative Name");
        initiative.setOrganizationId("42");
        initiative.setOrganizationName("Organization Name");
        initiative.setOrganizationVat("Organization Vat");
        initiative.setPdndToken("ABC123");
        initiative.setRefundRule(new InitiativeRefundRule());
        initiative.setRewardRule(new RewardGroups());
        initiative.setStatus("Status");
        initiative.setTrxRule(new InitiativeTrxConditions());
        initiative.setUpdateDate(LocalDateTime.of(1, 1, 1, 1, 1));
        initiative.setUpdatedBy("2020-03-01");
        groupRestConnectorImpl.notifyInitiativeToGroup(initiative);
        verify(groupFeignRestClient).notifyInitiativeForCitizen(any(), any());
    }
}

