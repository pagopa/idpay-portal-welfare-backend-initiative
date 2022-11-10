package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.service.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.io.IOException;

@Configuration
@EnableMongoAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<String> myAuditorProvider() throws IOException {
        return new AuditorAwareImpl();
    }
}
