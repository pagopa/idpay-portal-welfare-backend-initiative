package it.gov.pagopa.assistance.connector;

import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.WalletDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static it.gov.pagopa.assistance.utlis.Utils.extractMessageFromFeignException;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletRestClientImpl {

    private final WalletRestClient walletRestClient;

    public WalletDTO getWallet(String initiativeId, String userId) {
        log.debug("Calling Wallet MS for initiativeId={} userId={}", initiativeId, userId);
        try {
            ResponseEntity<WalletDTO> response = walletRestClient.walletDetail(initiativeId, userId);
            return response.getBody();
        } catch (FeignException e) {
            log.error("[ASSISTANCE] Error while calling Wallet MS for initiativeId={} userId={}", initiativeId, userId, e);
            throw new ServiceException(
                    AssistanceConstants.ConnectorError.ASSISTANCE_WALLET_ERROR,
                    extractMessageFromFeignException(e)
            );

        }
    }


}
