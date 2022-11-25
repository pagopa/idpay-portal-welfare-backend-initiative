package it.gov.pagopa.initiative.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import it.gov.pagopa.initiative.dto.ErrorDTO;

import java.io.DataInputStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
        assertEquals("An error occurred", body.getMessage());
        assertEquals("Code", body.getCode());
    }

    @Test
    void methodArgumentNotValidException() {

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
        assertEquals("Something gone wrong while notify Initiative for publishing", body.getMessage());
        assertEquals("it.gov.pagopa.initiative.published.bad.request", body.getCode());
    }
}

