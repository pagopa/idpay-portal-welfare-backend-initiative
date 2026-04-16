package it.gov.pagopa.initiative.connector.selc;

import it.gov.pagopa.initiative.dto.selc.UserResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {SelcRestConnectorImpl.class, String.class})
@ExtendWith(SpringExtension.class)
class SelcRestConnectorTest {
    @MockBean
    private SelcFeignRestClient selcFeignRestClient;

    @Autowired
    private SelcRestConnectorImpl selcRestConnectorImpl;

    @Test
    void testGetInstitutionProductUsers() {
        ArrayList<UserResource> userResourceList = new ArrayList<>();
        when(selcFeignRestClient.getInstitutionProductUsers(any(), any(), any()))
                .thenReturn(userResourceList);
        List<UserResource> actualInstitutionProductUsers = selcRestConnectorImpl.getInstitutionProductUsers("42");
        assertSame(userResourceList, actualInstitutionProductUsers);
        assertTrue(actualInstitutionProductUsers.isEmpty());
        verify(selcFeignRestClient).getInstitutionProductUsers(any(), any(), any());
    }
}

