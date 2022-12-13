package it.gov.pagopa.initiative.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaryRankingDTO {

    private String beneficiary;
    private LocalDateTime criteriaConsensusTimestamp;
    private long rankingValue;
    private long ranking;
    private String beneficiaryRankingStatus;
}
