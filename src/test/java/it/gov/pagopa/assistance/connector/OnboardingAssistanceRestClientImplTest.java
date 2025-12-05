package it.gov.pagopa.assistance.connector;

import feign.FeignException;
import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.OnboardingDTO;
import it.gov.pagopa.common.web.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        OnboardingAssistanceRestClientImpl.class,
})
class OnboardingAssistanceRestClientImplTest {

    @MockitoBean
    private OnboardingAssistanceRestClient onboardingRestClient;

    @Autowired
    private OnboardingAssistanceRestClientImpl service;

    private static final String INITIATIVE_ID = "INIT123";
    private static final String USER_ID = "USR123";

    @Test
    void getOnboardingStatus_OK() {
        OnboardingDTO dto = new OnboardingDTO();
        dto.setName("Alice");

        when(onboardingRestClient.onboardingStatus(INITIATIVE_ID, USER_ID))
                .thenReturn(ResponseEntity.ok(dto));

        OnboardingDTO result = service.getOnboardingStatus(INITIATIVE_ID, USER_ID);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(onboardingRestClient).onboardingStatus(INITIATIVE_ID, USER_ID);
    }





    @Test
    void getOnboardingStatus_feignExceptionThrown() {
        FeignException fe = mock(FeignException.class);

        when(onboardingRestClient.onboardingStatus(INITIATIVE_ID, USER_ID))
                .thenThrow(fe);

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> service.getOnboardingStatus(INITIATIVE_ID, USER_ID)
        );

        assertEquals(AssistanceConstants.ConnectorError.ASSISTANCE_ONBOARDING_ERROR,  ex.getCode());
        verify(onboardingRestClient).onboardingStatus(INITIATIVE_ID, USER_ID);
    }
}

