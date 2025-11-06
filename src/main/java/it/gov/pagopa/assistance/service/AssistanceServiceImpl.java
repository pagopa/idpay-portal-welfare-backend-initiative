package it.gov.pagopa.assistance.service;

import it.gov.pagopa.assistance.connector.*;
import it.gov.pagopa.assistance.dto.request.*;
import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

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

        TimelineDTO timeline = timelineClient.getTransactionOperation(initiativeId, userId);

        Optional<Operation> lastTransactionOp = timeline.getOperationList().stream()
                .max(Comparator.comparing(Operation::getOperationDate));

        TransactionDTO latestTransaction = null;
        PointOfSaleDTO pos = null;

        if (lastTransactionOp.isPresent()) {
            Operation op = lastTransactionOp.get();
            latestTransaction = transactionsClient.findByTrxIdAndUserId(op.getEventId(), userId);

            if (latestTransaction != null) {
                pos = posClient.getPointOfSale(
                        latestTransaction.getMerchantId(),
                        latestTransaction.getPointOfSaleId()
                );
            }
        }


        return VouchersStatusDTO.builder()
                .name(wallet.getName())
                .surname(wallet.getSurname())
                .dateOfBirth(null)
                .issueDate(wallet.getVoucherStartDate().atStartOfDay())
                .expirationDate(wallet.getVoucherStartDate().atStartOfDay())
                .maxDiscountAmount(wallet.getAmountCents())
                .status(wallet.getStatus())
                .dateOfUse(latestTransaction != null ? latestTransaction.getTrxDate() : null)
                .amountUsed(wallet.getAccruedCents())
                .typeOfStore(pos != null ? pos.getType() : null)
                .merchant(pos != null ? pos.getFranchiseName() : null)
                .merchantAddress(pos != null ?
                        pos.getAddress() + " , " + pos.getZipCode() + " " + pos.getCity() + " " + pos.getProvince() : null)
                .merchantCity(pos != null ? pos.getCity() : null)
                .phoneNumber(pos != null ? pos.getChannelPhone() : null)
                .goodAmount(latestTransaction != null ? latestTransaction.getEffectiveAmountCents() : null)
                .goodDescription(latestTransaction != null ?
                        latestTransaction.getAdditionalProperties().get("productName") : null)
                .build();
    }


    @Override
    public OnboardingStatusDTO onboardingStatus(String initiativeId, String userId) {
        WalletDTO wallet = walletClient.walletDetail(initiativeId, userId);

        return OnboardingStatusDTO.builder()
                .name(wallet.getName())
                .surname(wallet.getSurname())
                .dateOfBirth(null)
                .dateOfOnboardingRequest(null)
                .iseeOptionSelected(null)
                .channel(wallet.getChannel())
                .email(wallet.getUserMail())
                .status(wallet.getStatus())
                .build();
    }

}
