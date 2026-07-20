package it.gov.pagopa.initiative.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiativePageItem {
    @Id
    private String initiativeId;
    private String initiativeName;
    private String organizationName;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String onboardStatus;
    private Integer onboardStatusOrder;
    private List<String> atecoCodes;
}