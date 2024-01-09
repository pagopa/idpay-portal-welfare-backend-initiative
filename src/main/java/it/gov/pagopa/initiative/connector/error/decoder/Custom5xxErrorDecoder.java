package it.gov.pagopa.initiative.connector.error.decoder;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Custom5xxErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);
        log.debug("Feign Client Exception caught with Status [{}] during [{}] to [{}]", response.status(), response.request().httpMethod().name(), response.request().url());
        int status = response.status();
        if (status >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            log.debug("Retrying is about to start");
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    (Long)null,
                    response.request());
        }
        return exception;
    }
}
