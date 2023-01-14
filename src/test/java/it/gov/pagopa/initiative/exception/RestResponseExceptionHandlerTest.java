package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.initiative.dto.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;

import java.io.DataInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RestResponseExceptionHandlerTest {

    @Test
    void testHandleInitiativeException() {
        RestResponseExceptionHandler restResponseExceptionHandler = new RestResponseExceptionHandler();
        ResponseEntity<ErrorDTO> actualHandleInitiativeExceptionResult = restResponseExceptionHandler
                .handleInitiativeException(new InitiativeException("Code", "An error occurred", HttpStatus.CONTINUE));
        assertTrue(actualHandleInitiativeExceptionResult.hasBody());
        assertTrue(actualHandleInitiativeExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.CONTINUE, actualHandleInitiativeExceptionResult.getStatusCode());
        ErrorDTO body = actualHandleInitiativeExceptionResult.getBody();
        assert body != null;
        assertEquals("An error occurred", body.getMessage());
        assertEquals("Code", body.getCode());
    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        RestResponseExceptionHandler restResponseExceptionHandler = new RestResponseExceptionHandler();
        ResponseEntity<ErrorDTO> actualHandleHttpMessageNotReadableExceptionResult = restResponseExceptionHandler
                .handleHttpMessageNotReadableException(new HttpMessageNotReadableException("https://example.org/example"));
        assertTrue(actualHandleHttpMessageNotReadableExceptionResult.hasBody());
        assertTrue(actualHandleHttpMessageNotReadableExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleHttpMessageNotReadableExceptionResult.getStatusCode());
        ErrorDTO body = actualHandleHttpMessageNotReadableExceptionResult.getBody();
        assert body != null;
        assertEquals("https://example.org/example", body.getMessage());
        assertEquals("it.gov.pagopa.initiative.bad.request", body.getCode());
    }

    @Test
    void testHandleHttpMessageNotReadableException1() {
        RestResponseExceptionHandler restResponseExceptionHandler = new RestResponseExceptionHandler();
        ResponseEntity<ErrorDTO> actualHandleHttpMessageNotReadableExceptionResult = restResponseExceptionHandler
                .handleHttpMessageNotReadableException(new HttpMessageNotReadableException("https://example.org/example",
                        new MockHttpInputMessage(mock(DataInputStream.class))));
        assertTrue(actualHandleHttpMessageNotReadableExceptionResult.hasBody());
        assertTrue(actualHandleHttpMessageNotReadableExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleHttpMessageNotReadableExceptionResult.getStatusCode());
        ErrorDTO body = actualHandleHttpMessageNotReadableExceptionResult.getBody();
        assert body != null;
        assertEquals("https://example.org/example", body.getMessage());
        assertEquals("it.gov.pagopa.initiative.bad.request", body.getCode());
    }

    @Test
    void testHandleIntegrationException() {
        RestResponseExceptionHandler restResponseExceptionHandler = new RestResponseExceptionHandler();
        ResponseEntity<ErrorDTO> actualHandleIntegrationExceptionResult = restResponseExceptionHandler
                .handleIntegrationException(new IntegrationException(HttpStatus.CONTINUE));
        assertTrue(actualHandleIntegrationExceptionResult.hasBody());
        assertTrue(actualHandleIntegrationExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.CONTINUE, actualHandleIntegrationExceptionResult.getStatusCode());
        ErrorDTO body = actualHandleIntegrationExceptionResult.getBody();
        assert body != null;
        assertEquals("Something gone wrong while notify Initiative for publishing", body.getMessage());
        assertEquals("it.gov.pagopa.initiative.published.bad.request", body.getCode());
    }

    @Test
    void testHandleGenericException() {
        RestResponseExceptionHandler restResponseExceptionHandler = new RestResponseExceptionHandler();
        Exception exception = new Exception();
        ResponseEntity<ErrorDTO> actualHandleGenericExceptionResult = restResponseExceptionHandler
                .handleGenericException(exception);
        assertTrue(actualHandleGenericExceptionResult.hasBody());
        assertTrue(actualHandleGenericExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualHandleGenericExceptionResult.getStatusCode());
        ErrorDTO body = actualHandleGenericExceptionResult.getBody();
        assert body != null;
        assertNull(body.getMessage());
        assertEquals("it.gov.pagopa.initiative.general.error", body.getCode());
    }
}

