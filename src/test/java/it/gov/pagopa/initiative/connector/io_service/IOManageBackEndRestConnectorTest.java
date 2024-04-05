package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.*;
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
                "rest-client.backend-io-manage.service.name=backend-io",
                "rest-client.backend-io-manage.service.subscriptionKey=subscriptionKey"
        })
@WebMvcTest(value = IOManageBackEndRestConnector.class)
//@RestClientTest(components = IOBackEndFeignRestClient.class)
//@Import(RestConnectorConfig.class)
class IOManageBackEndRestConnectorTest {
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    private static final String SUBSCRIPTION_KEY = "subscriptionKey";
    private static final String EMAIL = "test@pagopa.it";
    private static final String PHONE = "0123456789";
    private static final String SUPPORT_URL = "support.url.it";
    private static final String PRIVACY_URL = "privacy.url.it";
    private static final String TOS_URL = "tos.url.it";
    private static final String DESCRIPTION = "description";
    private static final String SCOPE = "LOCAL";
    private static final String SERVICE_NAME = "serviceName";
    private static final String PRODUCT_DEPARTMENT_NAME = "productDepartmentName";
    private static final String SERVICE_ID = "serviceId";
    private static final String PRIMARY_KEY = "primaryKey";
    private static final String SECONDARY_KEY = "secondaryKey";

    private static final Integer TOPIC_ID = 0;

    private static final TopicDTO TOPIC = new TopicDTO(TOPIC_ID,"Altro");

    @Autowired
    IOManageBackEndRestConnector ioManageBackEndRestConnector;

    @MockBean
    IOManageBackEndFeignRestClient ioManageBackEndFeignRestClient;

    @Test
    void givenServiceFromInitiativePublished_whenPostCreateServiceCalled_thenReturnOkResponse() {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        ResponseEntity<ServiceResponseDTO> entityExpected = new ResponseEntity<>(serviceResponseDTOexpected, HttpStatus.OK);
        Mockito.when(ioManageBackEndFeignRestClient.createService(serviceRequestDTO, SUBSCRIPTION_KEY)).thenReturn(entityExpected);

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\mappings (or can be done with wireMockServer.stubFor)
        ServiceResponseDTO serviceResponseDTO = ioManageBackEndRestConnector.createService(serviceRequestDTO);

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        Assertions.assertEquals(entityExpected.getBody(), serviceResponseDTO);

        Mockito.verify(ioManageBackEndFeignRestClient,times(1)).createService(serviceRequestDTO, SUBSCRIPTION_KEY);
    }


    private ServiceRequestDTO createServiceRequestDTO() {
        ServiceRequestMetadataDTO serviceMetadataDTO = createServiceRequestMetadataDTO();
        return ServiceRequestDTO.builder()
                .serviceName(SERVICE_NAME)
                .description(DESCRIPTION)
                .organization(createOrganizationDTO())
                .serviceMetadata(serviceMetadataDTO)
                .build();
    }

    private ServiceRequestMetadataDTO createServiceRequestMetadataDTO() {
        return ServiceRequestMetadataDTO.builder()
                .email(EMAIL)
                .phone(PHONE)
                .supportUrl(SUPPORT_URL)
                .privacyUrl(PRIVACY_URL)
                .tosUrl(TOS_URL)
                .scope(SCOPE)
                .topicId(TOPIC_ID)
                .build();
    }

    private OrganizationDTO createOrganizationDTO() {
        return OrganizationDTO.builder()
                .departmentName(PRODUCT_DEPARTMENT_NAME)
                .organizationName(ORGANIZATION_NAME)
                .organizationFiscalCode(ORGANIZATION_VAT)
                .build();
    }
    private ServiceResponseDTO createServiceResponseDTO() {
        ServiceResponseMetadataDTO serviceMetadataDTO = createServiceResponseMetadataDTO();
        return ServiceResponseDTO.builder()
                .id(SERVICE_ID)
                .serviceName(SERVICE_NAME)
                .organization(createOrganizationDTO())
                .serviceMetadata(serviceMetadataDTO)
                .build();
    }

    private ServiceResponseMetadataDTO createServiceResponseMetadataDTO() {
        return ServiceResponseMetadataDTO.builder()
                .email(EMAIL)
                .phone(PHONE)
                .supportUrl(SUPPORT_URL)
                .privacyUrl(PRIVACY_URL)
                .tosUrl(TOS_URL)
                .scope(SCOPE)
                .topic(TOPIC)
                .build();
    }

    private KeysDTO createServiceTokenKeys() {
        return KeysDTO.builder()
                .primaryKey(PRIMARY_KEY)
                .secondaryKey(SECONDARY_KEY)
                .build();
    }


    @Test
    void testSendLogoIo() {
        when(ioManageBackEndFeignRestClient.sendLogo(any(), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        LogoIODTO logoIODTO = new LogoIODTO("Logo");
        ioManageBackEndRestConnector.sendLogoIo(SERVICE_ID, logoIODTO);
        verify(ioManageBackEndFeignRestClient).sendLogo(any(), any(), any());
        assertEquals("Logo", logoIODTO.getLogo());
    }

    @Test
    void givenServiceFromInitiativePublished_whenUpdateService_thenReturnOkResponse() {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();
        String serviceId = serviceResponseDTOexpected.getId();

        ResponseEntity<ServiceResponseDTO> entityExpected = new ResponseEntity<>(serviceResponseDTOexpected, HttpStatus.OK);
        Mockito.when(ioManageBackEndFeignRestClient.updateService(serviceId, serviceRequestDTO, SUBSCRIPTION_KEY)).thenReturn(entityExpected);

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\mappings (or can be done with wireMockServer.stubFor)
        ServiceResponseDTO serviceResponseDTO = ioManageBackEndRestConnector.updateService(serviceId, serviceRequestDTO);

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        Assertions.assertEquals(entityExpected.getBody(), serviceResponseDTO);

        Mockito.verify(ioManageBackEndFeignRestClient,times(1)).updateService(serviceId, serviceRequestDTO, SUBSCRIPTION_KEY);
    }

    @Test
    void testDeleteServiceIo() {
        when(ioManageBackEndFeignRestClient.deleteService(any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        ioManageBackEndRestConnector.deleteService(SERVICE_ID);
        verify(ioManageBackEndFeignRestClient).deleteService(any(), any());
    }

    @Test
    void testGetServiceKeys() {
        KeysDTO serviceResponseDTOexpected = createServiceTokenKeys();

        ResponseEntity<KeysDTO> entityExpected = new ResponseEntity<>(serviceResponseDTOexpected, HttpStatus.OK);
        Mockito.when(ioManageBackEndFeignRestClient.getServiceKeys(SERVICE_ID, SUBSCRIPTION_KEY)).thenReturn(entityExpected);

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\mappings (or can be done with wireMockServer.stubFor)
        KeysDTO serviceResponseDTO = ioManageBackEndRestConnector.getServiceKeys(SERVICE_ID);

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        Assertions.assertEquals(entityExpected.getBody(), serviceResponseDTO);

        Mockito.verify(ioManageBackEndFeignRestClient,times(1)).getServiceKeys(SERVICE_ID, SUBSCRIPTION_KEY);
    }

}
