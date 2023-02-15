package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "rest-client.backend-io.service.name=backend-io",
                "rest-client.backend-io.service.subscriptionKey=subscriptionKey"
        })
@WebMvcTest(value = IOBackEndRestConnector.class)
//@RestClientTest(components = IOBackEndFeignRestClient.class)
//@Import(RestConnectorConfig.class)
class IOBackEndRestConnectorTest {

    private static final String ROLE = "TEST_ROLE";
    private static final String INITIATIVE_ID = "initiativeId";
    private static final String ORGANIZATION_ID = "organizationId";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    private static final String ORGANIZATION_USER_ID = "organizationUserId";
    private static final String ORGANIZATION_USER_ROLE = "organizationUserRole";
    private static final String ANY_KEY_TOKEN_IO = "ANY_KEY_TOKEN_IO";
    private static final String EMAIL = "test@pagopa.it";
    private static final String PHONE = "0123456789";
    private static final String SUPPORT_URL = "support.url.it";
    private static final String PRIVACY_URL = "privacy.url.it";
    private static final String TOS_URL = "tos.url.it";
    private static final String DESCRIPTION = "description";
    private static final String SCOPE = "LOCAL";
    private static final boolean IS_VISIBLE = false;
    private static final String ORGANIZATION_FISCAL_CODE = "organizationFiscalCode";
    private static final String SERVICE_NAME = "serviceName";
    private static final String PRODUCT_DEPARTMENT_NAME = "productDepartmentName";
    private static final String SERVICE_ID = "serviceId";
    private static final String BASE_URL = "http://localhost:8080";

    @Autowired
    IOBackEndRestConnector ioBackEndRestConnector;

    @MockBean
    IOBackEndFeignRestClient ioBackEndFeignRestClient;

    @Test
    void givenServiceFromInitiativePublished_whenPostCreateServiceCalled_thenReturnOkResponse() {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        ResponseEntity<ServiceResponseDTO> entityExpected = new ResponseEntity<>(serviceResponseDTOexpected, HttpStatus.OK);
        Mockito.when(ioBackEndFeignRestClient.createService(serviceRequestDTO, "subscriptionKey")).thenReturn(entityExpected);

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\mappings (or can be done with wireMockServer.stubFor)
        ServiceResponseDTO serviceResponseDTO = ioBackEndRestConnector.createService(serviceRequestDTO);

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        Assertions.assertEquals(entityExpected.getBody(), serviceResponseDTO);

        Mockito.verify(ioBackEndFeignRestClient,times(1)).createService(serviceRequestDTO, "subscriptionKey");
    }


    private ServiceRequestDTO createServiceRequestDTO() {
        ServiceMetadataDTO serviceMetadataDTO = createServiceMetadataDTO();
        return ServiceRequestDTO.builder()
                .serviceName(SERVICE_NAME)
                .departmentName(PRODUCT_DEPARTMENT_NAME)
                .organizationName(ORGANIZATION_NAME)
                .organizationFiscalCode(ORGANIZATION_VAT)
                .isVisible(IS_VISIBLE)
                .serviceMetadata(serviceMetadataDTO)
                .build();
    }

    private ServiceMetadataDTO createServiceMetadataDTO() {
        return ServiceMetadataDTO.builder()
                .email(EMAIL)
                .phone(PHONE)
                .supportUrl(SUPPORT_URL)
                .privacyUrl(PRIVACY_URL)
                .tosUrl(TOS_URL)
                .description(DESCRIPTION)
                .scope(SCOPE)
                .build();
    }

    private ServiceResponseDTO createServiceResponseDTO() {
        ServiceMetadataDTO serviceMetadataDTO = createServiceMetadataDTO();
        return ServiceResponseDTO.builder()
                .serviceId(SERVICE_ID)
                .serviceName(SERVICE_NAME)
                .departmentName(PRODUCT_DEPARTMENT_NAME)
                .organizationName(ORGANIZATION_NAME)
                .organizationFiscalCode(ORGANIZATION_VAT)
                .isVisible(IS_VISIBLE)
                .primaryKey(ANY_KEY_TOKEN_IO)
                .serviceMetadata(serviceMetadataDTO)
                .build();
    }

    @Test
    void testSendLogoIo() {
        when(ioBackEndFeignRestClient.sendLogo(any(), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        LogoIODTO logoIODTO = new LogoIODTO("Logo");
        ioBackEndRestConnector.sendLogoIo(SERVICE_ID, "Primary Key", logoIODTO);
        verify(ioBackEndFeignRestClient).sendLogo(any(), any(), any());
        assertEquals("Logo", logoIODTO.getLogo());
    }

    @Test
    void givenServiceFromInitiativePublished_whenUpdateService_thenReturnOkResponse() {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();
        String serviceId = serviceResponseDTOexpected.getServiceId();

        ResponseEntity<ServiceResponseDTO> entityExpected = new ResponseEntity<>(serviceResponseDTOexpected, HttpStatus.OK);
        Mockito.when(ioBackEndFeignRestClient.updateService(serviceId, serviceRequestDTO, ANY_KEY_TOKEN_IO)).thenReturn(entityExpected);

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\mappings (or can be done with wireMockServer.stubFor)
        ServiceResponseDTO serviceResponseDTO = ioBackEndRestConnector.updateService(serviceId, serviceRequestDTO, serviceResponseDTOexpected.getPrimaryKey());

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        Assertions.assertEquals(entityExpected.getBody(), serviceResponseDTO);

        Mockito.verify(ioBackEndFeignRestClient,times(1)).updateService(serviceId, serviceRequestDTO, ANY_KEY_TOKEN_IO);
    }
}
