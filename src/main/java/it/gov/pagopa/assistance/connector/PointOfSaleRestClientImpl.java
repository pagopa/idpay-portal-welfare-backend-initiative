package it.gov.pagopa.assistance.connector;


import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.PointOfSaleDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static it.gov.pagopa.assistance.utlis.Utils.extractMessageFromFeignException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointOfSaleRestClientImpl {

    private final PointOfSaleRestClient pointOfSaleRestClient;

    public PointOfSaleDTO getPointOfSale(String merchantId, String pointOfSaleId) {
        log.debug("Calling Merchant MS for merchantId={} pointOfSaleId={}", merchantId, pointOfSaleId);
        try {
            ResponseEntity<PointOfSaleDTO> response = pointOfSaleRestClient.getPointOfSale(merchantId, pointOfSaleId);

            if (response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("[ASSISTANCE]  Empty or invalid response from Merchant MS for merchantId={} posId={}", merchantId, pointOfSaleId);
                throw new ServiceException(
                        AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR,
                        "Empty or invalid response from Merchant MS"
                );
            }

            return response.getBody();
        } catch (FeignException e) {
            log.error("[ASSISTANCE]  Error while calling Merchant MS for merchantId={} posId={}", merchantId, pointOfSaleId, e);
            throw new ServiceException(
                    AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR,
                    extractMessageFromFeignException(e)
            );
        }
    }
}
