package it.gov.pagopa.assistance.service;


import it.gov.pagopa.assistance.connector.*;
import it.gov.pagopa.assistance.dto.request.OnboardingDTO;
import it.gov.pagopa.assistance.dto.request.Operation;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import it.gov.pagopa.assistance.dto.request.WalletDTO;
import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssistanceServiceImpl implements AssistanceService {



    private final WalletRestClientImpl walletClient;
    private final TimelineRestClientImpl timelineClient;
    private final TransactionsRestClientImpl transactionsClient;
    private final PointOfSaleRestClientImpl posClient;
    private final OnboardingAssistanceRestClientImpl onboardingClient;

    @Override
    public VouchersStatusDTO vouchersStatus(String initiativeId, String userId) {
        log.debug("Fetching voucher status for initiativeId={} userId={}", initiativeId, userId);

        WalletDTO wallet = walletClient.getWallet(initiativeId, userId);
        log.debug("wallet={}", wallet);

        List<Operation> operations = timelineClient.getTimeline(initiativeId, userId);
        log.debug("operations={}", operations);

        List<TransactionDTO> transactions = transactionsClient.getTransactions(initiativeId,userId);
        log.debug("transactions={}", transactions);
        return buildVoucherStatus(wallet, operations, transactions);
    }




    private VouchersStatusDTO buildVoucherStatus(
            WalletDTO wallet,
            List<Operation> operations,
            List<TransactionDTO> transactions
    ) {

        List<Operation> reducedOperations = operations != null
                ? operations.stream()
                .map(op -> {
                    Operation o = new Operation();
                    o.setEventId(op.getEventId());
                    o.setOperationType(op.getOperationType());
                    o.setAmountCents(op.getAmountCents());
                    o.setOperationDate(op.getOperationDate());
                    o.setBusinessName(op.getBusinessName());
                    return o;
                })
                .toList()
                : null;

        List<TransactionDTO> reducedTransactions = transactions != null
                ? transactions.stream()
                .map(tx -> {
                    TransactionDTO t = new TransactionDTO();
                    t.setId(tx.getId());
                    t.setTrxDate(tx.getTrxDate());
                    t.setStatus(tx.getStatus());
                    t.setAmountCents(tx.getAmountCents());
                    t.setMerchantId(tx.getMerchantId());
                    t.setPointOfSaleId(tx.getPointOfSaleId());
                    t.setAdditionalProperties(tx.getAdditionalProperties());
                    return t;
                })
                .toList()
                : null;

        return VouchersStatusDTO.builder()
                .name(wallet.getName())
                .surname(wallet.getSurname())
                .issueDate(wallet.getVoucherStartDate() != null ? wallet.getVoucherStartDate() : null)
                .expirationDate(wallet.getVoucherEndDate() != null ? wallet.getVoucherEndDate() : null)
                .status(wallet.getVoucherStatus())
                .operations(reducedOperations)
                .transactions(reducedTransactions)
                .build();
    }



    @Override
    public OnboardingStatusDTO onboardingStatus(String initiativeId, String userId) {
        log.debug("Fetching onboarding status for initiativeId={} userId={}", initiativeId, userId);

        OnboardingDTO onboarding = onboardingClient.getOnboardingStatus(initiativeId, userId);

        log.debug("onboarding={}", onboarding);
        return OnboardingStatusDTO.builder()
                .name(onboarding.getName())
                .surname(onboarding.getSurname())
                .dateOfOnboardingRequest(onboarding.getCriteriaConsensusTimestamp())
                .dateOfOnboardingOk(onboarding.getOnboardingOkDate())
                .channel(onboarding.getChannel())
                .email(onboarding.getUserMail())
                .status(onboarding.getStatus())
                .detailKO(onboarding.getDetailKO())
                .build();
    }
}

