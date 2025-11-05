package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.dto.request.WalletDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletRestClientImpl   {

    private final WalletRestClient walletRestClient;

    public WalletDTO walletDetail(String initiativeId, String userId) {
        try {
            ResponseEntity<WalletDTO> response = walletRestClient.walletDetail(initiativeId,userId);
            return response.getBody();
        } catch (Exception e) {
            throw new ServiceException("code","message");
        }
    }
}