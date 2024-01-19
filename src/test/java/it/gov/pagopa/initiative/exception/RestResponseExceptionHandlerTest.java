package it.gov.pagopa.initiative.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import it.gov.pagopa.common.web.dto.ErrorDTO;
import java.io.DataInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;

class RestResponseExceptionHandlerTest {

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
        assertEquals("INITIATIVE_INVALID_REQUEST", body.getCode());
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
        assertEquals("INITIATIVE_INVALID_REQUEST", body.getCode());
    }
}

