package it.gov.pagopa.initiative.connector.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import feign.*;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import it.gov.pagopa.initiative.config.RestConnectorConfig;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.nio.charset.Charset;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "rest-client.backend-io.service.name=backend-io",
                "rest-client.backend-io.service.subscriptionKey=subscriptionKey"
        })
@Slf4j
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
    void givenServiceFromInitiativePublished_whenPostCreateServiceCalled_thenReturnOkResponse() throws Exception {
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
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .departmentName(PRODUCT_DEPARTMENT_NAME)
                .organizationName(ORGANIZATION_NAME)
                .organizationFiscalCode(ORGANIZATION_FISCAL_CODE)
                .isVisible(IS_VISIBLE)
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
        return ServiceResponseDTO.builder()
                .serviceId(SERVICE_ID)
                .build();
    }

}
