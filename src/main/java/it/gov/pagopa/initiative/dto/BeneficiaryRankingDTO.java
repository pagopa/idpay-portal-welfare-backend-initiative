package it.gov.pagopa.initiative.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private String familyId;
    private List<String> memberIds;
}
