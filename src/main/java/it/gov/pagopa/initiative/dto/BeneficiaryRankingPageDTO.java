package it.gov.pagopa.initiative.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaryRankingPageDTO {

    private List<BeneficiaryRankingDTO> content;
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private Long totalPages;
    private String rankingStatus;
    private LocalDateTime rankingPublishedTimestamp;
    private LocalDateTime rankingGeneratedTimestamp;
    private Long totalEligibleOk;
    private Long totalEligibleKo;
    private Long totalOnboardingKo;
    private String rankingFilePath;

}

