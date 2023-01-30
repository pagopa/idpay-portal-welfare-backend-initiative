package it.gov.pagopa.initiative.service;


import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import it.gov.pagopa.initiative.connector.decrypt.DecryptRestConnector;
import it.gov.pagopa.initiative.connector.encrypt.EncryptRestConnector;
import it.gov.pagopa.initiative.connector.file_storage.FileStorageConnector;
import it.gov.pagopa.initiative.connector.group.GroupRestConnector;
import it.gov.pagopa.initiative.connector.io_service.IOBackEndRestConnector;
import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestConnector;
import it.gov.pagopa.initiative.connector.ranking.RankingRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.NotFound;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Status;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseErrorDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.event.InitiativeProducer;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeAdditionalDTOsToIOServiceRequestDTOMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.trx.*;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import it.gov.pagopa.initiative.utils.InitiativeUtils;
import it.gov.pagopa.initiative.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.ADMIN;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.OPE_BASE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.initiative.conditions.notifyEmail=true"
        })
@WebMvcTest(value = {
        InitiativeService.class})
class InitiativeServiceTest {

    private static final String ANY_NOT_INITIATIVE_STATE = "ANY_NOT_INITIATIVE_STATE";
    public static final String INITIATIVE_NAME = "initiativeName1";
    public static final String ORGANIZATION_ID = "organizationId1";
    public static final String INITIATIVE_ID = "initiativeId";
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
    public static final String ANY_KEY_TOKEN_IO = "ANY_KEY_TOKEN_IO";
    private static final String ROLE = "ROLE";
    private static final String CF = "CF";
    private static final String USER_ID = "USER_ID";
    private static final String STATUS = "ONBOARDING_OK";
    private static final LocalDateTime STARTDATE = LocalDateTime.now();
    private static final LocalDateTime ENDDATE = LocalDateTime.now();
    private static final String LOGO_EXTENSION = "png";
    private static final String LOGO_MIME_TYPE = "image/png";
    private static final String FILE_NAME = "logo.png";
    public static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    public static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";

    @Autowired
    InitiativeService initiativeService;

    @MockBean
    InitiativeRepository initiativeRepository;

    @MockBean
    InitiativeProducer initiativeProducer;

    @MockBean
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @MockBean
    Utilities utilities;

    @MockBean
    InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;

    @MockBean
    IOBackEndRestConnector ioBackEndRestConnector;

    @MockBean
    GroupRestConnector groupRestConnector;

    @MockBean
    FileStorageConnector fileStorageConnector;

    @MockBean
    OnboardingRestConnector onboardingRestConnector;

    @MockBean
    EncryptRestConnector encryptRestConnector;

    @MockBean
    DecryptRestConnector decryptRestConnector;

    @MockBean
    RankingRestConnector rankingRestConnector;

    @MockBean
    EmailNotificationService emailNotificationService;

    @MockBean
    AESTokenService ioTokenService;

    @MockBean
    InitiativeValidationService initiativeValidationService;

    @MockBean
    InitiativeUtils initiativeUtils;

