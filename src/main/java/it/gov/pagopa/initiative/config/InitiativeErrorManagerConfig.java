package it.gov.pagopa.initiative.config;

import it.gov.pagopa.common.web.dto.ErrorDTO;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitiativeErrorManagerConfig {
    @Bean
    ErrorDTO defaultErrorDTO() {
        return new ErrorDTO(
                InitiativeConstants.Exception.InternalServerError.INITIATIVE_GENERIC_ERROR,
                "A generic error occurred"
        );
    }

    @Bean
    ErrorDTO tooManyRequestsErrorDTO() {
        return new ErrorDTO("INITIATIVE_TOO_MANY_REQUEST", "Too Many Requests");
    }

    @Bean
    ErrorDTO templateValidationErrorDTO(){
        return new ErrorDTO("INITIATIVE_INVALID_REQUEST", null);
    }
}
