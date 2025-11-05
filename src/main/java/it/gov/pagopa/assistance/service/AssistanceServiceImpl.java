package it.gov.pagopa.assistance.service;

import it.gov.pagopa.assistance.connector.*;
import it.gov.pagopa.assistance.dto.request.PointOfSaleDTO;
import it.gov.pagopa.assistance.dto.request.TimelineDTO;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import it.gov.pagopa.assistance.dto.request.WalletDTO;
import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssistanceServiceImpl implements AssistanceService {
    private final WalletRestClientImpl walletClient;
    private final TimelineRestClientImpl timelineClient;
    private final TransactionsRestClientImpl transactionsClient;
    private final PointOfSaleRestClientImpl posClient;

    public AssistanceServiceImpl(WalletRestClientImpl walletClient, TimelineRestClientImpl timelineClient, TransactionsRestClientImpl transactionsClient, PointOfSaleRestClientImpl posClient) {
        this.walletClient = walletClient;
        this.timelineClient = timelineClient;
        this.transactionsClient = transactionsClient;
        this.posClient = posClient;
    }

    @Override
    public VouchersStatusDTO vouchersStatus(String initiativeId, String userId) {


        WalletDTO wallet = walletClient.walletDetail(initiativeId, userId);

        TimelineDTO timeline = timelineClient.getTimeline(initiativeId,userId);

        List<TransactionDTO> transactionList = timeline.getOperationList().stream()
                .map(event -> transactionsClient.findByTrxIdAndUserId(event.getEventId(), userId))
                .toList();
        TransactionDTO trx = transactionList.getLast();
        PointOfSaleDTO pos = posClient.getPointOfSale(trx.getMerchantId(), trx.getPointOfSaleId());


        VouchersStatusDTO.builder()
                .name(wallet.getName())
                .surname(wallet.getSurname())
                .dateOfBirth(null)
                .issueDate(wallet.getVoucherStartDate().atStartOfDay())
                .expirationDate(wallet.getVoucherStartDate().atStartOfDay())
                .maxDiscountAmount(wallet.getAmountCents())
                .status(wallet.getStatus())
                .dateOfUse(trx.getTrxDate())
                .amountUsed(wallet.getAccruedCents())
                .typeOfStore(pos.getType())
                .merchant(pos.getFranchiseName())
                .merchantAddress(pos.getAddress() + " , " + pos.getZipCode() +" "+pos.getCity()+" "+pos.getProvince())
                .merchantCity(pos.getCity())
                .phoneNumber(pos.getChannelPhone())
                .goodAmount(null)
                .goodDescription(trx.getAdditionalProperties().get("productName"))
                .build();

        return null;
    }

    @Override
    public OnboardingStatusDTO onboardingStatus(String initiativeId, String userId) {
        return null;
    }

}
