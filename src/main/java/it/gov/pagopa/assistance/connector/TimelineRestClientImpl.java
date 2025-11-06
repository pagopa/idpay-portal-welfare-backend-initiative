package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.TimelineDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimelineRestClientImpl {

    private final TimelineRestClient timelineRestClient;

    public TimelineDTO getTransactionOperation(String initiativeId, String userId) {
        try {
            ResponseEntity<TimelineDTO> resposne = timelineRestClient.getTimeline(
                    initiativeId,
                    userId,
                    "TRANSACTION",
                    null, null,
                    null, null
            );
            return resposne.getBody();
        } catch (Exception e) {
            throw new ServiceException(AssistanceConstants.ConnectorError.ASSISTANCE_TIMELINE_ERROR,"Error While Call Timeline MS");
        }
    }
}
