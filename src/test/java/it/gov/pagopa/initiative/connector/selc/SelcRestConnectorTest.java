package it.gov.pagopa.initiative.connector.selc;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.initiative.dto.selc.UserResource;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        when(selcFeignRestClient.getInstitutionProductUsers((String) any(), (String) any(), (String) any()))
                .thenReturn(userResourceList);
        List<UserResource> actualInstitutionProductUsers = selcRestConnectorImpl.getInstitutionProductUsers("42");
        assertSame(userResourceList, actualInstitutionProductUsers);
        assertTrue(actualInstitutionProductUsers.isEmpty());
        verify(selcFeignRestClient).getInstitutionProductUsers((String) any(), (String) any(), (String) any());
    }
}

