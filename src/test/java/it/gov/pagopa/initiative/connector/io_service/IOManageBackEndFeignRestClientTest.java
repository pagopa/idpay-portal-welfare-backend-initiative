package it.gov.pagopa.initiative.connector.io_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import feign.FeignException;
import feign.RetryableException;
import it.gov.pagopa.initiative.config.IOBackEndRestConnectorConfig;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.io.service.*;
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
                "rest-client.backend-io-manage.service.name=backend-io-manage",
                "rest-client.backend-io-manage.service.subscriptionKey=subscriptionKey"
        })
@SpringBootTest
@ContextConfiguration(
        initializers = IOManageBackEndFeignRestClientTest.WireMockInitializer.class,
        classes = {
                IOManageBackEndRestConnectorImpl.class,
                IOManageBackEndFeignRestClient.class,
                IOBackEndRestConnectorConfig.class,
                FeignAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class
        })
class IOManageBackEndFeignRestClientTest {
    private static final String SERVICE_ID = "SERVICE_ID";
    private static final String SERVICE_ID_500 = "SERVICE_ID_500";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    private static final String EMAIL = "test@pagopa.it";
    private static final String PHONE = "0123456789";
    private static final String SUPPORT_URL = "support.url.it";
    private static final String PRIVACY_URL = "privacy.url.it";
    private static final String TOS_URL = "tos.url.it";
    private static final String DESCRIPTION = "description";
    private static final String SCOPE = "LOCAL";
    private static final String SERVICE_NAME = "serviceName";
    private static final String PRODUCT_DEPARTMENT_NAME = "productDepartmentName";
    private static final String PRIMARY_KEY = "primaryKey";
    private static final String SECONDARY_KEY = "secondaryKey";
    private static final Integer TOPIC_ID = 0;

