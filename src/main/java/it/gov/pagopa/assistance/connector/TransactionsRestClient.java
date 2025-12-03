package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "${assistance.rest-client.transactions.serviceCode}",
        url = "${assistance.rest-client.transactions.baseUrl}"
)
public interface TransactionsRestClient {

    @GetMapping(
            value = "/idpay/transactions/{initiativeId}/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<TransactionDTO> findByInitiativeIdAndUserId(
            @PathVariable(value = "initiativeId") String initiativeId,
            @PathVariable(value = "userId") String userId
    );

}
