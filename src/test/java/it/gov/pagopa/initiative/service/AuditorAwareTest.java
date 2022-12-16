package it.gov.pagopa.initiative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuditorAwareImpl.class})
@ExtendWith(SpringExtension.class)
class AuditorAwareTest {
    @Autowired
    private AuditorAwareImpl auditorAwareImpl;

    @MockBean
    private HttpServletRequest httpServletRequest;

    @Test
    void testGetCurrentAuditor() {
        when(httpServletRequest.getMethod()).thenReturn(HttpMethod.GET.toString());
        Optional<String> actualCurrentAuditor = auditorAwareImpl.getCurrentAuditor();
        assertTrue(actualCurrentAuditor.isPresent());
        assertEquals("UNDEFINED", actualCurrentAuditor.get());
        verify(httpServletRequest).getMethod();
    }

    @Test
    void testGetCurrentAuditor_switchCases_POST() {
        String organizationUserId = "";
        when(httpServletRequest.getMethod()).thenReturn(HttpMethod.POST.toString());
        when(httpServletRequest.getHeader(any())).thenReturn((organizationUserId));
        assertEquals(organizationUserId, httpServletRequest.getHeader(any()));
        assertThat(auditorAwareImpl.getCurrentAuditor()).hasValue(organizationUserId);
        }

    @Test
    void testGetCurrentAuditor_switchCases_POST_Exception() {
        when(httpServletRequest.getHeader(any())).thenThrow(new IllegalStateException());
        when(httpServletRequest.getMethod()).thenReturn(HttpMethod.POST.toString());
        assertThrows(IllegalStateException.class, () -> auditorAwareImpl.getCurrentAuditor());
        verify(httpServletRequest).getHeader(any());
        verify(httpServletRequest).getMethod();
    }
}

