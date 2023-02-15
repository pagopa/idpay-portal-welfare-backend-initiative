package it.gov.pagopa.initiative.connector.io_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import feign.FeignException;
import it.gov.pagopa.initiative.config.IOBackEndRestConnectorConfig;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseErrorDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "rest-client.backend-io.service.name=backend-io",
                "rest-client.backend-io.service.subscriptionKey=subscriptionKey"
        })
@SpringBootTest
@ContextConfiguration(
        initializers = IOBackEndFeignRestClientTest.WireMockInitializer.class,
        classes = {
                IOBackEndRestConnectorImpl.class,
                IOBackEndFeignRestClient.class,
                IOBackEndRestConnectorConfig.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
class IOBackEndFeignRestClientTest {

    private static final String ROLE = "TEST_ROLE";
    private static final String INITIATIVE_ID = "initiativeId";
    private static final String ORGANIZATION_ID = "organizationId";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    private static final String ORGANIZATION_VAT_NOT_VALID = "organizationVatNotValid";
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
    private static final String SERVICE_NAME = "serviceName";
    private static final String PRODUCT_DEPARTMENT_NAME = "productDepartmentName";
    private static final String SERVICE_ID = "serviceId";

    public static class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            //Instruct a Server to be the PDV Mock Server
            wireMockServer = new WireMockServer(new WireMockConfiguration()
                    .dynamicPort()
                    .usingFilesUnderClasspath("src/test/resources/stub")
            );
            wireMockServer.start();

            configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);

            configurableApplicationContext.addApplicationListener(
                    applicationEvent -> {
                        if (applicationEvent instanceof ContextClosedEvent) {
                            wireMockServer.stop();
                        }
                    });

            //Override test properties to instruct FeignClient Service to use the same address & port of WireMockServer
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    configurableApplicationContext,
                    String.format(
                            "rest-client.backend-io.service.base-url=http://%s:%d",
                            wireMockServer.getOptions().bindAddress(), wireMockServer.port()));
        }
    }

    private static WireMockServer wireMockServer;

    @Autowired
    IOBackEndRestConnector ioBackEndRestConnector;

    @Autowired
    IOBackEndFeignRestClient ioBackEndFeignRestClient;

    @Test
    void givenServiceFromInitiativePublished_whenPostCreateServiceCalled_thenReturnMatchingService_200() throws JsonProcessingException {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        //Prepare json to be verified if it will be the same for call
        String serviceRequestDTOjson = new ObjectMapper().writeValueAsString(serviceRequestDTO);
        //Prepare json to be returned
        String serviceResponseDTOjson = new ObjectMapper().writeValueAsString(serviceResponseDTOexpected);
        
        wireMockServer.stubFor(post(urlEqualTo("/services"))
                .withRequestBody(equalToJson(serviceRequestDTOjson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(serviceResponseDTOjson)
                        .withStatus(HttpStatus.OK.value())
                )
        );

        //Connector will call the fake server and expecting to reply what we Stub previously with wireMockServer.stubFor (or also can be done with src\resources\stub\mappings)
        ResponseEntity<ServiceResponseDTO> responseEntity = ioBackEndFeignRestClient.createService(serviceRequestDTO, "subscriptionKey");
        ServiceResponseDTO serviceResponseDTO = responseEntity.getBody();

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        //Match json values and expect being done 1 external call
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.postRequestedFor(WireMock.urlEqualTo("/services"))
                        .withRequestBody(equalToJson(serviceRequestDTOjson, true, false))
        );
    }

    @Test
    void givenServiceNotValid_FromInitiativePublished_whenPostCreateServiceCalled_thenReturnException_400() throws JsonProcessingException {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTOnotValid();
        ServiceResponseErrorDTO serviceResponseErrorDTO = createServiceResponseErrorDTO(400);

        //Prepare json to be verified if it will be the same for call
        String serviceRequestDTOjson = new ObjectMapper().writeValueAsString(serviceRequestDTO);
        //Prepare json to be returned
        String serviceResponseErrorDTOjson = new ObjectMapper().writeValueAsString(serviceResponseErrorDTO);

        wireMockServer.stubFor(post(urlEqualTo("/services"))
                .withRequestBody(equalToJson(serviceRequestDTOjson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(serviceResponseErrorDTOjson)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        Executable executable = () -> ioBackEndFeignRestClient.createService(serviceRequestDTO, "subscriptionKey");
        FeignException exception = Assertions.assertThrows(FeignException.class, executable);
        assertThat(exception.getMessage()).contains("[400 Bad Request]");
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.postRequestedFor(WireMock.urlEqualTo("/services"))
                        .withRequestBody(equalToJson(serviceRequestDTOjson, true, false))
        );
    }

    @Test
    void givenServiceNotValid_FromInitiativePublished_whenPostCreateServiceCalled_thenReturnException_500() throws JsonProcessingException {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTOnotValid();
        ServiceResponseErrorDTO serviceResponseErrorDTO = createServiceResponseErrorDTO(500);

        //Prepare json to be verified if it will be the same for call
        String serviceRequestDTOjson = new ObjectMapper().writeValueAsString(serviceRequestDTO);
        //Prepare json to be returned
        String serviceResponseErrorDTOjson = new ObjectMapper().writeValueAsString(serviceResponseErrorDTO);

        wireMockServer.stubFor(post(urlEqualTo("/services"))
                .withRequestBody(equalToJson(serviceRequestDTOjson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(serviceResponseErrorDTOjson)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
        );

        Executable executable = () -> ioBackEndFeignRestClient.createService(serviceRequestDTO, "subscriptionKey");
        FeignException exception = Assertions.assertThrows(FeignException.class, executable);
        assertThat(exception.getMessage()).contains("[500 Server Error]");
        assertThat(exception.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.postRequestedFor(WireMock.urlEqualTo("/services"))
                        .withRequestBody(equalToJson(serviceRequestDTOjson, true, false))
        );
    }

    @Test
    void givenServiceFromInitiativePublished_whenPutUpdateServiceCalled_thenReturnMatchingService_200() throws JsonProcessingException {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        String cta = InitiativeConstants.Cta.CTA + InitiativeConstants.Cta.CTA_NIT + InitiativeConstants.Cta.CTA_1
                + InitiativeConstants.Cta.CTA_TEXT + InitiativeConstants.Cta.CTA_ACTION + SERVICE_ID + InitiativeConstants.Cta.CTA_NEN
                + InitiativeConstants.Cta.CTA + InitiativeConstants.Cta.CTA_NIT + InitiativeConstants.Cta.CTA_1
                + InitiativeConstants.Cta.CTA_TEXT_ENGLISH + InitiativeConstants.Cta.CTA_ACTION + SERVICE_ID + InitiativeConstants.Cta.CTA_NEN + InitiativeConstants.Cta.CTA_END;

        serviceRequestDTO.getServiceMetadata().setCta(cta);
        serviceResponseDTOexpected.getServiceMetadata().setCta(cta);

        //Prepare json to be verified if it will be the same for call
        String serviceRequestDTOjson = new ObjectMapper().writeValueAsString(serviceRequestDTO);
        
        //JSON to be returned placed here: src\resources\stub\mappings\io\digital_citizenship_api_put_updateService_200.json

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\stub\mappings (or can be done with wireMockServer.stubFor)
        ResponseEntity<ServiceResponseDTO> responseEntity = ioBackEndFeignRestClient.updateService(SERVICE_ID, serviceRequestDTO, "subscriptionKey");
        ServiceResponseDTO serviceResponseDTO = responseEntity.getBody();

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        //Match json values and expect being done 1 external call
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.putRequestedFor(WireMock.urlEqualTo("/services/" + SERVICE_ID))
                        .withRequestBody(equalToJson(serviceRequestDTOjson, true, false))
        );
    }

    @Test
    void givenServiceNotValid_FromInitiativePublished_whenPutUpdateServiceCalled_thenReturnException_500() throws JsonProcessingException {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTOnotValid();
        ServiceResponseErrorDTO serviceResponseErrorDTO = createServiceResponseErrorDTO(500);

        //Prepare json to be verified if it will be the same for call
        String serviceRequestDTOjson = new ObjectMapper().writeValueAsString(serviceRequestDTO);

        //JSON to be returned placed here: src\resources\stub\mappings\io\digital_citizenship_api_put_updateService_500.json

        Executable executable = () -> ioBackEndFeignRestClient.updateService(SERVICE_ID, serviceRequestDTO, "subscriptionKey");
        FeignException exception = Assertions.assertThrows(FeignException.class, executable);
        assertThat(exception.getMessage()).contains("[500 Server Error]");
        assertThat(exception.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.putRequestedFor(WireMock.urlEqualTo("/services/" + SERVICE_ID))
                        .withRequestBody(equalToJson(serviceRequestDTOjson, true, false))
        );
    }

    @AfterEach
    void resetAll() {
        wireMockServer.resetAll();
    }

    private ServiceResponseErrorDTO createServiceResponseErrorDTO(int httpStatus) {
        return ServiceResponseErrorDTO.builder()
                .type("https://example.com/problem/constraint-violation")
                .title("title")
                .status(httpStatus)
                .detail("There was an error processing the request")
                .instance("http://example.com")
                .build();
    }

    private ServiceRequestDTO createServiceRequestDTOnotValid() {
        ServiceMetadataDTO serviceMetadataDTO = createServiceMetadataDTO();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .departmentName(PRODUCT_DEPARTMENT_NAME)
                .organizationName(ORGANIZATION_NAME)
                .organizationFiscalCode(ORGANIZATION_VAT_NOT_VALID)
                .isVisible(IS_VISIBLE)
                .build();
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
                .serviceMetadata(serviceMetadataDTO)
                .build();
    }

}
