package it.gov.pagopa.initiative.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiativePageItem {
    @Id
    private String initiativeId;
    private String initiativeName;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String onboardStatus;
    private Integer onboardStatusOrder;
}