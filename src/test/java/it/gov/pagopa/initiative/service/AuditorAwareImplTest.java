package it.gov.pagopa.initiative.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuditorAwareImpl.class})
@ExtendWith(SpringExtension.class)
class AuditorAwareImplTest {
    @Autowired
    private AuditorAwareImpl auditorAwareImpl;

    @MockBean
    private HttpServletRequest httpServletRequest;

    @Test
    void testGetCurrentAuditor() {
        when(httpServletRequest.getMethod()).thenReturn("https://example.org/example");
        Optional<String> actualCurrentAuditor = auditorAwareImpl.getCurrentAuditor();
        assertTrue(actualCurrentAuditor.isPresent());
        assertEquals("UNDEFINED", actualCurrentAuditor.get());
        verify(httpServletRequest).getMethod();
    }
}

