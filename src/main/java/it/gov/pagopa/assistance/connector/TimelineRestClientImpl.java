package it.gov.pagopa.assistance.connector;

import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.Operation;
import it.gov.pagopa.assistance.dto.request.TimelineDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.gov.pagopa.assistance.utlis.Utils.extractMessageFromFeignException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimelineRestClientImpl {

    private final TimelineRestClient timelineRestClient;

    public List<Operation> getTimeline(String initiativeId, String userId) {
        log.debug("Calling Timeline MS for initiativeId={} userId={}", initiativeId, userId);
        try {
            ResponseEntity<TimelineDTO> response = timelineRestClient.getTimeline(
                    initiativeId,
                    userId,
                    "TRANSACTION",
                    0, 10,
                    null, null
            );

            return response.getBody().getOperationList();
        } catch (FeignException e) {
            if(e.status() == 404)
                return List.of();
            log.error("[ASSISTANCE]  Error while calling Timeline MS for initiativeId={} userId={}", initiativeId, userId, e);
            throw new ServiceException(
                    AssistanceConstants.ConnectorError.ASSISTANCE_TIMELINE_ERROR,
                    extractMessageFromFeignException(e)
            );
        }
    }
}
