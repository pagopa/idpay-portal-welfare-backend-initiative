package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.OrganizationDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestMetadataDTO;
import it.gov.pagopa.initiative.model.Channel;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@TestPropertySource(
//        locations = "classpath:application.yml",
//        properties = {
//                "rest-client.backend-io-manage.service.request.departmentName=productDepartmentName",
//        })
//@WebMvcTest(value = {
//        InitiativeAdditionalDTOsToIOServiceRequestDTOMapper.class})
class InitiativeAdditionalDTOsToIOServiceRequestDTOMapperTest {


    InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;

    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_VAT = "organizationVat";
    private static final String ORGANIZATION_USER_ROLE = "organizationUserRole";
    private static final String SUPPORT_URL = "support.url.it";
    private static final String PRIVACY_URL = "privacy.url.it";
    private static final String TOS_URL = "tos.url.it";
    private static final String DESCRIPTION = "description";
    private static final String SCOPE = "LOCAL";
    private static final String SERVICE_NAME = "serviceName";
    private static final String PRODUCT_DEPARTMENT_NAME = "productDepartmentName";
    private static final Integer TOPIC_ID =0 ;
    private ServiceRequestDTO serviceRequestDTOexpected;
    private List<String> authorizedRecipients;

    @ParameterizedTest
    @ValueSource(strings = {ORGANIZATION_NAME, ""})
    void givenInitiativeAdditionalAndOrganizationInfo_whenOrganizationNameChangeAndAuthorizedRecipientsPassed_thenServiceRequestContainDefaultProductDepartmentName(String organizationName){
        //Init constructor
        authorizedRecipients = Arrays.asList("AAAAAA00A00A000A","BBBBBB00B00B000B");
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(
                PRODUCT_DEPARTMENT_NAME,
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
        assertEquals(StringUtils.isNotBlank(organizationName)? organizationName : PRODUCT_DEPARTMENT_NAME, serviceRequestDTO.getOrganization().getDepartmentName());
        assertEquals(serviceRequestDTOexpected, serviceRequestDTO);
    }

    @ParameterizedTest
    @ValueSource(strings = {ORGANIZATION_NAME, ""})
    void givenInitiativeAdditionalAndOrganizationInfo_whenOrganizationNameChangeAndNoAuthorizedRecipients_thenServiceRequestContainDefaultProductDepartmentName(String organizationName){
        //Init constructor
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(
                PRODUCT_DEPARTMENT_NAME,
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
        assertEquals(StringUtils.isNotBlank(organizationName)? organizationName : PRODUCT_DEPARTMENT_NAME, serviceRequestDTO.getOrganization().getDepartmentName());
        assertEquals(serviceRequestDTOexpected, serviceRequestDTO);
    }

    private ServiceRequestDTO createServiceRequestDTOexpected(String organizationName, List<String> authorizedRecipients) {
        ServiceRequestMetadataDTO serviceMetadataDTO = createServiceRequestMetadataDTO();
        ServiceRequestDTO.ServiceRequestDTOBuilder serviceRequestDTOBuilder = ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .organization(createOrganizationDTOexpected(organizationName))
                .description(DESCRIPTION);
        return CollectionUtils.isEmpty(authorizedRecipients) ? serviceRequestDTOBuilder.build() : serviceRequestDTOBuilder.authorizedRecipients(authorizedRecipients).build();
    }

    private OrganizationDTO createOrganizationDTOexpected(String organizationName) {
        return  OrganizationDTO.builder()
                .departmentName(StringUtils.isNotBlank(organizationName) ? organizationName : PRODUCT_DEPARTMENT_NAME)
                .organizationName(organizationName)
                .organizationFiscalCode(ORGANIZATION_VAT)
                .build();
    }

    private ServiceRequestMetadataDTO createServiceRequestMetadataDTO() {
        return ServiceRequestMetadataDTO.builder()
                .supportUrl(SUPPORT_URL)
                .privacyUrl(PRIVACY_URL)
                .tosUrl(TOS_URL)
                .scope(SCOPE)
                .topicId(TOPIC_ID)
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

    @Test
    void givenSupportUrlOnAdditionalInfo_whenToServiceRequestDTO_thenSupportUrlOverridesWebChannel() {
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(PRODUCT_DEPARTMENT_NAME, null);
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        initiativeAdditional.setSupportUrl("https://configured.support.url");

        ServiceRequestDTO result = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, organizationInfo());

        assertEquals("https://configured.support.url", result.getServiceMetadata().getSupportUrl());
    }

    @Test
    void givenNoSupportUrlOnAdditionalInfo_whenToServiceRequestDTO_thenSupportUrlFallsBackToWebChannel() {
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(PRODUCT_DEPARTMENT_NAME, null);
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        ServiceRequestDTO result = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, organizationInfo());

        assertEquals(SUPPORT_URL, result.getServiceMetadata().getSupportUrl());
    }

    @Test
    void givenCompatibleProductsUrl_whenToServiceRequestDTO_thenWebUrlIsValued() {
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(PRODUCT_DEPARTMENT_NAME, null);
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        initiativeAdditional.setCompatibleProductsUrl("https://bonusdecoder.it/elenco");

        ServiceRequestDTO result = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, organizationInfo());

        assertEquals("https://bonusdecoder.it/elenco", result.getServiceMetadata().getWebUrl());
    }

    @Test
    void givenNoCompatibleProductsUrl_whenToServiceRequestDTO_thenWebUrlIsNull() {
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(PRODUCT_DEPARTMENT_NAME, null);
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        ServiceRequestDTO result = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, organizationInfo());

        assertNull(result.getServiceMetadata().getWebUrl());
    }

    @Test
    void givenNoNewFields_whenToServiceRequestDTO_thenDescriptionUnchanged() {
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(PRODUCT_DEPARTMENT_NAME, null);
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        ServiceRequestDTO result = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, organizationInfo());

        assertEquals(DESCRIPTION, result.getServiceMetadata() != null ? result.getDescription() : null);
        assertEquals(DESCRIPTION, result.getDescription());
    }

    @Test
    void givenLocalizedSectionsAndDates_whenToServiceRequestDTO_thenDescriptionEnrichedInItalian() {
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(PRODUCT_DEPARTMENT_NAME, null);
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        initiativeAdditional.setEligibilityInfoMap(localized("Chi IT", "Who EN"));
        initiativeAdditional.setBenefitInfoMap(localized("Offre IT", "Offers EN"));
        initiativeAdditional.setHowToRequestInfoMap(localized("Richiedi IT", "Request EN"));
        initiativeAdditional.setHowToUseInfoMap(localized("Usa IT", "Use EN"));
        initiativeAdditional.setReminderInfoMap(localized("Ricorda IT", "Remember EN"));
        initiativeAdditional.setRequestStartDate(LocalDate.of(2025, 11, 18));
        initiativeAdditional.setBonusValidityDays(15);
        initiativeAdditional.setServiceAvailabilityDate(LocalDateTime.of(2025, 11, 18, 9, 30));

        ServiceRequestDTO result = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, organizationInfo());
        String description = result.getDescription();

        assertTrue(description.startsWith(DESCRIPTION));
        assertTrue(description.contains("## Chi può richiederlo\nChi IT"));
        assertTrue(description.contains("## Cosa offre\nOffre IT"));
        assertTrue(description.contains("## Come richiederlo\nRichiedi IT"));
        assertTrue(description.contains("## Come si usa\nUsa IT"));
        assertTrue(description.contains("## Ricorda\nRicorda IT"));
        assertTrue(description.contains("Richieste aperte dal 18 novembre 2025"));
        assertTrue(description.contains("Validità del bonus: entro 15 giorni"));
        assertTrue(description.contains("Servizio disponibile dal 18 novembre 2025 alle 09:30"));
        // lingua di default: italiano -> nessun testo inglese
        assertFalse(description.contains("Who EN"));
        assertFalse(description.contains("Offers EN"));
    }

    @Test
    void givenSectionWithoutItalian_whenToServiceRequestDTO_thenSectionIsSkipped() {
        initiativeAdditionalDTOsToIOServiceRequestDTOMapper = new InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(PRODUCT_DEPARTMENT_NAME, null);
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        Map<String, String> onlyEnglish = new HashMap<>();
        onlyEnglish.put("en", "Only english");
        initiativeAdditional.setEligibilityInfoMap(onlyEnglish);

        ServiceRequestDTO result = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, organizationInfo());

        assertEquals(DESCRIPTION, result.getDescription());
        assertFalse(result.getDescription().contains("Only english"));
    }



    private InitiativeOrganizationInfoDTO organizationInfo() {
        return InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();
    }

    private Map<String, String> localized(String italian, String english) {
        Map<String, String> map = new HashMap<>();
        map.put("it", italian);
        map.put("en", english);
        return map;
    }

}

