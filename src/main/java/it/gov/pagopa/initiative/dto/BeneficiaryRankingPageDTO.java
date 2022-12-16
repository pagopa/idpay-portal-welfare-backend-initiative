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
    private long totalElements;
    private long totalPages;
    private String rankingStatus;
    private LocalDateTime rankingPublishedTimeStamp;
    private LocalDateTime rankingGeneratedTimeStamp;
    private long totalEligibleOk;
    private long totalEligibleKo;
    private long totalOnboardingKo;
    private String rankingFilePath;

}

