package it.gov.pagopa.initiative.connector.ranking;

import it.gov.pagopa.initiative.dto.RankingPageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RankingRestConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class RankingRestConnectorTest {

    public static final String ORGANIZATION_ID = "organizationId1";
    public static final String INITIATIVE_ID = "initiativeId";
    private static final String USER_ID = "USER_ID";

    @MockBean
    private RankingRestClient rankingRestClient;

    @Autowired
    private RankingRestConnectorImpl rankingRestConnectorImpl;

    @Test
    void testGetRankingList() {
        RankingPageDTO rankingPageDTO = new RankingPageDTO();
        when(rankingRestClient.getRankingList(any(), any(), any(), any(),
                any())).thenReturn(rankingPageDTO);
        assertSame(rankingPageDTO,
                rankingRestConnectorImpl.getRankingList(ORGANIZATION_ID, INITIATIVE_ID, null, "Beneficiary Ranking " +
                        "Status", USER_ID));
        verify(rankingRestClient).getRankingList(any(), any(), any(), any(),
                any());
    }
}

