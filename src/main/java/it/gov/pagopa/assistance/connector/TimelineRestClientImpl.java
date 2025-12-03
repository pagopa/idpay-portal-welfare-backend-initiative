package it.gov.pagopa.assistance.connector;

import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.TimelineDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static it.gov.pagopa.assistance.utlis.Utils.extractMessageFromFeignException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimelineRestClientImpl {

    private final TimelineRestClient timelineRestClient;

    public TimelineDTO getTimeline(String initiativeId, String userId) {
        log.debug("Calling Timeline MS for initiativeId={} userId={}", initiativeId, userId);
        try {
            ResponseEntity<TimelineDTO> response = timelineRestClient.getTimeline(
                    initiativeId,
                    userId,
                    "TRANSACTION",
                    0, 20,
                    null, null
            );

            if (response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("[ASSISTANCE]  Empty or invalid response from Timeline MS for initiativeId={} userId={}", initiativeId, userId);
                return null;
            }

            return response.getBody();
        } catch (FeignException e) {
            log.error("[ASSISTANCE]  Error while calling Timeline MS for initiativeId={} userId={}", initiativeId, userId, e);
            throw new ServiceException(
                    AssistanceConstants.ConnectorError.ASSISTANCE_TIMELINE_ERROR,
                    extractMessageFromFeignException(e)
            );
        }
    }
}
