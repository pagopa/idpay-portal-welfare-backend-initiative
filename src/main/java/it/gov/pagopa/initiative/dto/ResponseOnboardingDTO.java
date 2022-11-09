package it.gov.pagopa.initiative.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOnboardingDTO {
  private List<OnboardingStatusCitizenDTO> onboardingStatusCitizenDTOList;
  private int pageNo;
  private int pageSize;
  private int totalElements;
  private int totalPages;
}

