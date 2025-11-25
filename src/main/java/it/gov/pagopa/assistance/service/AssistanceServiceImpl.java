package it.gov.pagopa.assistance.service;


import it.gov.pagopa.assistance.connector.*;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.*;
import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


        if(!wallet.getVoucherStatus().equals(AssistanceConstants.USED)){
            log.debug("Voucher not used. initiativeId={} userId={}", initiativeId, userId);
            return buildVoucherStatus(wallet, null, null);
        }

        TimelineDTO timeline = timelineClient.getTimeline(initiativeId, userId);
        log.debug("timeline={}", timeline);


        Optional<Operation> lastTransactionOp = timeline.getOperationList().stream()
                .filter(op -> op.getOperationDate() != null)
                .max(Comparator.comparing(Operation::getOperationDate));

        if (lastTransactionOp.isEmpty()) {
            log.debug("No transaction operations found for initiativeId={} userId={}", initiativeId, userId);
            return buildVoucherStatus(wallet, null, null);
        }

        Operation op = lastTransactionOp.get();
        TransactionDTO latestTransaction = transactionsClient.getTransaction(op.getEventId(), userId);
        log.debug("transaction={}", latestTransaction);
        PointOfSaleDTO pos = null;

        if (latestTransaction != null &&
                latestTransaction.getMerchantId() != null &&
                latestTransaction.getPointOfSaleId() != null) {
            pos = posClient.getPointOfSale(latestTransaction.getMerchantId(), latestTransaction.getPointOfSaleId());
        }

        log.debug("pos={}", pos);
        return buildVoucherStatus(wallet, latestTransaction, pos);
    }


    private VouchersStatusDTO buildVoucherStatus(WalletDTO wallet, TransactionDTO transaction, PointOfSaleDTO pos) {

        String merchantAddress = null;
        if (pos != null) {
            merchantAddress = Stream.of(pos.getAddress(), pos.getZipCode(), pos.getCity(), pos.getProvince())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" "));
        }

        String goodDescription = Optional.ofNullable(transaction)
                .map(TransactionDTO::getAdditionalProperties)
                .map(props -> props.get(AssistanceConstants.PRODUCT_NAME))
                .map(Object::toString)
                .orElse(null);

        return VouchersStatusDTO.builder()
                .name(wallet.getName())
                .surname(wallet.getSurname())
                .issueDate(wallet.getVoucherStartDate() != null ? wallet.getVoucherStartDate().atStartOfDay() : null)
                .expirationDate(wallet.getVoucherEndDate() != null ? wallet.getVoucherEndDate().atStartOfDay() : null)
                .maxDiscountAmount(wallet.getVoucherStatus().equals("USED") ? Objects.requireNonNull(transaction).getRewards().get("68dd003ccce8c534d1da22bc").getCounters().getInitiativeBudgetCents() : wallet.getAmountCents())
                .status(wallet.getVoucherStatus())
                .dateOfUse(transaction != null ? transaction.getTrxDate() : null)
                .amountUsed(wallet.getAccruedCents())
                .typeOfStore(pos != null ? pos.getType() : null)
                .merchant(pos != null ? pos.getFranchiseName() : null)
                .merchantAddress(merchantAddress)
                .merchantCity(pos != null ? pos.getCity() : null)
                .phoneNumber(pos != null ? pos.getChannelPhone() : null)
                .goodAmount(transaction != null ? transaction.getEffectiveAmountCents() : null)
                .goodDescription(goodDescription)
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