    private static final TopicDTO TOPIC = new TopicDTO(TOPIC_ID,"Altro");


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
                            "rest-client.backend-io-manage.service.base-url=http://%s:%d",
                            wireMockServer.getOptions().bindAddress(), wireMockServer.port()));
        }
    }

    private static WireMockServer wireMockServer;

    @Autowired
    IOManageBackEndRestConnector ioManageBackEndRestConnector;

    @Autowired
    IOManageBackEndFeignRestClient ioManageBackEndFeignRestClient;

    @Test
    void givenServiceFromInitiativePublished_whenPostCreateServiceCalled_thenReturnMatchingService_200() throws JsonProcessingException {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        //Prepare json to be verified if it will be the same for call
        String serviceRequestDTOjson = new ObjectMapper().writeValueAsString(serviceRequestDTO);
        //Prepare json to be returned
        String serviceResponseDTOjson = new ObjectMapper().writeValueAsString(serviceResponseDTOexpected);

        wireMockServer.stubFor(post(urlEqualTo("/manage/services"))
                .withRequestBody(equalToJson(serviceRequestDTOjson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(serviceResponseDTOjson)
                        .withStatus(HttpStatus.OK.value())
                )
        );

        //Connector will call the fake server and expecting to reply what we Stub previously with wireMockServer.stubFor (or also can be done with src\resources\stub\mappings)
        ResponseEntity<ServiceResponseDTO> responseEntity = ioManageBackEndFeignRestClient.createService(serviceRequestDTO, "subscriptionKey");
        ServiceResponseDTO serviceResponseDTO = responseEntity.getBody();

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        //Match json values and expect being done 1 external call
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.postRequestedFor(WireMock.urlEqualTo("/manage/services"))
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

        wireMockServer.stubFor(post(urlEqualTo("/manage/services"))
                .withRequestBody(equalToJson(serviceRequestDTOjson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(serviceResponseErrorDTOjson)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        Executable executable = () -> ioManageBackEndFeignRestClient.createService(serviceRequestDTO, "subscriptionKey");
        FeignException exception = Assertions.assertThrows(FeignException.class, executable);
        assertThat(exception.getMessage()).contains("[400 Bad Request]");
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.postRequestedFor(WireMock.urlEqualTo("/manage/services"))
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

        wireMockServer.stubFor(post(urlEqualTo("/manage/services"))
                .withRequestBody(equalToJson(serviceRequestDTOjson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(serviceResponseErrorDTOjson)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
        );

        Executable executable = () -> ioManageBackEndFeignRestClient.createService(serviceRequestDTO, "subscriptionKey");
        FeignException exception = Assertions.assertThrows(RetryableException.class, executable);
        assertThat(exception.getMessage()).contains("[500 Server Error]");
        assertThat(exception.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(5,
                WireMock.postRequestedFor(WireMock.urlEqualTo("/manage/services"))
                        .withRequestBody(equalToJson(serviceRequestDTOjson, true, false))
        );
    }

    @Test
    void givenServiceFromInitiativePublished_whenPutUpdateServiceCalled_thenReturnMatchingService_200() throws JsonProcessingException {
        ServiceRequestDTO serviceRequestDTO = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        String cta = InitiativeConstants.CtaConstant.START +
                InitiativeConstants.CtaConstant.IT + InitiativeConstants.CtaConstant.CTA_1_IT + InitiativeConstants.CtaConstant.TEXT_IT + InitiativeConstants.CtaConstant.ACTION_IT + SERVICE_ID +
                InitiativeConstants.CtaConstant.EN + InitiativeConstants.CtaConstant.CTA_1_EN + InitiativeConstants.CtaConstant.TEXT_EN + InitiativeConstants.CtaConstant.ACTION_EN + SERVICE_ID +
                InitiativeConstants.CtaConstant.END;

        serviceRequestDTO.getServiceMetadata().setCta(cta);
        serviceResponseDTOexpected.getServiceMetadata().setCta(cta);

        //Prepare json to be verified if it will be the same for call
        String serviceRequestDTOjson = new ObjectMapper().writeValueAsString(serviceRequestDTO);

        //JSON to be returned placed here: src\resources\stub\mappings\io\digital_citizenship_api_put_updateService_200.json
        //Prepare json to be returned
        String serviceResponseDTOjson = new ObjectMapper().writeValueAsString(serviceResponseDTOexpected);

        wireMockServer.stubFor(put(urlEqualTo("/manage/services/"+SERVICE_ID))
                .withRequestBody(equalToJson(serviceRequestDTOjson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(serviceResponseDTOjson)
                        .withStatus(HttpStatus.OK.value())
                )
        );

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\stub\mappings (or can be done with wireMockServer.stubFor)
        ResponseEntity<ServiceResponseDTO> responseEntity = ioManageBackEndFeignRestClient.updateService(SERVICE_ID, serviceRequestDTO, "primaryKey");
        ServiceResponseDTO serviceResponseDTO = responseEntity.getBody();

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        //Match json values and expect being done 1 external call
        assertThat(serviceResponseDTO).isEqualTo(serviceResponseDTOexpected);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.putRequestedFor(WireMock.urlEqualTo("/manage/services/" + SERVICE_ID))
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

        Executable executable = () -> ioManageBackEndFeignRestClient.updateService(SERVICE_ID_500, serviceRequestDTO, "primaryKey");
        FeignException exception = Assertions.assertThrows(RetryableException.class, executable);
        assertThat(exception.getMessage()).contains("[500 Server Error]");
        assertThat(exception.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(5,
                WireMock.putRequestedFor(WireMock.urlEqualTo("/manage/services/" + SERVICE_ID_500))
                        .withRequestBody(equalToJson(serviceRequestDTOjson, true, false))
        );
    }


    @Test
    void givenServiceFromInitiativePublished_whenDeleteServiceCalled_thenReturn_204() {

        //JSON to be returned placed here: src\resources\stub\mappings\io\digital_citizenship_api_put_updateService_200.json

        wireMockServer.stubFor(delete(urlEqualTo("/manage/services/"+SERVICE_ID))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.NO_CONTENT.value())
                )
        );

        //Connector will call the fake server and expecting to reply what we Stub on src\resources\stub\mappings (or can be done with wireMockServer.stubFor)
        ResponseEntity<Void> responseEntity = ioManageBackEndFeignRestClient.deleteService(SERVICE_ID, "subscriptionKey");
        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.deleteRequestedFor(WireMock.urlEqualTo("/manage/services/" + SERVICE_ID))
        );
    }

    @Test
    void givenServiceNotValid_FromInitiativePublished_whenDeleteServiceCalled_thenReturnException_500() {

        //JSON to be returned placed here: src\resources\stub\mappings\io\digital_citizenship_api_put_updateService_500.json

        Executable executable = () -> ioManageBackEndFeignRestClient.deleteService(SERVICE_ID_500, "primaryKey");
        FeignException exception = Assertions.assertThrows(RetryableException.class, executable);
        assertThat(exception.getMessage()).contains("[500 Server Error]");
        assertThat(exception.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(5,
                WireMock.deleteRequestedFor(WireMock.urlEqualTo("/manage/services/" + SERVICE_ID_500))
        );
    }

    @Test
    void givenServiceFromInitiativePublished_whenGetServiceToken_200() throws JsonProcessingException {
        KeysDTO responseKeysDTO = createServiceIOKeys();
        String responseKeysDTOjson = new ObjectMapper().writeValueAsString(responseKeysDTO);

        wireMockServer.stubFor(get(urlEqualTo("/manage/services/" + SERVICE_ID + "/keys"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseKeysDTOjson)
                        .withStatus(HttpStatus.OK.value())
                )
        );

        //Connector will call the fake server and expecting to reply what we Stub previously with wireMockServer.stubFor (or also can be done with src\resources\stub\mappings)
        ResponseEntity<KeysDTO> responseEntity = ioManageBackEndFeignRestClient.getServiceKeys(SERVICE_ID, "subscriptionKey");
        KeysDTO serviceResponseDTO = responseEntity.getBody();

        //Asserting if Client (FeignClient, WireMock client ecc.) responded properly
        assertNotNull(serviceResponseDTO);
        //Match json values and expect being done 1 external call
        assertThat(serviceResponseDTO).isEqualTo(responseKeysDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(1,
                WireMock.getRequestedFor(WireMock.urlEqualTo("/manage/services/" + SERVICE_ID + "/keys")));
    }

    private ServiceRequestDTO createServiceRequestDTOnotValid() {
        ServiceRequestMetadataDTO serviceMetadataDTO = createServiceRequestMetadataDTO();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .description(DESCRIPTION)
                .organization(createOrganizationDTO())
                .build();
    }

    @Test
    void givenServiceNotValid_FromInitiativePublished_whenGetServiceToken_thenReturnException_500() {

        //JSON to be returned placed here: src\resources\stub\mappings\io\digital_citizenship_api_put_updateService_500.json

        Executable executable = () -> ioManageBackEndFeignRestClient.getServiceKeys(SERVICE_ID_500, "primaryKey");
        FeignException exception = Assertions.assertThrows(RetryableException.class, executable);
        assertThat(exception.getMessage()).contains("[500 Server Error]");
        assertThat(exception.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //Verifying has been done 1 external call to IO BackEnd with our Request
        wireMockServer.verify(5,
                WireMock.getRequestedFor(WireMock.urlEqualTo("/manage/services/" + SERVICE_ID_500 + "/keys"))
        );
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

    private ServiceResponseErrorDTO createServiceResponseErrorDTO(int httpStatus) {
        return ServiceResponseErrorDTO.builder()
                .type("https://example.com/problem/constraint-violation")
                .title("title")
                .status(httpStatus)
                .detail("There was an error processing the request")
                .instance("http://example.com")
                .build();
    }

    private KeysDTO createServiceIOKeys() {
        return KeysDTO.builder()
                .primaryKey(PRIMARY_KEY)
                .secondaryKey(SECONDARY_KEY)
                .build();
    }

    @AfterEach
    void resetAll() {
        wireMockServer.resetAll();
    }

}
