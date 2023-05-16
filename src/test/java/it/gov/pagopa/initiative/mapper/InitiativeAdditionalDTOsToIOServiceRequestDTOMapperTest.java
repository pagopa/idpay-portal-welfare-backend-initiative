package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.model.Channel;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@TestPropertySource(
//        locations = "classpath:application.yml",
//        properties = {
//                "rest-client.backend-io.service.request.departmentName=productDepartmentName",
//                "rest-client.backend-io.service.request.isVisible=false"
//        })
//@WebMvcTest(value = {
//        InitiativeAdditionalDTOsToIOServiceRequestDTOMapper.class})
class InitiativeAdditionalDTOsToIOServiceRequestDTOMapperTest {

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

    private ServiceRequestDTO serviceRequestDTOexpected;
    private final Boolean isVisible = false;
    private List<String> authorizedRecipients;

    @ParameterizedTest
    @ValueSource(strings = {ORGANIZATION_NAME, ""})
    void givenInitiativeAdditionalAndOrganizationInfo_whenOrganizationNameChangeAndAuthorizedRecipientsPassed_thenServiceRequestContainDefaultProductDepartmentName(String organizationName){
        //Init constructor
        authorizedRecipients = Arrays.asList("AAAAAA00A00A000A","BBBBBB00B00B000B");
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(
                PRODUCT_DEPARTMENT_NAME,
                isVisible,
                authorizedRecipients
        );

        serviceRequestDTOexpected = createServiceRequestDTOexpected(organizationName, authorizedRecipients);

        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(organizationName)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO);
        assertEquals(StringUtils.isNotBlank(organizationName)? organizationName : PRODUCT_DEPARTMENT_NAME, serviceRequestDTO.getDepartmentName());
        assertEquals(serviceRequestDTOexpected, serviceRequestDTO);
    }

    @ParameterizedTest
    @ValueSource(strings = {ORGANIZATION_NAME, ""})
    void givenInitiativeAdditionalAndOrganizationInfo_whenOrganizationNameChangeAndNoAuthorizedRecipients_thenServiceRequestContainDefaultProductDepartmentName(String organizationName){
        //Init constructor
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(
                PRODUCT_DEPARTMENT_NAME,
                isVisible,
                null
        );

        serviceRequestDTOexpected = createServiceRequestDTOexpected(organizationName, null);

        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(organizationName)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO);
        assertEquals(StringUtils.isNotBlank(organizationName)? organizationName : PRODUCT_DEPARTMENT_NAME, serviceRequestDTO.getDepartmentName());
        assertEquals(serviceRequestDTOexpected, serviceRequestDTO);
    }

    private ServiceRequestDTO createServiceRequestDTOexpected(String organizationName, List<String> authorizedRecipients) {
        ServiceMetadataDTO serviceMetadataDTO = createServiceMetadataDTO();
        ServiceRequestDTO.ServiceRequestDTOBuilder serviceRequestDTOBuilder = ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .departmentName(StringUtils.isNotBlank(organizationName) ? organizationName : PRODUCT_DEPARTMENT_NAME)
                .organizationName(organizationName)
                .organizationFiscalCode(ORGANIZATION_VAT)
                .isVisible(IS_VISIBLE);
        return CollectionUtils.isEmpty(authorizedRecipients) ? serviceRequestDTOBuilder.build() : serviceRequestDTOBuilder.authorizedRecipients(authorizedRecipients).build();
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
