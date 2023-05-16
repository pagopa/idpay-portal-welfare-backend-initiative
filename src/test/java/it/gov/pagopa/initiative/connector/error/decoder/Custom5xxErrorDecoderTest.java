package it.gov.pagopa.initiative.connector.error.decoder;

import feign.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {Custom5xxErrorDecoder.class})
@ExtendWith(SpringExtension.class)
class Custom5xxErrorDecoderTest {
    @Autowired
    private Custom5xxErrorDecoder custom5xxErrorDecoder;

    @Test
    void testDecode_404(){
        Request request = Request.create(Request.HttpMethod.PUT, "url", new HashMap<>(), null, new RequestTemplate());
        Response response = Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .reason("Not found")
                .body(new byte[0])
                .request(request)
                .headers(new HashMap<>())
                .protocolVersion(null)
                .build();

        Exception ex = custom5xxErrorDecoder.decode("Method Key", response);
        assertTrue(ex instanceof FeignException);
    }
    @Test
    void testDecode_500(){
        Request request = Request.create(Request.HttpMethod.PUT, "url", new HashMap<>(), null, new RequestTemplate());
        Response response = Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .reason("Internal server error")
                .body(new byte[0])
                .request(request)
                .headers(new HashMap<>())
                .protocolVersion(null)
                .build();

        Exception ex = custom5xxErrorDecoder.decode("Method Key", response);
        assertTrue(ex instanceof RetryableException);
    }
}

