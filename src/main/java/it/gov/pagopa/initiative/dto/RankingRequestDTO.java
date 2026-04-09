package it.gov.pagopa.initiative.dto;

import java.time.Instant;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingRequestDTO {

    @NotEmpty
    private String userId;
    @NotEmpty
    private String initiativeId;
    @NotEmpty
    private String organizationId;
    @NotNull
    private Instant admissibilityCheckDate;
    private Instant criteriaConsensusTimestamp;
    private long rankingValue;
    private long ranking;
    private String beneficiaryRankingStatus;
    private String familyId;
    private Set<String> memberIds;
}
