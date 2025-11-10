package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.dto.request.WalletDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "${assistance.rest-client.wallet.serviceCode}",
        url = "${assistance.rest-client.wallet.baseUrl}"
)
public interface WalletRestClient {

    @GetMapping(
            value = "/idpay/wallet/{initiativeId}/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WalletDTO> walletDetail(
            @PathVariable("initiativeId") String initiativeId,
            @PathVariable("userId") String userId
    );
}