    @Test
    void givenRoleAdmin_retrieveInitiativeSummary_ok() {
        Initiative step2Initiative1 = createStep2Initiative();
        Initiative step2Initiative2 = createStep2Initiative();
        List<Initiative> initiativeList = Arrays.asList(step2Initiative1, step2Initiative2);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(ORGANIZATION_ID, true)).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, ADMIN);

        //Check the equality of the results
        assertEquals(initiativeList, initiatives);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(ORGANIZATION_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void givenRoleOpeBase_statusInRevision_retrieveInitiativeSummary_ok() {
        Initiative step2Initiative1 = createStep2Initiative();
        step2Initiative1.setStatus(InitiativeConstants.Status.IN_REVISION);
        List<Initiative> initiativeList = List.of(step2Initiative1);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(ORGANIZATION_ID, true)).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, OPE_BASE);

        //Check the equality of the results
        assertEquals(initiativeList, initiatives);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(ORGANIZATION_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void givenRoleOpeBase_statusApproved_retrieveInitiativeSummary_ok() {
        Initiative step2Initiative1 = createStep2Initiative();
        step2Initiative1.setStatus(InitiativeConstants.Status.APPROVED);
        List<Initiative> initiativeList = List.of(step2Initiative1);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(ORGANIZATION_ID, true)).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, OPE_BASE);

        //Check the equality of the results
        assertEquals(initiativeList, initiatives);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(ORGANIZATION_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void givenRoleOpeBase_statusToCheck_retrieveInitiativeSummary_ok() {
        Initiative step2Initiative1 = createStep2Initiative();
        step2Initiative1.setStatus(InitiativeConstants.Status.TO_CHECK);
        List<Initiative> initiativeList = List.of(step2Initiative1);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(ORGANIZATION_ID, true)).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, OPE_BASE);

        //Check the equality of the results
        assertEquals(initiativeList, initiatives);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(ORGANIZATION_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }


    @Test
    void retrieveInitiativeSummary_ko() {
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, ADMIN);
        } catch (InitiativeException e) {
            System.out.println("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void testGetInitiativesIssuerList() {
        Initiative step2Initiative1 = createStep2Initiative();
        step2Initiative1.setStatus(Status.PUBLISHED);
        List<Initiative> initiativeList = List.of(step2Initiative1);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByEnabledAndStatus(true, "PUBLISHED")).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.getInitiativesIssuerList();

        //Check the equality of the results
        assertEquals(initiativeList, initiatives);
        verify(initiativeRepository).findByEnabledAndStatus(any(), any());
    }

    @Test
    void insertInitiative_ok() {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.insert(any(Initiative.class))).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.insertInitiative(step2Initiative, ORGANIZATION_ID, ORGANIZATION_NAME, ROLE);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).insert(any(Initiative.class));
    }

    @Test
    void insertInitiative_setStatus() {
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setStatus(null);

        initiativeService.insertInitiative(step2Initiative, ORGANIZATION_ID, ORGANIZATION_NAME, ROLE);
        assertEquals(InitiativeConstants.Status.DRAFT, step2Initiative.getStatus());
    }

    @Test
    void getInitiativeIdFromServiceId_thenValidationIsPassed() {
        Initiative step1Initiative = createStep1Initiative();
        when(initiativeRepository.retrieveServiceId(SERVICE_ID)).thenReturn(Optional.ofNullable(step1Initiative));
        Initiative initiative = initiativeService.getInitiativeIdFromServiceId(SERVICE_ID);
        assertEquals(step1Initiative, initiative);
        verify(initiativeRepository, times(1)).retrieveServiceId(SERVICE_ID);
    }

    @Test
    void getInitiativeIdFromServiceId_throwInitiativeException_thenValidationFailed() {
        when(initiativeRepository.retrieveServiceId(SERVICE_ID)).thenReturn(Optional.empty());
        Mockito.doThrow(new InitiativeException(NotFound.CODE,
                String.format(NotFound.INITIATIVE_ID_BY_SERVICE_ID_MESSAGE, SERVICE_ID),HttpStatus.NOT_FOUND)).when(initiativeRepository).retrieveServiceId("");
        try {
            initiativeService.getInitiativeIdFromServiceId(SERVICE_ID);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_ID_BY_SERVICE_ID_MESSAGE, SERVICE_ID), e.getMessage());
        }
    }

    @Test
    void getPrimaryAndSecondaryToken_thenValidationIsPassed() {
        Initiative step1Initiative = createStep1Initiative();
        when(initiativeRepository.findByInitiativeIdAndEnabled(INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step1Initiative));
        InitiativeAdditional additional = initiativeService.getPrimaryAndSecondaryTokenIO(INITIATIVE_ID);
        assert step1Initiative != null;
        assertEquals(step1Initiative.getAdditionalInfo(), additional);
        verify(initiativeRepository, times(1)).findByInitiativeIdAndEnabled(INITIATIVE_ID, true);
    }

    @Test
    void getPrimaryAndSecondaryToken_throwInitiativeException_thenValidationFailed() {
        when(initiativeRepository.findByInitiativeIdAndEnabled(INITIATIVE_ID, true)).thenReturn(Optional.empty());
        Mockito.doThrow(new InitiativeException(NotFound.CODE,
                String.format(NotFound.INITIATIVE_ID_BY_SERVICE_ID_MESSAGE, SERVICE_ID),HttpStatus.NOT_FOUND))
                .when(initiativeRepository).findByInitiativeIdAndEnabled("", true);
        try {
            initiativeService.getPrimaryAndSecondaryTokenIO(INITIATIVE_ID);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.PRIMARY_AND_SECONDARY_TOKEN_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void getInitiative_ok() {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE); // same as: verify(initiativeValidationService, times(1))
    }

    @Test
    void getInitiative_ko() {
        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void getInitiative_roleBase_statusInRevision_ok() {
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE); // same as: verify(initiativeValidationService, times(1))
    }

    @Test
    void getInitiative_roleBase_statusApproved_ok() {
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setStatus(InitiativeConstants.Status.APPROVED);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE); // same as: verify(initiativeValidationService, times(1))
    }

    @Test
    void getInitiative_roleBase_statusToCheck_ok() {
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE); // same as: verify(initiativeValidationService, times(1))
    }

    @Test
    void getInitiative_roleBase_statusNotValid_thenThrowException_ok() {
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setStatus(InitiativeConstants.Status.PUBLISHED);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE); // same as: verify(initiativeValidationService, times(1))
    }

    @Test
    void getInitiativeBeneficiaryView_ok() {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeBeneficiaryView(INITIATIVE_ID, true)).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiativeBeneficiaryView(INITIATIVE_ID);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeBeneficiaryView(INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void getInitiativeBeneficiaryView_ko() {
        when(initiativeRepository.retrieveInitiativeBeneficiaryView(INITIATIVE_ID, true)).thenReturn(Optional.empty());
        try {
            initiativeService.getInitiativeBeneficiaryView(INITIATIVE_ID);
        } catch (InitiativeException e) {
            System.out.println("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeGeneralInfo_ok() {
        Initiative step2Initiative = createStep2Initiative();
        InitiativeGeneral initiativeGeneral = createInitiativeGeneral(true);
        step2Initiative.setGeneral(initiativeGeneral);
        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);
        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative, ROLE);
        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void updateInitiativeGeneralInfo_FakeInPUBLISHED_thenUnprocessableState() {
        Initiative step2Initiative = createStep2Initiative();
        InitiativeGeneral initiativeGeneral = createInitiativeGeneral(true);
        step2Initiative.setGeneral(initiativeGeneral);
        step2Initiative.setStatus(InitiativeConstants.Status.PUBLISHED);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeGeneralInfo_thenThrowInitiativeException() {
        Initiative fullInitiative = createStep2Initiative();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, fullInitiative, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeRefundRules_languageException() {
        Initiative fullInitiative = createFullInitiative();
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ENGLISH.getLanguage(), "en");

        InitiativeGeneral initiativeGeneral = createInitiativeGeneral(true);
        initiativeGeneral.setDescriptionMap(language);
        fullInitiative.setGeneral(initiativeGeneral);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(fullInitiative);

        try {
            initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, fullInitiative, true);
        } catch (InitiativeException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeRefundRules_inRevision() {
        Initiative fullInitiative = createFullInitiative();
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = createInitiativeGeneral(false);
        initiativeGeneral.setDescriptionMap(language);
        fullInitiative.setGeneral(initiativeGeneral);
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(fullInitiative);
        initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, fullInitiative, true);
        assertEquals(Status.IN_REVISION, fullInitiative.getStatus());

    }

    @Test
    void updateInitiativeRefundRules_inRevision_beneficiaryKnown() {
        Initiative fullInitiative = createFullInitiative();
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = createInitiativeGeneral(true);
        initiativeGeneral.setDescriptionMap(language);
        fullInitiative.setGeneral(initiativeGeneral);
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(fullInitiative);
        initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, fullInitiative, true);
        assertEquals(Status.IN_REVISION, fullInitiative.getStatus());

    }

    @Test
    void storeInitiativeLogo_ok() throws Exception {
        InputStream logo = new ByteArrayInputStream("logo.png".getBytes());
        Initiative initiative = this.createFullInitiative();
        Set <String> logoExtension = new HashSet<>();
        Set <String> logoMimeTypes = new HashSet<>();
        logoExtension.add(LOGO_EXTENSION);
        logoMimeTypes.add(LOGO_MIME_TYPE);
        InitiativeGeneral general = createInitiativeGeneral(true);
        initiative.setGeneral(general);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doNothing().when(fileStorageConnector).uploadInitiativeLogo(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoExtensions()).thenReturn(logoExtension);
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoMimeTypes()).thenReturn(logoMimeTypes);
        LogoDTO logoDTO = initiativeService.storeInitiativeLogo(ORGANIZATION_ID, INITIATIVE_ID, logo, LOGO_MIME_TYPE,
                FILE_NAME);

        assertEquals(FILE_NAME,logoDTO.getLogoFileName());
    }

    @Test
    void storeInitiativeLogo_koExtension() throws Exception {
        InputStream logo = new ByteArrayInputStream("logo.png".getBytes());
        Initiative initiative = this.createFullInitiative();
        Set <String> logoExtension = new HashSet<>();
        Set <String> logoMimeTypes = new HashSet<>();
        logoExtension.add(LOGO_EXTENSION);
        logoMimeTypes.add(LOGO_MIME_TYPE);
        InitiativeGeneral general = createInitiativeGeneral(true);
        initiative.setGeneral(general);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doNothing().when(fileStorageConnector).uploadInitiativeLogo(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoExtensions()).thenReturn(logoExtension);
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoMimeTypes()).thenReturn(logoMimeTypes);
        try {
            LogoDTO logoDTO = initiativeService.storeInitiativeLogo(ORGANIZATION_ID, INITIATIVE_ID, logo, LOGO_MIME_TYPE,
                    "logo.jpg");
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Invalid file extension"));
        }
    }

    @Test
    void storeInitiativeLogo_koMimeType() throws Exception {
        InputStream logo = new ByteArrayInputStream("logo.png".getBytes());
        Initiative initiative = this.createFullInitiative();
        Set <String> logoExtension = new HashSet<>();
        Set <String> logoMimeTypes = new HashSet<>();
        logoExtension.add(LOGO_EXTENSION);
        logoMimeTypes.add(LOGO_MIME_TYPE);
        InitiativeGeneral general = createInitiativeGeneral(true);
        initiative.setGeneral(general);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doNothing().when(fileStorageConnector).uploadInitiativeLogo(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoExtensions()).thenReturn(logoExtension);
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoMimeTypes()).thenReturn(logoMimeTypes);
        try {
            LogoDTO logoDTO = initiativeService.storeInitiativeLogo(ORGANIZATION_ID, INITIATIVE_ID, logo, "image/jpg",
                    FILE_NAME);
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("allowed only"));
        }
    }

    @Test
    void updateInitiativeAdditionalInfo_ok() {
        Initiative step2Initiative = createStep1Initiative();
        Initiative initiativeSavedExpected = createStep1Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.save(any(Initiative.class))).thenReturn(initiativeSavedExpected);
        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeAdditionalInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative, ROLE);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        //Expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).save(any(Initiative.class));
    }

    @Test
    void updateInitiativeAdditionalInfo_thenThrowInitiativeException() {
        Initiative fullInitiative = createStep1Initiative();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, fullInitiative, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeBeneficiary_ok() {
        Initiative step2Initiative = createStep2Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        //Instruct the initiativeValidationService Mock to do nothing for checkAutomatedCriteriaOrderDirectionWithRanking
        doNothing().when(initiativeValidationService).checkAutomatedCriteriaOrderDirectionWithRanking(step2Initiative,automatedCriteriaList);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule, ROLE);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void updateInitiativeBeneficiary_thenThrowInitiativeException() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeRewardAndTrxRules_ok() {
        Initiative step3Initiative = createStep3Initiative();

        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step3Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void updateInitiativeRewardAndTrxRules_thenThrowInitiativeException() {
        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateRefundRules_ok() {
        Initiative initiative = createInitiativeOnlyRefundRule();
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, false);
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void updateRefundRules_thenThrowInitiativeException() {
        Initiative initiative = createInitiativeOnlyRefundRule();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, false);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    /*@Test
    void updateInitiativeRefundRules_emailException() {
        Initiative initiative = createInitiativeOnlyRefundRule();

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);

        Mockito.doNothing().when(emailNotificationService).sendInitiativeToCurrentOrganization(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        try {
            initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, true);
        } catch (FeignException e) {
            Assertions.fail();
        }
    }*/

    @Test
    void updateRefundRule_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException() {
        Initiative initiative = Initiative.builder().initiativeId(INITIATIVE_ID).status(InitiativeConstants.Status.PUBLISHED).build();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, true));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void givenStatusIN_REVISION_updateInitiativeApprovedStatus_thenStatusIsChangedWithSuccess() {
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void givenStatusIN_REVISION_updateInitiativeApprovedStatus_thenThrowInitiativeException() {
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void givenStatusTO_CHECK_updateInitiativeApprovedStatus_whenStatusNotValid_thenThrowInitiativeException() {
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeApprovedStatus_emailException() {
        Initiative step4Initiative = createStep4Initiative();
        step4Initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step4Initiative);

        Request request =
                Request.create(Request.HttpMethod.PUT, "url", new HashMap<>(), null, new RequestTemplate());
        Mockito.doThrow(new FeignException.BadRequest("", request, new byte[0], null))
                .when(emailNotificationService).sendInitiativeToCurrentOrganization(Mockito.any(), Mockito.anyString(),
                        Mockito.anyString());
        try {
            initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        } catch (FeignException e) {
            Assertions.fail();}
    }

    @Test
    void logicallyDeleteInitiative_thenDeletedIsSettedToTrueWithSuccess() {
        Initiative initiative = createStep5Initiative();
        initiative.setEnabled(true);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void logicallyDeleteInitiative_thenThrowNewInitiativeException() {
        Initiative initiative = createStep5Initiative();
        initiative.setEnabled(true);

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void logicallyDeleteInitiative_thenThrowNewInitiativeException2() {
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.PUBLISHED);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);

        try {
            initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        }
        catch (InitiativeException e){
            assertEquals(InitiativeConstants.Exception.BadRequest.CODE, e.getCode());
        }
    }

    @Test
    void logicallyDeleteInitiative_emailException() {
        Initiative initiative = createStep5Initiative();
        initiative.setEnabled(true);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);
        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        Request request =
                Request.create(Request.HttpMethod.DELETE, "url", new HashMap<>(), null, new RequestTemplate());
        Mockito.doThrow(new FeignException.BadRequest("", request, new byte[0], null))
                .when(emailNotificationService).sendInitiativeToPagoPA(Mockito.any(), Mockito.anyString(),
                        Mockito.anyString());
        try {
            initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        } catch (FeignException e) {
            Assertions.fail();}
    }

    @Test
    void sendInitiativeInfoToNotificationManager() {
        Initiative initiative = createStep5Initiative();
        initiativeService.sendInitiativeInfoToNotificationManager(initiative);
        verify(groupRestConnector).notifyInitiativeToGroup(initiative);
    }

    @Test
    void updateInitiativeStatusToCheck_thenStatusIsUpdatedWithSuccess() {
        Initiative step4Initiative = createStep4Initiative();
        step4Initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step4Initiative);
        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void updateInitiativeStatusToCheck_thenThrowInitiativeExceptionStatusIsNotInRevision() {
        Initiative step4Initiative = createStep4Initiative();
        step4Initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                HttpStatus.NOT_FOUND))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeStatusToCheck_emailException() {
        Initiative step4Initiative = createStep4Initiative();
        step4Initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step4Initiative);

        Request request =
                Request.create(Request.HttpMethod.PUT, "url", new HashMap<>(), null, new RequestTemplate());
        Mockito.doThrow(new FeignException.BadRequest("", request, new byte[0], null))
                .when(emailNotificationService).sendInitiativeToCurrentOrganization(Mockito.any(), Mockito.anyString(),
                        Mockito.anyString());
        try {
            initiativeService.updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        } catch (FeignException e) {
            Assertions.fail();}
    }

    @Test
    void updateInitiativeStatusToPUBLISHED_thenInitiativeIsUpdated() {
        Initiative initiativeExpected = createStep5Initiative();
        initiativeExpected.setStatus(InitiativeConstants.Status.PUBLISHED);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.save(any(Initiative.class))).thenReturn(initiativeExpected);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiative(initiativeExpected);

        //Expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).save(any(Initiative.class));
    }


    @Test
    void givenInitiativeAPPROVEDandNextStatusPUBLISHED_whenInitiativeIsAllowedToBeNextStatus_thenOk() {
        //Instruct Initiative to have a status Valid (APPROVED)
        Initiative initiative = createStep5Initiative();
        initiative.setStatus(InitiativeConstants.Status.APPROVED);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);
        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void givenOneOfInitiativeStatusNotAPPROVEDandNextStatusPUBLISHED_whenInitiativeIsNOTAllowedToBeNextStatus_thenThrowInitiativeException() {
        //Instruct Initiative to have a status Not Valid
        Initiative initiative = createStep5Initiative();
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);

        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID.formatted(initiative.getInitiativeId()), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void givenAnyInitiative_whenNextStatusIsNotSetOfInitiativeStatus_thenThrowInitiativeException() {
        //Instruct Initiative to have a status Not Valid
        Initiative initiative = createStep5Initiative();

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, ANY_NOT_INITIATIVE_STATE, ROLE);

        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID.formatted(initiative.getInitiativeId()), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void givenInitiativeAPPROVEDandNextStatusPUBLISHED_throwsException() {
        Initiative initiative = createStep5Initiative();

        try {
            initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, STATUS,
                    InitiativeConstants.Role.OPE_BASE);
        } catch (InitiativeException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.CODE, e.getCode());
        }
    }

    @Test
    void givenInitiativeDTO_whenRuleEngineProduceIsValid_thenOk() {
        //Instruct Initiative
        Initiative initiative = createStep5Initiative();

        when(initiativeProducer.sendPublishInitiative(initiative)).thenReturn(true);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.sendInitiativeInfoToRuleEngine(initiative);

        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void givenInitiativeDTO_whenRuleEngineProduceIsNotValid_thenThrowException() {
        //Instruct Initiative
        Initiative initiative = createStep5Initiative();

        when(initiativeProducer.sendPublishInitiative(initiative)).thenReturn(false);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.sendInitiativeInfoToRuleEngine(initiative);

        Assertions.assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void givenDTOsInitiativeAndInitiativeOrganizationInfo_whenIntegrationWithIOBackEndIsOK_thenReturnInitiativeUpdated() {
        //Instruct Initiative
        Initiative initiative = createStep5Initiative();

        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();

        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        ServiceRequestDTO serviceRequestDTOexpected = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        when(initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO)).thenReturn(serviceRequestDTOexpected);
        when(ioBackEndRestConnector.createService(serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);
        when(ioTokenService.encrypt(anyString())).thenReturn(ANY_KEY_TOKEN_IO);

        Initiative initiativeActual = initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
        assertEquals(SERVICE_ID, initiativeActual.getAdditionalInfo().getServiceId());
        assertEquals(ANY_KEY_TOKEN_IO, initiativeActual.getAdditionalInfo().getPrimaryTokenIO());

        //Expecting connector to be called once with correct param
        verify(ioBackEndRestConnector, times(1)).createService(serviceRequestDTOexpected);
    }

    @Test
    void sendInitiativeInfoToIOBackEndServiceAndUpdateInitiativeWithLogo() {
        Initiative initiative = createStep5Initiative();
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        initiativeAdditional.setLogoFileName("logo file name");
        initiative.setAdditionalInfo(initiativeAdditional);
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        ServiceRequestDTO serviceRequestDTOexpected = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        when(initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO)).thenReturn(serviceRequestDTOexpected);
        when(ioBackEndRestConnector.createService(serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);
        when(ioTokenService.encrypt(anyString())).thenReturn(ANY_KEY_TOKEN_IO);
        Mockito.doNothing().when(ioBackEndRestConnector).sendLogoIo(anyString(),anyString(),any());

        Initiative initiativeActual = initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
        assertEquals(SERVICE_ID, initiativeActual.getAdditionalInfo().getServiceId());
        assertEquals(ANY_KEY_TOKEN_IO, initiativeActual.getAdditionalInfo().getPrimaryTokenIO());

        //Expecting connector to be called once with correct param
        verify(ioBackEndRestConnector, times(1)).createService(serviceRequestDTOexpected);
    }

    @Test
    void sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative_feignException() {
        Initiative initiative = createStep5Initiative();
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();
        ServiceRequestDTO serviceRequestDTOexpected = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();
        Request request =
                Request.create(Request.HttpMethod.PUT, "url", new HashMap<>(), null, new RequestTemplate());
        when(initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO)).thenReturn(serviceRequestDTOexpected);
        when(ioBackEndRestConnector.createService(serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);
        when(ioTokenService.encrypt(anyString())).thenReturn(ANY_KEY_TOKEN_IO);
        Mockito.doThrow(new FeignException.BadRequest("", request, new byte[0], null))
                .when(emailNotificationService).sendInitiativeToCurrentOrganization(Mockito.any(), Mockito.anyString(),
                        Mockito.anyString());
        Mockito.doThrow(new FeignException.BadRequest("", request, new byte[0], null))
                .when(emailNotificationService).sendInitiativeToPagoPA(Mockito.any(), Mockito.anyString(),
                        Mockito.anyString());
        try {
            initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
        } catch (FeignException e) {
            Assertions.fail();}
    }

    @Test
    void getRankingList_ok() {
        Initiative initiative = this.createFullInitiative();
        RankingRequestDTO rankingRequestDTO = new RankingRequestDTO(USER_ID,INITIATIVE_ID,ORGANIZATION_ID,LocalDateTime.now(),LocalDateTime.now(),1,1,"test");
        RankingPageDTO rankingPageDTO =new RankingPageDTO();
        rankingPageDTO.setContent(List.of(rankingRequestDTO));
        DecryptCfDTO decryptCfDTO = new DecryptCfDTO(CF);
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(rankingRestConnector.getRankingList(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(rankingPageDTO);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.when(decryptRestConnector.getPiiByToken(USER_ID)).thenReturn(decryptCfDTO);
        BeneficiaryRankingPageDTO beneficiaryRankingDTO = initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, encryptedCfDTO.getToken(),
                Status.PUBLISHED);
        assertEquals(CF,beneficiaryRankingDTO.getContent().get(0).getBeneficiary());

    }
    @Test
    void getRankingList_initiative_not_found() {
        try {
            BeneficiaryRankingPageDTO beneficiaryRankingDTO = initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (InitiativeException e) {
            assertEquals(NotFound.CODE,e.getCode());
        }
    }
    @Test
    void getRankingList_ko_encrypt() {
        Initiative initiative = this.createFullInitiative();
        OnboardingStatusCitizenDTO onboardingStatusCitizenDTO = new OnboardingStatusCitizenDTO(USER_ID, STATUS, LocalDateTime.now().toString());
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new InitiativeException(InternalServerError.CODE, "", HttpStatus.INTERNAL_SERVER_ERROR)).when(encryptRestConnector).upsertToken(Mockito.any());
        try {
            BeneficiaryRankingPageDTO beneficiaryRankingDTO = initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
    }

    @Test
    void getRankingList_ko_decrypt() {
        Initiative initiative = this.createFullInitiative();
        RankingRequestDTO rankingRequestDTO = new RankingRequestDTO(USER_ID,INITIATIVE_ID,ORGANIZATION_ID,LocalDateTime.now(),LocalDateTime.now(),1,1,"test");
        RankingPageDTO rankingPageDTO =new RankingPageDTO();
        rankingPageDTO.setContent(List.of(rankingRequestDTO));
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(rankingRestConnector.getRankingList(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(rankingPageDTO);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new InitiativeException(InternalServerError.CODE, "", HttpStatus.INTERNAL_SERVER_ERROR)).when(decryptRestConnector).getPiiByToken(Mockito.anyString());
        try {
            BeneficiaryRankingPageDTO beneficiaryRankingDTO = initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
    }

    @Test
    void getRankingList_ko_ranking() {
        Initiative initiative = this.createFullInitiative();
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new InitiativeException(InternalServerError.CODE, "", HttpStatus.INTERNAL_SERVER_ERROR)).when(rankingRestConnector).getRankingList(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.anyString(),Mockito.anyString());
        try {
            BeneficiaryRankingPageDTO beneficiaryRankingDTO = initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
    }

    @Test
    void getRankingList_ko_ranking_disabled() {
        Initiative initiative = this.createFullInitiative();
        initiative.setGeneral(new InitiativeGeneral());
        initiative.getGeneral().setRankingEnabled(false);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        try {
            BeneficiaryRankingPageDTO beneficiaryRankingDTO = initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
    }

    @Test
    void getOnboardingStatusList_ok() {
        Initiative initiative = this.createFullInitiative();
        OnboardingStatusCitizenDTO onboardingStatusCitizenDTO = new OnboardingStatusCitizenDTO(USER_ID, STATUS, LocalDateTime.now().toString());
        List<OnboardingStatusCitizenDTO> onboardingStatusCitizenDTOS = new ArrayList<>();
        onboardingStatusCitizenDTOS.add(onboardingStatusCitizenDTO);
        ResponseOnboardingDTO onboardingDTO = new ResponseOnboardingDTO(onboardingStatusCitizenDTOS, 1, 1, 1, 1);
        DecryptCfDTO decryptCfDTO = new DecryptCfDTO(CF);
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.when(onboardingRestConnector.getOnboarding(INITIATIVE_ID, null, USER_ID, STARTDATE, ENDDATE, STATUS)).thenReturn(onboardingDTO);
        Mockito.when(decryptRestConnector.getPiiByToken(USER_ID)).thenReturn(decryptCfDTO);
        OnboardingDTO onboardingDTO1 = initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS, null);
        assertEquals(CF,onboardingDTO1.getContent().get(0).getBeneficiary());
        assertEquals(STATUS,onboardingDTO1.getContent().get(0).getBeneficiaryState());

    }

    @Test
    void getOnboardingStatusList_ko_encrypt() {
        Initiative initiative = this.createFullInitiative();
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new InitiativeException(InternalServerError.CODE, "", HttpStatus.INTERNAL_SERVER_ERROR)).when(encryptRestConnector).upsertToken(Mockito.any());
        try {
            initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS,
                    Pageable.ofSize(21));
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
    }

    @Test
    void getOnboardingStatusList_ko_decrypt() {
        Initiative initiative = this.createFullInitiative();
        OnboardingStatusCitizenDTO onboardingStatusCitizenDTO = new OnboardingStatusCitizenDTO(USER_ID, STATUS, LocalDateTime.now().toString());
        List<OnboardingStatusCitizenDTO> onboardingStatusCitizenDTOS = new ArrayList<>();
        onboardingStatusCitizenDTOS.add(onboardingStatusCitizenDTO);
        ResponseOnboardingDTO onboardingDTO = new ResponseOnboardingDTO(onboardingStatusCitizenDTOS, 1, 1, 1, 1);
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.when(onboardingRestConnector.getOnboarding(INITIATIVE_ID, Pageable.ofSize(21), USER_ID, STARTDATE, ENDDATE, STATUS)).thenReturn(onboardingDTO);
        Mockito.doThrow(new InitiativeException(InternalServerError.CODE, "", HttpStatus.INTERNAL_SERVER_ERROR)).when(decryptRestConnector).getPiiByToken(Mockito.anyString());
        try {
            initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS,
                    Pageable.ofSize(21));
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
    }

    @Test
    void getOnboardingStatusList_ko_onboarding() {
        Initiative initiative = this.createFullInitiative();
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new InitiativeException(InternalServerError.CODE, "", HttpStatus.INTERNAL_SERVER_ERROR)).when(onboardingRestConnector).getOnboarding(INITIATIVE_ID, null, USER_ID, STARTDATE, ENDDATE, STATUS);
        try {
            OnboardingDTO onboardingDTO1 = initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS, null);
            Assertions.fail();
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
    }

    @Test
    void getOnboardingStatusList_initiative_internal_server_error() {
        try {
            OnboardingDTO onboardingDTO1 = initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS, null);
            Assertions.fail();
        } catch (InitiativeException e) {
            assertEquals(InternalServerError.CODE,e.getCode());
        }
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
                .primaryKey(ANY_KEY_TOKEN_IO)
                .build();
    }

    Initiative createFullInitiative() {
        return createStep5Initiative();
    }

    InitiativeDTO createFullInitiativeDTO() {
        return createStep5InitiativeDTO();
    }

    /*
     * Step 1
     */

    Initiative createStep1Initiative() {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setInitiativeName(INITIATIVE_NAME);
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setStatus("DRAFT");
        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(boolean beneficiaryKnown) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneral.setBeneficiaryKnown(beneficiaryKnown);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        initiativeGeneral.setBudget(new BigDecimal(1000000000));
        initiativeGeneral.setEndDate(LocalDate.of(2022, 9, 8));
        initiativeGeneral.setStartDate(LocalDate.of(2022, 8, 8));
        initiativeGeneral.setRankingStartDate(LocalDate.of(2022, 9, 18));
        initiativeGeneral.setRankingEndDate(LocalDate.of(2022, 8, 18));
        initiativeGeneral.setDescriptionMap(language);
        return initiativeGeneral;
    }
    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceIO(true);
        initiativeAdditional.setServiceId(SERVICE_ID);
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        initiativeAdditional.setDescription("Description");
        initiativeAdditional.setPrivacyLink("privacyLink");
        initiativeAdditional.setTcLink("tcLink");
        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.EMAIL);
        channel.setContact("contact");
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        initiativeAdditional.setChannels(channels);
        return initiativeAdditional;
    }

    InitiativeDTO createStep1InitiativeDTO() {
        InitiativeDTO initiativeDTO = InitiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName(INITIATIVE_NAME)
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .additionalInfo(createInitiativeAdditionalDTO()).build();
        return initiativeDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(boolean beneficiaryKnown) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(beneficiaryKnown);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId(SERVICE_ID);
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("description");
        initiativeAdditionalDTO.setPrivacyLink("privacy.url.it");
        initiativeAdditionalDTO.setTcLink("tos.url.it");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.WEB);
        channelDTO.setContact("support.url.it");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

    /*
     * Step 2
     */

    Initiative createStep2Initiative() {
        Initiative initiative = createStep1Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiative.setBeneficiaryRule(initiativeBeneficiaryRule);
        return initiative;
    }

    private InitiativeBeneficiaryRule createInitiativeBeneficiaryRule() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        SelfCriteriaBool selfCriteriaBool = new SelfCriteriaBool();
        selfCriteriaBool.set_type(TypeBoolEnum.BOOLEAN);
        selfCriteriaBool.setCode("B001");
        selfCriteriaBool.setDescription("Desc_bool");
        selfCriteriaBool.setValue(true);
        SelfCriteriaMulti selfCriteriaMulti = new SelfCriteriaMulti();
        selfCriteriaMulti.set_type(TypeMultiEnum.MULTI);
        selfCriteriaMulti.setCode("B001");
        selfCriteriaMulti.setDescription("Desc_Multi");
        List<String> values = new ArrayList<>();
        values.add("valore1");
        values.add("valore2");
        selfCriteriaMulti.setValue(values);
        List<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        iSelfDeclarationCriteriaList.add(selfCriteriaBool);
        iSelfDeclarationCriteriaList.add(selfCriteriaMulti);
        initiativeBeneficiaryRule.setSelfDeclarationCriteria(iSelfDeclarationCriteriaList);
        AutomatedCriteria automatedCriteria = new AutomatedCriteria();
        automatedCriteria.setAuthority("Authority_ISEE");
        automatedCriteria.setCode("Code_ISEE");
        automatedCriteria.setField("true");
        automatedCriteria.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteria.setValue("value");
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteria);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        initiativeBeneficiaryRule.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRule.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRule;
    }

    InitiativeDTO createStep2InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeDTO.setBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        return initiativeDTO;
    }

    private InitiativeBeneficiaryRuleDTO createInitiativeBeneficiaryRuleDTO() {
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();
        SelfCriteriaBoolDTO selfCriteriaBoolDTO = new SelfCriteriaBoolDTO();
        selfCriteriaBoolDTO.setType(it.gov.pagopa.initiative.dto.TypeBoolEnum.BOOLEAN);
        selfCriteriaBoolDTO.setCode("B001");
        selfCriteriaBoolDTO.setDescription("Desc_bool");
        selfCriteriaBoolDTO.setValue(true);
        SelfCriteriaMultiDTO selfCriteriaMultiDTO = new SelfCriteriaMultiDTO();
        selfCriteriaMultiDTO.setType(it.gov.pagopa.initiative.dto.TypeMultiEnum.MULTI);
        selfCriteriaMultiDTO.setCode("B001");
        selfCriteriaMultiDTO.setDescription("Desc_Multi");
        List<String> values = new ArrayList<>();
        values.add("valore1");
        values.add("valore2");
        selfCriteriaMultiDTO.setValue(values);
        List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems = new ArrayList<>();
        anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems.add(selfCriteriaBoolDTO);
        anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems.add(selfCriteriaMultiDTO);
        initiativeBeneficiaryRuleDTO.setSelfDeclarationCriteria(anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems);
        AutomatedCriteriaDTO automatedCriteriaDTO = new AutomatedCriteriaDTO();
        automatedCriteriaDTO.setAuthority("Authority_ISEE");
        automatedCriteriaDTO.setCode("Code_ISEE");
        automatedCriteriaDTO.setField("true");
        automatedCriteriaDTO.setOperator(FilterOperatorEnum.EQ);
        automatedCriteriaDTO.setValue("value");
        List<AutomatedCriteriaDTO> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaDTO);
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaList);
        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRuleDTO;
    }

    /*
     * Step 3
     */

    Initiative createStep3Initiative() {
        Initiative initiative = createStep2Initiative();
        //TODO ora settato con l'utilizzo dei RewardGroups. Associare un faker booleano per i casi OK, altrimenti separare i 2 casi
        initiative.setRewardRule(createRewardRule(false));
        initiative.setTrxRule(createTrxRuleCondition());
        return initiative;
    }

    private InitiativeRewardRule createRewardRule(boolean isRewardFixedValue) {
        if (isRewardFixedValue) {
            //TODO Aggiungere RewardValue
            return null;
        } else {
            RewardGroups rewardGroups = new RewardGroups();
            RewardGroups.RewardGroup rewardGroup1 = new RewardGroups.RewardGroup(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
            RewardGroups.RewardGroup rewardGroup2 = new RewardGroups.RewardGroup(BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(40));
            List<RewardGroups.RewardGroup> rewardGroupList = new ArrayList<>();
            rewardGroupList.add(rewardGroup1);
            rewardGroupList.add(rewardGroup2);
            rewardGroups.setRewardGroups(rewardGroupList);
            return rewardGroups;
        }
    }

    private InitiativeTrxConditions createTrxRuleCondition() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();

        List<DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeek.DayConfig dayConfig1 = new DayOfWeek.DayConfig();
        Set<java.time.DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(java.time.DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeek.Interval> intervals = new ArrayList<>();
        DayOfWeek.Interval interval1 = new DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);
        DayOfWeek dayOfWeek = new DayOfWeek(dayConfigs);

        Threshold threshold = new Threshold();
        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);

        TrxCount trxCount = new TrxCount();
        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        MccFilter mccFilter = new MccFilter();
        mccFilter.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    InitiativeDTO createStep3InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        //TODO ora settato con l'utilizzo dei RewardGroups. Associare un faker booleano per i casi OK, altrimenti separare i 2 casi
        initiativeDTO.setRewardRule(createRewardRuleDTO(false));
        initiativeDTO.setTrxRule(createTrxRuleConditionDTO());
        return initiativeDTO;
    }

    private InitiativeRewardRuleDTO createRewardRuleDTO(boolean isRewardFixedValue) {
        if (isRewardFixedValue) {
            //TODO Aggiungere RewardValue
            return null;
        } else {
            RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
            RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
            RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(40));
            List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
            rewardGroupDTOList.add(rewardGroupDTO1);
            rewardGroupDTOList.add(rewardGroupDTO2);
            rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
            return rewardGroupsDTO;
        }
    }

    private InitiativeTrxConditionsDTO createTrxRuleConditionDTO() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();

        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<java.time.DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(java.time.DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);
        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();
        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();
        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private Initiative createStep4Initiative() {
        return createStep3Initiative();
    }

    private InitiativeDTO createStep4InitiativeDTO() {
        return createStep3InitiativeDTO();
    }

    private Initiative createStep5Initiative() {
        return createStep4Initiative();
    }

    private InitiativeDTO createStep5InitiativeDTO() {
        return createStep4InitiativeDTO();
    }

    AccumulatedAmount createAccumulatedAmountValid() {
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccumulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThreshold(BigDecimal.valueOf(100000));
        return amount;
    }

    TimeParameter createTimeParameterValid() {
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    AdditionalInfo createAdditionalInfoValid() {
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }

    InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmountValid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }

    InitiativeRefundRule createRefundRuleValidWithTimeParameter() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameterValid());
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }

    Initiative createInitiativeOnlyRefundRule() {
        Initiative initiative = createStep1Initiative();
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }
}
