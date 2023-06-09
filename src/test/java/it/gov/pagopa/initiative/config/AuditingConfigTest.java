package it.gov.pagopa.initiative.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.initiative.service.AuditorAwareImpl;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuditingConfig.class})
@ExtendWith(SpringExtension.class)
class AuditingConfigTest {
    @Autowired
    private AuditingConfig auditingConfig;

    @MockBean
    private HttpServletRequest httpServletRequest;

    @MockBean
    private MappingMongoConverter mappingMongoConverter;

    @Test
    void testMyAuditorProvider() {
        assertTrue(auditingConfig.myAuditorProvider() instanceof AuditorAwareImpl);
    }
}

