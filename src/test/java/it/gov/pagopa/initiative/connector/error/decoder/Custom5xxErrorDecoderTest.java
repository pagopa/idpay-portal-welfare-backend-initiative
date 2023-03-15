package it.gov.pagopa.initiative.connector.error.decoder;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {Custom5xxErrorDecoder.class})
@ExtendWith(SpringExtension.class)
class Custom5xxErrorDecoderTest {
    @Autowired
    private Custom5xxErrorDecoder custom5xxErrorDecoder;

    @Disabled
    void testDecode() throws Exception {
        Response response = Response.builder().build();
        FeignException exception = feign.FeignException.errorStatus("Method Key", response);

        Mockito.doThrow(new FeignException.InternalServerError(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).when(custom5xxErrorDecoder.decode(
                "Method Key",
                response));

        try {
            custom5xxErrorDecoder.decode("Method Key", response);
        } catch (RetryableException e) {
            assertEquals(response.status(), e.status());
            assertEquals(exception.getMessage(), e.getMessage());
            assertEquals(response.request().httpMethod(), e.request().httpMethod());

        }
    }
}

