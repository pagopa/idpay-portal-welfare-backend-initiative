package it.gov.pagopa.assistance.connector;


import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsRestClientImpl   {


    private final TransactionsRestClient transactionsRestClient;

    public TransactionDTO findByTrxIdAndUserId(String trxId, String userId) {
        try {
            return transactionsRestClient.findByTrxIdAndUserId(trxId,userId);
        } catch (Exception e) {
            throw new ServiceException(AssistanceConstants.ConnectorError.ASSISTANCE_TRANSACTION_ERROR,"Error While Call Transaction MS");
        }
    }
}
