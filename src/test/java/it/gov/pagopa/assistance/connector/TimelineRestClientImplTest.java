package it.gov.pagopa.assistance.connector;

import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.Operation;
import it.gov.pagopa.assistance.dto.request.TimelineDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TimelineRestClientImpl.class
})
class TimelineRestClientImplTest {

    @MockitoBean
    private TimelineRestClient timelineRestClient;

    @Autowired
    private TimelineRestClientImpl service;

    private static final String INITIATIVE_ID = "INIT123";
    private static final String USER_ID = "USR123";



    @Test
    void getTimeline_OK() {
        Operation operation = new Operation();
        operation.setEventId("eventId");
        TimelineDTO dto = new TimelineDTO();
        dto.setOperationList(List.of(operation));
        when(timelineRestClient.getTimeline(
                INITIATIVE_ID,
                USER_ID,
                "TRANSACTION",
                0, 10,
                null, null
        )).thenReturn(ResponseEntity.ok(dto));

        List<Operation> result = service.getTimeline(INITIATIVE_ID, USER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(timelineRestClient).getTimeline(
                INITIATIVE_ID,
                USER_ID,
                "TRANSACTION",
                0, 10,
                null, null
        );
    }






    @Test
    void getTimeline_feignExceptionThrown() {
        FeignException fe = mock(FeignException.class);

        when(timelineRestClient.getTimeline(
                INITIATIVE_ID,
                USER_ID,
                "TRANSACTION",
                0, 10,
                null, null
        )).thenThrow(fe);

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getTimeline(INITIATIVE_ID, USER_ID)
        );

        assertEquals(
                AssistanceConstants.ConnectorError.ASSISTANCE_TIMELINE_ERROR,
                ex.getCode()
        );

        verify(timelineRestClient).getTimeline(
                INITIATIVE_ID,
                USER_ID,
                "TRANSACTION",
                0, 10,
                null, null
        );
    }
}
