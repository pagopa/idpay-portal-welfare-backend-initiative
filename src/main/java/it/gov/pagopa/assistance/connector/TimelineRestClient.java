package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.dto.request.TimelineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@FeignClient(
        name = "${rest-client.timeline.serviceCode}",
        url = "${rest-client.timeline.baseUrl}"
)
public interface TimelineRestClient {

    @GetMapping(
            value = "/idpay/timeline/{initiativeId}/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TimelineDTO> getTimeline(
            @PathVariable("initiativeId") String initiativeId,
            @PathVariable("userId") String userId,
            @RequestParam(required = false) String operationType,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dateFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dateTo
    );
}