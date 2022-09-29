package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.ChannelDTO;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "rest-client.backend-io.service.request.departmentName=productDepartmentName",
                "rest-client.backend-io.service.request.isVisible=false"
        })
@WebMvcTest(value = {
        InitiativeAdditionalDTOsToIOServiceRequestDTOMapper.class})
@Slf4j
class InitiativeAdditionalDTOsToIOServiceRequestDTOMapperTest {

    @Autowired
    InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;

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

    private final ServiceRequestDTO serviceRequestDTOexpected;

    InitiativeAdditionalDTOsToIOServiceRequestDTOMapperTest() {
        serviceRequestDTOexpected = createServiceRequestDTO();;
    }

    @Test
    void toInitiativeTrxConditionsNull_equals(){
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();

        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserId(ORGANIZATION_USER_ID)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServicePayloadDTO(initiativeAdditionalDTO, initiativeOrganizationInfoDTO);
        assertEquals(serviceRequestDTOexpected, serviceRequestDTO);
    }

    private ServiceRequestDTO createServiceRequestDTO() {
        ServiceMetadataDTO serviceMetadataDTO = createServiceMetadataDTO();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .departmentName(PRODUCT_DEPARTMENT_NAME)
                .organizationName(ORGANIZATION_NAME)
                .organizationFiscalCode(ORGANIZATION_VAT)
                .isVisible(IS_VISIBLE)
                .build();
    }

    private ServiceMetadataDTO createServiceMetadataDTO() {
        return ServiceMetadataDTO.builder()
                .supportUrl(SUPPORT_URL)
                .privacyUrl(PRIVACY_URL)
                .tosUrl(TOS_URL)
                .description(DESCRIPTION)
                .scope(SCOPE)
                .build();
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId("serviceId");
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("description");
        initiativeAdditionalDTO.setPrivacyLink("privacy.url.it");;
        initiativeAdditionalDTO.setTcLink("tos.url.it");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.WEB);
        channelDTO.setContact("support.url.it");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

}
