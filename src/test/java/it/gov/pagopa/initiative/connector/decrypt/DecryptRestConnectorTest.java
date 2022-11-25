package it.gov.pagopa.initiative.connector.decrypt;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.initiative.dto.DecryptCfDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DecryptRestConnectorImpl.class, String.class})
@ExtendWith(SpringExtension.class)
class DecryptRestConnectorTest {
    @MockBean
    private DecryptRest decryptRest;

    @Autowired
    private DecryptRestConnectorImpl decryptRestConnectorImpl;

    @Test
    void testGetPiiByToken() {
        DecryptCfDTO decryptCfDTO = new DecryptCfDTO("Pii");
        when(decryptRest.getPiiByToken((String) any(), (String) any())).thenReturn(decryptCfDTO);
        assertSame(decryptCfDTO, decryptRestConnectorImpl.getPiiByToken("ABC123"));
        verify(decryptRest).getPiiByToken((String) any(), (String) any());
    }
}

