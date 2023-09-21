package it.gov.pagopa.initiative.connector.io_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.*;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "rest-client.backend-io-manage.service.name=backend-io",
                "rest-client.backend-io-manage.service.subscriptionKey=subscriptionKey"
        })
@WebMvcTest(value = IOManageBackEndRestConnector.class)
//@RestClientTest(components = IOBackEndFeignRestClient.class)
//@Import(RestConnectorConfig.class)
class IOManageBackEndRestConnectorTest {

    private static final String SERVICE_ID = "serviceId";

    @Autowired
    IOManageBackEndRestConnector ioManageBackEndRestConnector;

    @MockBean
    IOManageBackEndFeignRestClient ioManageBackEndFeignRestClient;

    @Test
    void testDeleteServiceIo() {
        when(ioManageBackEndFeignRestClient.deleteService(any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        ioManageBackEndRestConnector.deleteService(SERVICE_ID);
        verify(ioManageBackEndFeignRestClient).deleteService(any(), any());
    }

}
