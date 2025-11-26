package it.gov.pagopa.assistance.connector;

import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.PointOfSaleDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PointOfSaleRestClientImpl.class
})
class PointOfSaleRestClientImplTest {

    @MockitoBean
    private PointOfSaleRestClient pointOfSaleRestClient;

    @Autowired
    private PointOfSaleRestClientImpl service;

    private static final String MERCHANT_ID = "MRC123";
    private static final String POS_ID = "POS999";



    @Test
    void getPointOfSale_OK() {
        PointOfSaleDTO dto = new PointOfSaleDTO();
        dto.setFranchiseName("Store One");

        when(pointOfSaleRestClient.getPointOfSale(MERCHANT_ID, POS_ID))
                .thenReturn(ResponseEntity.ok(dto));

        PointOfSaleDTO result = service.getPointOfSale(MERCHANT_ID, POS_ID);

        assertNotNull(result);
        assertEquals("Store One", result.getFranchiseName());
        verify(pointOfSaleRestClient).getPointOfSale(MERCHANT_ID, POS_ID);
    }



    @Test
    void getPointOfSale_responseNull_throwsServiceException() {
        when(pointOfSaleRestClient.getPointOfSale(MERCHANT_ID, POS_ID))
                .thenReturn(null);

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getPointOfSale(MERCHANT_ID, POS_ID)
        );

        assertEquals(AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR, ex.getCode());
    }


    @Test
    void getPointOfSale_non2xx_throwsServiceException() {
        when(pointOfSaleRestClient.getPointOfSale(MERCHANT_ID, POS_ID))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getPointOfSale(MERCHANT_ID, POS_ID)
        );

        assertEquals(AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR, ex.getCode());
    }


    @Test
    void getPointOfSale_bodyNull_throwsServiceException() {
        when(pointOfSaleRestClient.getPointOfSale(MERCHANT_ID, POS_ID))
                .thenReturn(ResponseEntity.ok().body(null));

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getPointOfSale(MERCHANT_ID, POS_ID)
        );

        assertEquals(AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR, ex.getCode());
    }


    @Test
    void getPointOfSale_feignExceptionThrown() {
        FeignException fe = mock(FeignException.class);

        when(pointOfSaleRestClient.getPointOfSale(MERCHANT_ID, POS_ID))
                .thenThrow(fe);

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getPointOfSale(MERCHANT_ID, POS_ID)
        );

        assertEquals(AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR, ex.getCode());
        verify(pointOfSaleRestClient).getPointOfSale(MERCHANT_ID, POS_ID);
    }
}

