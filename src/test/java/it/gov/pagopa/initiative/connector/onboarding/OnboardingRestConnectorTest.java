package it.gov.pagopa.initiative.connector.onboarding;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.initiative.dto.ResponseOnboardingDTO;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OnboardingRestConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class OnboardingRestConnectorTest {
    @MockBean
    private OnboardingRestClient onboardingRestClient;

    @Autowired
    private OnboardingRestConnectorImpl onboardingRestConnectorImpl;

    @Test
    void testGetOnboarding() {
        ResponseOnboardingDTO responseOnboardingDTO = new ResponseOnboardingDTO();
        when(onboardingRestClient.getOnboarding((String) any(), (Pageable) any(), (String) any(), (LocalDateTime) any(),
                (LocalDateTime) any(), (String) any())).thenReturn(responseOnboardingDTO);
        LocalDateTime startDate = LocalDateTime.of(1, 1, 1, 1, 1);
        assertSame(responseOnboardingDTO, onboardingRestConnectorImpl.getOnboarding("42", null, "42", startDate,
                LocalDateTime.of(1, 1, 1, 1, 1), "Status"));
        verify(onboardingRestClient).getOnboarding((String) any(), (Pageable) any(), (String) any(), (LocalDateTime) any(),
                (LocalDateTime) any(), (String) any());
    }
}

