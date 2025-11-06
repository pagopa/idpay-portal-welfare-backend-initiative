package it.gov.pagopa.assistance.dto.response;

import it.gov.pagopa.assistance.enums.Channel;
import it.gov.pagopa.assistance.enums.PointOfSaleTypeEnum;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public class OnboardingStatusDTO {

    private String name;
    private String surname;
    private LocalDateTime dateOfBirth;
    private LocalDateTime dateOfOnboardingRequest;
    private String iseeOptionSelected;
    private Channel channel;
    private String email;
    private String status;

}
