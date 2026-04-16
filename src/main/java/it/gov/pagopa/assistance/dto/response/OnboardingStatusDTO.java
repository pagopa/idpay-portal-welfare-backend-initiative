package it.gov.pagopa.assistance.dto.response;

import it.gov.pagopa.assistance.enums.Channel;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
@Builder
@Data
public class OnboardingStatusDTO {


    private Instant dateOfOnboardingRequest;
    private Instant dateOfOnboardingOk;
    private Channel channel;
    private String status;
    private String name;
    private String surname;
    private String email;
    private String detailKO;
}
