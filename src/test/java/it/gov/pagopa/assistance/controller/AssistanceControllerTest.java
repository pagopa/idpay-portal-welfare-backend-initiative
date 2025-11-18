package it.gov.pagopa.assistance.controller;


import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import it.gov.pagopa.assistance.enums.Channel;
import it.gov.pagopa.assistance.service.AssistanceService;
import it.gov.pagopa.initiative.config.ServiceExceptionConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static it.gov.pagopa.assistance.costants.AssistanceConstants.USED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssistanceControllerImpl.class)
@ContextConfiguration(classes = { AssistanceControllerImpl.class })
class AssistanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssistanceService assistanceService;

    private static final String USER_ID = "687f8a176a5c92458819922a";
    private static final String INITIATIVE_ID = "687f8a176a5c92458819922a";

    @Test
    void onboardingStatus_OK() throws Exception {
        OnboardingStatusDTO onboardingStatusDTO = OnboardingStatusDTO.builder().channel(Channel.IO).build();
        Mockito.when(assistanceService.onboardingStatus(INITIATIVE_ID, USER_ID))
                .thenReturn(onboardingStatusDTO);

        mockMvc.perform(post("/idpay/assistance/onboardings/status/{initiativeId}/{userId}", INITIATIVE_ID, USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void vouchersStatus_OK() throws Exception {
        VouchersStatusDTO vouchersStatusDTO = VouchersStatusDTO.builder().status(USED).build();
        Mockito.when(assistanceService.vouchersStatus(INITIATIVE_ID, USER_ID))
                .thenReturn(vouchersStatusDTO);

        mockMvc.perform(post("/idpay/assistance/vouchers/status/{initiativeId}/{userId}", INITIATIVE_ID, USER_ID))
                .andExpect(status().isOk());
    }
}