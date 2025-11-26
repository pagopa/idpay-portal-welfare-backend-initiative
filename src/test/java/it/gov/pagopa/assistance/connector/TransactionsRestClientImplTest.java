package it.gov.pagopa.assistance.connector;


import feign.FeignException;

import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TransactionsRestClientImpl.class
})
class TransactionsRestClientImplTest {

    @MockitoBean
    private TransactionsRestClient transactionsRestClient;

    @Autowired
    private TransactionsRestClientImpl service;

    private static final String TRX_ID = "TRX123";
    private static final String USER_ID = "USR123";


    @Test
    void getTransaction_OK() {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(TRX_ID);

        when(transactionsRestClient.findByTrxIdAndUserId(TRX_ID, USER_ID))
                .thenReturn(dto);

        TransactionDTO result = service.getTransaction(TRX_ID, USER_ID);

        assertNotNull(result);
        assertEquals(TRX_ID, result.getId());

        verify(transactionsRestClient).findByTrxIdAndUserId(TRX_ID, USER_ID);
    }


    @Test
    void getTransaction_nullResult_throwsServiceException() {
        when(transactionsRestClient.findByTrxIdAndUserId(TRX_ID, USER_ID))
                .thenReturn(null);

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getTransaction(TRX_ID, USER_ID)
        );

        assertEquals(
                AssistanceConstants.ConnectorError.ASSISTANCE_TRANSACTION_ERROR,
                ex.getCode()
        );
    }


    @Test
    void getTransaction_feignException_throwsServiceException() {
        FeignException fe = mock(FeignException.class);

        when(transactionsRestClient.findByTrxIdAndUserId(TRX_ID, USER_ID))
                .thenThrow(fe);

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getTransaction(TRX_ID, USER_ID)
        );

        assertEquals(
                AssistanceConstants.ConnectorError.ASSISTANCE_TRANSACTION_ERROR,
                ex.getCode()
        );

        verify(transactionsRestClient).findByTrxIdAndUserId(TRX_ID, USER_ID);
    }
}
