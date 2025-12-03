package it.gov.pagopa.assistance.service;


import it.gov.pagopa.assistance.connector.*;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.*;
import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import it.gov.pagopa.assistance.enums.Channel;
import it.gov.pagopa.assistance.enums.PointOfSaleTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        AssistanceServiceImpl.class,
})
class AssistanceServiceImplTest {

    @MockitoBean  private WalletRestClientImpl walletClient;
    @MockitoBean private TimelineRestClientImpl timelineClient;
    @MockitoBean private TransactionsRestClientImpl transactionsClient;
    @MockitoBean private PointOfSaleRestClientImpl posClient;
    @MockitoBean private OnboardingAssistanceRestClientImpl onboardingClient;

    @Autowired
    private AssistanceServiceImpl service;

    private static final String INITIATIVE_ID = "INIT123";
    private static final String USER_ID = "USR123";



    @Test
    void vouchersStatus_ok() {
        WalletDTO wallet = new WalletDTO();
        wallet.setVoucherStatus(AssistanceConstants.USED);
        wallet.setName("Mary");
        wallet.setSurname("Smith");
        wallet.setAccruedCents(200L);

        when(walletClient.getWallet(INITIATIVE_ID, USER_ID))
                .thenReturn(wallet);

        Operation op = new Operation();
        op.setEventId("EVT1");
        op.setOperationDate(LocalDateTime.of(2024, 2, 1, 10, 0));

        TimelineDTO timeline = new TimelineDTO();
        timeline.setOperationList(List.of(op));

        when(timelineClient.getTimeline(INITIATIVE_ID, USER_ID))
                .thenReturn(timeline);

        RewardCounters rewardCounters = RewardCounters.builder()
                .initiativeBudgetCents(10000L)
                .build();
        Reward reward = Reward.builder()
                .initiativeId("68dd003ccce8c534d1da22bc")
                .counters(rewardCounters)
                .build();

        TransactionDTO trx = new TransactionDTO();
        trx.setTrxDate(LocalDateTime.of(2024, 2, 1, 10, 0));
        trx.setMerchantId("MRC1");
        trx.setPointOfSaleId("POS100");
        trx.setEffectiveAmountCents(500L);
        trx.setAdditionalProperties(Map.of(AssistanceConstants.PRODUCT_NAME, "Pizza"));
        trx.setRewards(Map.of("68dd003ccce8c534d1da22bc",reward));
        when(transactionsClient.getTransactions("INIT123", USER_ID))
                .thenReturn(List.of(trx));

        PointOfSaleDTO pos = new PointOfSaleDTO();
        pos.setFranchiseName("Super Pizza");
        pos.setCity("Rome");
        pos.setProvince("RM");
        pos.setAddress("Via Roma 1");
        pos.setZipCode("00100");
        pos.setType(PointOfSaleTypeEnum.ONLINE);

        when(posClient.getPointOfSale("MRC1", "POS100"))
                .thenReturn(pos);

        VouchersStatusDTO result = service.vouchersStatus(INITIATIVE_ID, USER_ID);

        assertNotNull(result);
        assertEquals("Mary", result.getName());
        assertEquals("USED", result.getStatus());


        verify(walletClient).getWallet(INITIATIVE_ID, USER_ID);
        verify(timelineClient).getTimeline(INITIATIVE_ID, USER_ID);
        verify(transactionsClient).getTransactions("INIT123", USER_ID);

    }

    @Test
    void onboardingStatus_ok() {
        OnboardingDTO onboarding = new OnboardingDTO();
        onboarding.setName("Alice");
        onboarding.setSurname("Wonderland");
        onboarding.setStatus("OK");
        onboarding.setUserMail("alice@mail.com");
        onboarding.setChannel(Channel.IO);
        onboarding.setCriteriaConsensusTimestamp(LocalDateTime.of(2024, 1, 10, 10, 0));

        when(onboardingClient.getOnboardingStatus(INITIATIVE_ID, USER_ID))
                .thenReturn(onboarding);

        OnboardingStatusDTO result = service.onboardingStatus(INITIATIVE_ID, USER_ID);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals("OK", result.getStatus());
        assertEquals("alice@mail.com", result.getEmail());

        verify(onboardingClient).getOnboardingStatus(INITIATIVE_ID, USER_ID);
    }
}
