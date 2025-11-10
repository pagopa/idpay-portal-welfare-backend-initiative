package it.gov.pagopa.assistance.connector;


import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsRestClientImpl {

    private final TransactionsRestClient transactionsRestClient;

    public TransactionDTO getTransaction(String trxId, String userId) {
        log.debug("Calling Transaction MS for trxId={} userId={}", trxId, userId);
        try {
            TransactionDTO transaction = transactionsRestClient.findByTrxIdAndUserId(trxId, userId);

            if (transaction == null) {
                log.warn("Transaction not found for trxId={} userId={}", trxId, userId);
                throw new ServiceException(
                        AssistanceConstants.ConnectorError.ASSISTANCE_TRANSACTION_ERROR,
                        "Transaction not found in Transaction MS"
                );
            }

            return transaction;
        } catch (RestClientException e) {
            log.error("Error while calling Transaction MS for trxId={} userId={}", trxId, userId, e);
            throw new ServiceException(
                    AssistanceConstants.ConnectorError.ASSISTANCE_TRANSACTION_ERROR,
                    "Error while calling Transaction MS"
            );
        }
    }
}
