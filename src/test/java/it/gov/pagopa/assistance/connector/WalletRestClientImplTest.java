package it.gov.pagopa.assistance.connector;


import feign.FeignException;
import it.gov.pagopa.assistance.dto.request.WalletDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
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
        WalletRestClientImpl.class
})
class WalletRestClientImplTest {

    @MockitoBean
    private WalletRestClient walletRestClient;

    @Autowired
    private WalletRestClientImpl service;

    private static final String INITIATIVE_ID = "INIT123";
    private static final String USER_ID = "USR123";



    @Test
    void getWallet_OK() {
        WalletDTO dto = new WalletDTO();
        dto.setInitiativeId(INITIATIVE_ID);

        when(walletRestClient.walletDetail(INITIATIVE_ID, USER_ID))
                .thenReturn(ResponseEntity.ok(dto));

        WalletDTO result = service.getWallet(INITIATIVE_ID, USER_ID);

        assertNotNull(result);
        assertEquals(INITIATIVE_ID, result.getInitiativeId());

        verify(walletRestClient).walletDetail(INITIATIVE_ID, USER_ID);
    }



    @Test
    void getWallet_feignException_throwsServiceException() {
        FeignException fe = mock(FeignException.class);

        when(walletRestClient.walletDetail(INITIATIVE_ID, USER_ID))
                .thenThrow(fe);

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getWallet(INITIATIVE_ID, USER_ID)
        );

        assertEquals(AssistanceConstants.ConnectorError.ASSISTANCE_WALLET_ERROR, ex.getCode());

        verify(walletRestClient).walletDetail(INITIATIVE_ID, USER_ID);
    }
}
