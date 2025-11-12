package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.dto.request.PointOfSaleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "${assistance.rest-client.merchant.serviceCode}",
        url = "${assistance.rest-client.merchant.baseUrl}"
)
public interface PointOfSaleRestClient {

    @GetMapping(
            value = "/idpay/merchant/portal/{merchantId}/point-of-sales/{pointOfSaleId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<PointOfSaleDTO> getPointOfSale(
            @PathVariable("merchantId") String merchantId,
            @PathVariable("pointOfSaleId") String pointOfSaleId
    );
}