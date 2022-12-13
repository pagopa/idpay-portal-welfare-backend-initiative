package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.model.Channel;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import org.apache.commons.lang3.StringUtils;
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

    private String organizationNameExpected;

    @Test
    void givenInitiativeAdditionalAndOrganizationInfo_whenOrganizationIsEmpty_thenServiceRequestContainDefaultProductDepartmentName(){
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName("")
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        organizationNameExpected = "";

        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO);
        assertEquals(organizationNameExpected, serviceRequestDTO.getOrganizationName());
    }

    @Test
    void givenInitiativeAdditionalAndOrganizationInfo_whenOrganizationIsNotEmpty_thenServiceRequestContainOrganizationNamePassedByParams(){
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        organizationNameExpected = ORGANIZATION_NAME;

        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO);
        assertEquals(organizationNameExpected, serviceRequestDTO.getOrganizationName());
    }

    private ServiceRequestDTO createServiceRequestDTO(String organizationName) {
        ServiceMetadataDTO serviceMetadataDTO = createServiceMetadataDTO();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .departmentName(StringUtils.isNotBlank(organizationName)? organizationName : PRODUCT_DEPARTMENT_NAME)
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

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceIO(true);
        initiativeAdditional.setServiceId("serviceId");
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        initiativeAdditional.setDescription("description");
        initiativeAdditional.setPrivacyLink("privacy.url.it");
        initiativeAdditional.setTcLink("tos.url.it");
        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.WEB);
        channel.setContact("support.url.it");
        List<Channel> channelList = new ArrayList<>();
        channelList.add(channel);
        initiativeAdditional.setChannels(channelList);
        return initiativeAdditional;
    }

}
