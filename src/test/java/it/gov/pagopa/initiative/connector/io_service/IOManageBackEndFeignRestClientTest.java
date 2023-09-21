package it.gov.pagopa.initiative.connector.io_service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import feign.FeignException;
import feign.RetryableException;
import it.gov.pagopa.initiative.config.IOBackEndRestConnectorConfig;
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

    @AfterEach
    void resetAll() {
        wireMockServer.resetAll();
    }

}
