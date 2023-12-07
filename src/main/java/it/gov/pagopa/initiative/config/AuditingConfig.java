package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.service.AuditorAwareImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<String> myAuditorProvider(HttpServletRequest request) {
        return new AuditorAwareImpl(request);
    }
}
