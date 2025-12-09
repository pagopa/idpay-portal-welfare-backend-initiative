package it.gov.pagopa.assistance.connector;


import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.gov.pagopa.assistance.utlis.Utils.extractMessageFromFeignException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsRestClientImpl {

    private final TransactionsRestClient transactionsRestClient;

    public List<TransactionDTO> getTransactions(String initiativeId, String userId) {
        log.debug("[ASSISTANCE]  Calling Transaction MS for initiativeId={} userId={}", initiativeId, userId);
        try {
            return transactionsRestClient.findByInitiativeIdAndUserId(initiativeId, userId);
        } catch (FeignException e) {
            log.error("[ASSISTANCE]  Error while calling Transaction MS for initiativeId={} userId={}", initiativeId, userId, e);
            throw new ServiceException(
                    AssistanceConstants.ConnectorError.ASSISTANCE_TRANSACTION_ERROR,
                    extractMessageFromFeignException(e)
            );
        }
    }
}
