package it.gov.pagopa.initiative.connector.onboarding;

import it.gov.pagopa.initiative.dto.ResponseOnboardingDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

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
        when(onboardingRestClient.getOnboarding(any(), any(), any(), any(),
                any(), any())).thenReturn(responseOnboardingDTO);
        LocalDate startDate = LocalDate.of(1, 1, 1);
        assertSame(responseOnboardingDTO, onboardingRestConnectorImpl.getOnboarding("42", null, "42", startDate, startDate,"Status"));
        verify(onboardingRestClient).getOnboarding(any(), any(), any(), any(),
                any(), any());
    }
}

