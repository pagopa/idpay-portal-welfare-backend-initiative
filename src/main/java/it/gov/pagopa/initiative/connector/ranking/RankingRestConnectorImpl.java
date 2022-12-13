package it.gov.pagopa.initiative.connector.ranking;

import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestClient;
import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestConnector;
import it.gov.pagopa.initiative.dto.RankingPageDTO;
import it.gov.pagopa.initiative.dto.RankingRequestDTO;
import it.gov.pagopa.initiative.dto.ResponseOnboardingDTO;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RankingRestConnectorImpl implements RankingRestConnector {

  private final RankingRestClient rankingRestClient;

  public RankingRestConnectorImpl(
      RankingRestClient rankingRestClient) {
    this.rankingRestClient = rankingRestClient;
  }

  @Override
  public RankingPageDTO getRankingList(String organizationId, String initiativeId,
          Pageable pageable, String beneficiaryRankingStatus, String userId) {
    return rankingRestClient.getRankingList(organizationId,initiativeId,pageable,beneficiaryRankingStatus, userId);
  }
}
