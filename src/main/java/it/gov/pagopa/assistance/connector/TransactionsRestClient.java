package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "${rest-client.transactions.serviceCode}",
        url = "${rest-client.transactions.baseUrl}"
)
public interface TransactionsRestClient {

    @GetMapping(
            value = "/idpay/transactions/{trxId}/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    TransactionDTO findByTrxIdAndUserId(
            @PathVariable(value = "trxId") String idTrxIssuer,
            @PathVariable(value = "userId") String userId
    );

}
