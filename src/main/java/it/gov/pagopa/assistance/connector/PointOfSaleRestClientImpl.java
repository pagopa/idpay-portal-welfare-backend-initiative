package it.gov.pagopa.assistance.connector;


import it.gov.pagopa.assistance.dto.request.PointOfSaleDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointOfSaleRestClientImpl {

    private final PointOfSaleRestClient pointOfSaleRestClient;

    public PointOfSaleDTO getPointOfSale(String merchantId, String pointOfSaleId) {
        try {
            ResponseEntity<PointOfSaleDTO> response =
                    pointOfSaleRestClient.getPointOfSale(merchantId, pointOfSaleId);
            return response.getBody();
        } catch (Exception e) {
            throw new ServiceException("code","message");
        }
    }
}
