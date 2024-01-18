package it.gov.pagopa.initiative.service;


import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import it.gov.pagopa.initiative.connector.decrypt.DecryptRestConnector;
import it.gov.pagopa.initiative.connector.encrypt.EncryptRestConnector;
import it.gov.pagopa.initiative.connector.file_storage.InitiativeFileStorageConnector;
import it.gov.pagopa.initiative.connector.group.GroupRestConnector;
import it.gov.pagopa.initiative.connector.io_service.IOManageBackEndRestConnector;
import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestConnector;
import it.gov.pagopa.initiative.connector.ranking.RankingRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.NotFound;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Status;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.io.service.OrganizationDTO;
import it.gov.pagopa.initiative.dto.io.service.*;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.event.CommandsProducer;
import it.gov.pagopa.initiative.event.InitiativeProducer;
import it.gov.pagopa.initiative.exception.custom.*;
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
import it.gov.pagopa.initiative.utils.AuditUtilities;
import it.gov.pagopa.initiative.utils.InitiativeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_NOT_VALID_END_DATE;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.ADMIN;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Role.PAGOPA_ADMIN;
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
    private static final String ORGANIZATION_USER_ROLE = "organizationUserRole";
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
    AuditUtilities auditUtilities;

    @MockBean
    InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;
    @MockBean
    IOManageBackEndRestConnector ioManageBackEndRestConnector;

    @MockBean
    GroupRestConnector groupRestConnector;

    @MockBean
    InitiativeFileStorageConnector initiativeFileStorageConnector;

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
    InitiativeValidationService initiativeValidationService;

    @MockBean
    InitiativeUtils initiativeUtils;

    @MockBean
    CommandsProducer commandsProducer;


    @ParameterizedTest
    @ValueSource(strings = {PAGOPA_ADMIN,ADMIN})
    void givenInitiativeList_whenRoleParametrized_thenRetrieveInitiativeSummary_ok(String role) {
        Initiative step2Initiative1 = createStep2Initiative();
        step2Initiative1.setStatus(Status.PUBLISHED);
        Initiative step2Initiative2 = createStep2Initiative();
        step2Initiative2.setStatus(Status.CLOSED);
        Initiative step2Initiative3 = createStep2Initiative();
        step2Initiative3.setStatus(Status.SUSPENDED);
        Initiative step2Initiative4 = createStep2Initiative();
        step2Initiative4.setStatus(Status.DRAFT);
        Initiative step2Initiative5 = createStep2Initiative();
        step2Initiative5.setStatus(Status.APPROVED);
        Initiative step2Initiative6 = createStep2Initiative();
        step2Initiative6.setStatus(Status.TO_CHECK);
        Initiative step2Initiative7 = createStep2Initiative();
        step2Initiative7.setStatus(Status.IN_REVISION);
        List<Initiative> initiativeList = Arrays.asList(step2Initiative1, step2Initiative2,step2Initiative3,step2Initiative4,step2Initiative5,step2Initiative6,step2Initiative7);
        initiativeList = PAGOPA_ADMIN.equals(role) ? initiativeList.stream().filter(
                        initiative -> (
                                initiative.getStatus().equals(Status.IN_REVISION) ||
                                        initiative.getStatus().equals(Status.TO_CHECK) ||
                                        initiative.getStatus().equals(Status.APPROVED) ||
                                        initiative.getStatus().equals(Status.CLOSED) ||
                                        initiative.getStatus().equals(Status.PUBLISHED)))
                .toList() : initiativeList;

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(ORGANIZATION_ID, true)).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, role);

        //Check the equality of the results
        assertEquals(initiatives.size(),initiativeList.size());

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(ORGANIZATION_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void givenInitiativeList_when_RolePagopa_then_ListOfRetrieveInitiativeSummary_isEmpty() {
        Initiative step2Initiative3 = createStep2Initiative();
        step2Initiative3.setStatus(Status.SUSPENDED);
        Initiative step2Initiative4 = createStep2Initiative();
        step2Initiative4.setStatus(Status.DRAFT);
        List<Initiative> initiativeList = Arrays.asList(step2Initiative3,step2Initiative4);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(ORGANIZATION_ID, true)).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID, PAGOPA_ADMIN);

        //Check the equality of the results
        assertEquals(0,initiatives.size());

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(ORGANIZATION_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void testGetInitiativesIssuerList() {
        Initiative step2Initiative1 = createStep2Initiative();
        step2Initiative1.setStatus(Status.PUBLISHED);
        List<Initiative> initiativeList = List.of(step2Initiative1);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByEnabledAndStatus(true, "PUBLISHED")).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.getPublishedInitiativesList();

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
        when(initiativeRepository.retrieveByServiceId(SERVICE_ID)).thenReturn(Optional.ofNullable(step1Initiative));
        Initiative initiative = initiativeService.getInitiativeIdFromServiceId(SERVICE_ID);
        assertEquals(step1Initiative, initiative);
        verify(initiativeRepository, times(1)).retrieveByServiceId(SERVICE_ID);
    }

    @Test
    void getInitiativeIdFromServiceId_throwInitiativeException_thenValidationFailed() {
        when(initiativeRepository.retrieveByServiceId(SERVICE_ID)).thenReturn(Optional.empty());
        Mockito.doThrow(new InitiativeNotFoundException("Initiative with serviceId [%s] not found".formatted(SERVICE_ID))).when(initiativeRepository).retrieveByServiceId("");
        try {
            initiativeService.getInitiativeIdFromServiceId(SERVICE_ID);
        } catch (InitiativeNotFoundException e) {
            log.info("InitiativeNotFoundException: " + e.getCode());
            assertEquals(NotFound.INITIATIVE_NOT_FOUND, e.getCode());
            assertEquals("Initiative with serviceId [%s] not found".formatted(SERVICE_ID), e.getMessage());
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
        doThrow(new InitiativeNotFoundException(InitiativeConstants.Exception.NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeNotFoundException exception = Assertions.assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
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
    void getInitiativeBeneficiaryDetail_ok() {
       Initiative fullInitiative = createFullInitiative();
       Locale acceptLanguage = Locale.ITALIAN;

       when(initiativeRepository.findByInitiativeIdAndStatusIn(anyString(),anyList())).thenReturn(Optional.of(fullInitiative));
       InitiativeDetailDTO initiativeDetailDTO = createInitiativeDetailDTO();
       when(initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage)).thenReturn(initiativeDetailDTO);
       InitiativeDetailDTO initiativeDetailDTO1 = initiativeService.getInitiativeBeneficiaryDetail(INITIATIVE_ID,acceptLanguage);

       assertEquals(initiativeDetailDTO,initiativeDetailDTO1);

    }

    @Test
    void getInitiativeBeneficiaryDetail_ko() {

        Locale acceptLanguage = Locale.ITALIAN;

        when(initiativeRepository.findByInitiativeIdAndStatusIn(anyString(),anyList())).thenReturn(Optional.empty());
        try {
            initiativeService.getInitiativeBeneficiaryDetail(INITIATIVE_ID,acceptLanguage);
        } catch (InitiativeNotFoundException e){
            assertEquals(NotFound.INITIATIVE_NOT_FOUND,e.getCode());
            assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), e.getMessage());
        }

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
        } catch (InitiativeNotFoundException e) {
            System.out.println("InitiativeNotFoundException: " + e.getCode());
            assertEquals(NotFound.INITIATIVE_NOT_FOUND,e.getCode());
            assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), e.getMessage());
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
        initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative, ROLE, true);
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
        Executable executable = () -> initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative, ROLE, true);
        InitiativeStatusNotValidException exception = Assertions.assertThrows(InitiativeStatusNotValidException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_VALID, exception.getCode());
        assertEquals("Initiative [%s] with status [%s] is unprocessable for status not valid".formatted(INITIATIVE_ID,step2Initiative.getStatus()), exception.getMessage());
    }

    @Test
    void updateInitiativeGeneralInfo_thenThrowInitiativeException() {
        Initiative fullInitiative = createStep2Initiative();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, fullInitiative, ROLE, false);
        InitiativeNotFoundException exception = Assertions.assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsNF_ok() {
        Initiative fullInitiative = createStep2Initiative();
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        Initiative initiative = createStep2Initiative();
        initiative.setGeneral(generalInfoInitiative);
        initiative.setInitiativeName("serviceName");

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID,INITIATIVE_ID,ROLE)).thenReturn(fullInitiative);


        initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID,INITIATIVE_ID,initiative,ROLE, false);

        assertEquals(fullInitiative,initiative);
}
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsNFAndFamilyUnitCompositionIsNull_ko() {
        Initiative fullInitiative = createFullInitiative();
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        fullInitiative.getGeneral().setFamilyUnitComposition(null);
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setGeneral(generalInfoInitiative);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID,INITIATIVE_ID,ROLE)).thenReturn(fullInitiative);

        try {
            initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID,INITIATIVE_ID,step2Initiative,ROLE, false);
        } catch (InitiativeFamilyUnitCompositionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] family unit composition must be unset because beneficiary type is not NF".formatted(fullInitiative.getInitiativeId()), e.getMessage());
        }
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsNFAndFamilyUnitCompositionIsNotInpsOrAnpr_ko() {
        Initiative fullInitiative = createFullInitiative();
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        fullInitiative.getGeneral().setFamilyUnitComposition("TEST");
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setGeneral(generalInfoInitiative);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID,INITIATIVE_ID,ROLE)).thenReturn(fullInitiative);

        try {
            initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID,INITIATIVE_ID,step2Initiative,ROLE, false);
        } catch (InitiativeFamilyUnitCompositionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] family unit composition must be set as 'INPS' or 'ANPR'".formatted(fullInitiative.getInitiativeId()), e.getMessage());
        }
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsPFAndFamilyUnitCompositionIsNotNull_ko() {
        Initiative fullInitiative = createFullInitiative();
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        fullInitiative.getGeneral().setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setGeneral(generalInfoInitiative);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID,INITIATIVE_ID,ROLE)).thenReturn(fullInitiative);

        try {
            initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID,INITIATIVE_ID,step2Initiative,ROLE, false);
        } catch (InitiativeFamilyUnitCompositionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] family unit composition must be unset because beneficiary type is not NF".formatted(fullInitiative.getInitiativeId()), e.getMessage());
        }
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
        } catch (InitiativeRequiredLanguageException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_ITALIAN_LANGUAGE_REQUIRED_FOR_DESCRIPTION, e.getCode());
            assertEquals("Italian language is required for initiative [%s] description".formatted(fullInitiative.getInitiativeId()),e.getMessage());
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
    void storeInitiativeLogo_ok() {
        InputStream logo = new ByteArrayInputStream("logo.png".getBytes());
        Initiative initiative = this.createFullInitiative();
        Set <String> logoExtension = new HashSet<>();
        Set <String> logoMimeTypes = new HashSet<>();
        logoExtension.add(LOGO_EXTENSION);
        logoMimeTypes.add(LOGO_MIME_TYPE);
        InitiativeGeneral general = createInitiativeGeneral(true);
        initiative.setGeneral(general);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doNothing().when(initiativeFileStorageConnector).uploadInitiativeLogo(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoExtensions()).thenReturn(logoExtension);
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoMimeTypes()).thenReturn(logoMimeTypes);
        LogoDTO logoDTO = initiativeService.storeInitiativeLogo(ORGANIZATION_ID, INITIATIVE_ID, logo, LOGO_MIME_TYPE,
                FILE_NAME);

        assertEquals(FILE_NAME,logoDTO.getLogoFileName());
    }

    @Test
    void storeInitiativeLogo_koExtension() {
        InputStream logo = new ByteArrayInputStream("logo.png".getBytes());
        Initiative initiative = this.createFullInitiative();
        Set <String> logoExtension = new HashSet<>();
        Set <String> logoMimeTypes = new HashSet<>();
        logoExtension.add(LOGO_EXTENSION);
        logoMimeTypes.add(LOGO_MIME_TYPE);
        InitiativeGeneral general = createInitiativeGeneral(true);
        initiative.setGeneral(general);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doNothing().when(initiativeFileStorageConnector).uploadInitiativeLogo(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoExtensions()).thenReturn(logoExtension);
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoMimeTypes()).thenReturn(logoMimeTypes);
        try {
            initiativeService.storeInitiativeLogo(ORGANIZATION_ID, INITIATIVE_ID, logo, LOGO_MIME_TYPE,
                    "logo.jpg");
        } catch(InitiativeLogoException e) {
            assertEquals(InternalServerError.INITIATIVE_LOGO_ERROR, e.getCode());
            assertTrue(e.getMessage().contains("An error occurred during the uploading logo"));
        }
    }

    @Test
    void storeInitiativeLogo_koMimeType() {
        InputStream logo = new ByteArrayInputStream("logo.png".getBytes());
        Initiative initiative = this.createFullInitiative();
        Set <String> logoExtension = new HashSet<>();
        Set <String> logoMimeTypes = new HashSet<>();
        logoExtension.add(LOGO_EXTENSION);
        logoMimeTypes.add(LOGO_MIME_TYPE);
        InitiativeGeneral general = createInitiativeGeneral(true);
        initiative.setGeneral(general);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doNothing().when(initiativeFileStorageConnector).uploadInitiativeLogo(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoExtensions()).thenReturn(logoExtension);
        Mockito.when(initiativeUtils.getAllowedInitiativeLogoMimeTypes()).thenReturn(logoMimeTypes);
        try {
            initiativeService.storeInitiativeLogo(ORGANIZATION_ID, INITIATIVE_ID, logo, "image/jpg",
                    FILE_NAME);
        } catch(InitiativeLogoException e) {
            assertEquals(InternalServerError.INITIATIVE_LOGO_ERROR, e.getCode());
            assertTrue(e.getMessage().contains("An error occurred during the uploading logo"));
        }
    }
    @Test
    void storeInitiativeLogo_initiativeNotFound() {
        InputStream logo = new ByteArrayInputStream("logo.png".getBytes());
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true))
                .thenThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                        NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)));
        Mockito.doNothing().when(initiativeFileStorageConnector).uploadInitiativeLogo(Mockito.any(), Mockito.anyString(),
                Mockito.anyString());
        try {
            initiativeService.storeInitiativeLogo(ORGANIZATION_ID, INITIATIVE_ID, logo, LOGO_MIME_TYPE,
                    FILE_NAME);
        } catch (InitiativeNotFoundException e){
            assertEquals(NotFound.INITIATIVE_NOT_FOUND, e.getCode());
            assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), e.getMessage());
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
        doThrow(new InitiativeNotFoundException(
                NotFound.INITIATIVE_NOT_FOUND,
                String.format(NotFound.INITIATIVE_NOT_FOUND_MESSAGE, INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, fullInitiative, ROLE, false);
        InitiativeNotFoundException exception = Assertions.assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeBeneficiary_ok() {
        Initiative step2Initiative = createStep2Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        //Instruct the initiativeValidationService Mock to do nothing for checkAutomatedCriteriaOrderDirectionWithRanking
        doNothing().when(initiativeValidationService).checkAutomatedCriteria(step2Initiative,automatedCriteriaList);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule, ROLE, false);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void updateInitiativeBeneficiaryDraft_ok() {
        Initiative step2Initiative = createStep2Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step2Initiative);

        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        //Instruct the initiativeValidationService Mock to do nothing for checkAutomatedCriteriaOrderDirectionWithRanking
        doNothing().when(initiativeValidationService).checkAutomatedCriteria(step2Initiative,automatedCriteriaList);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule, ROLE, true);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
    }

    @Test
    void updateInitiativeBeneficiary_thenThrowInitiativeException() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateStep3InitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule, ROLE, true);
        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsPFAndISeeIsMissing_ko() {
        Initiative fullInitiative = createFullInitiative();
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        InitiativeBeneficiaryRule beneficiaryInfoInitiative = createInitiativeBeneficiaryRule();
        fullInitiative.setBeneficiaryRule(beneficiaryInfoInitiative);
        fullInitiative.getBeneficiaryRule().setAutomatedCriteria(null);
        Initiative step2Initiative = createStep2Initiative();
        step2Initiative.setGeneral(generalInfoInitiative);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID,INITIATIVE_ID,ROLE)).thenReturn(fullInitiative);

        try {
            initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID,INITIATIVE_ID,step2Initiative,ROLE, false);
        } catch (AutomatedCriteriaNotValidException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_BENEFICIARY_NF_ISEE_MISSING, e.getCode());
            assertEquals("Automated criteria for family initiative [%s] not valid because ISEE is missing".formatted(INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateInitiativeRewardAndTrxRules_ok() {
        Initiative step3Initiative = createStep3Initiative();

        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step3Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE, false);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        verify(initiativeValidationService, times(1)).checkRewardRuleAbsolute(initiative);
    }

    @Test
    void updateInitiativeRewardAndTrxRulesDraft_ok() {
        Initiative step3Initiative = createStep3Initiative();

        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(step3Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE, true);

        // you are expecting initiativeValidationService to be called once with correct param
        verify(initiativeValidationService, times(1)).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        verify(initiativeValidationService, times(0)).checkRewardRuleAbsolute(step3Initiative);
    }

    @Test
    void updateInitiativeRewardAndTrxRules_thenThrowInitiativeException() {
        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        //doThrow InitiativeException for getInitiative method
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, ROLE, false);
        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
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
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, false);
        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
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
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, () -> initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, ROLE, initiative, true));

        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
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
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void givenStatusTO_CHECK_updateInitiativeApprovedStatus_whenStatusNotValid_thenThrowInitiativeException() {
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //Instruct the initiativeValidationService Mock to return Dummy Initiatives
        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeStatusNotValidException exception = Assertions.assertThrows(InitiativeStatusNotValidException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_VALID, exception.getCode());
        assertEquals("The status of initiative [%s] is not IN_REVISION".formatted(INITIATIVE_ID), exception.getMessage());
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
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void logicallyDeleteInitiative_thenThrowNewInitiativeException2() {
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.PUBLISHED);

        when(initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE)).thenReturn(initiative);

        try {
            initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        }
        catch (DeleteInitiativeException e){
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_CANNOT_BE_DELETED, e.getCode());
            assertEquals("Initiative [%s] with current status [%s] cannot be deleted".formatted(initiative.getInitiativeId(),initiative.getStatus()),e.getMessage());
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
        doThrow(new InitiativeNotFoundException(NotFound.INITIATIVE_NOT_FOUND,
                NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID)))
                .when(initiativeValidationService).getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ROLE);

        //prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID, ROLE);
        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, executable);
        assertEquals(NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID), exception.getMessage());
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

        LocalDate localDateNow = LocalDate.now();
        InitiativeGeneral initiativeGeneral = InitiativeGeneral.builder()
                .startDate(localDateNow.minusDays(5))
                .endDate(localDateNow)
                .build();
        initiative.setGeneral(initiativeGeneral);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);
        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void givenInitiativeAPPROVEDandNextStatusPUBLISHED_whenInitiativeIsAllowedToBeNextStatus_thenKoEndDate() {
        //Instruct Initiative to have a status Valid (APPROVED)
        Initiative initiative = createStep5Initiative();
        initiative.setStatus(InitiativeConstants.Status.APPROVED);

        LocalDate localDateNow = LocalDate.now();
        InitiativeGeneral initiativeGeneral = InitiativeGeneral.builder()
                .startDate(localDateNow.minusDays(5))
                .endDate(localDateNow.minusDays(3))
                .build();
        initiative.setGeneral(initiativeGeneral);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        InitiativeDateInvalidException initiativeDateInvalidException = assertThrows(InitiativeDateInvalidException.class,
                () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, Status.PUBLISHED, ROLE));

        assertEquals(INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_NOT_VALID_END_DATE, initiativeDateInvalidException.getCode());
        assertEquals("Initiative [%s] unprocessable because the end date [%s] has passed".formatted(initiative.getInitiativeId(),initiativeGeneral.getEndDate()), initiativeDateInvalidException.getMessage());
    }

    @Test
    void givenOneOfInitiativeStatusNotAPPROVEDandNextStatusPUBLISHED_whenInitiativeIsNOTAllowedToBeNextStatus_thenThrowInitiativeException() {
        //Instruct Initiative to have a status Not Valid
        Initiative initiative = createStep5Initiative();
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, ROLE);

        InitiativeStatusNotValidException exception = Assertions.assertThrows(InitiativeStatusNotValidException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_VALID, exception.getCode());
        assertEquals("Initiative [%s] with status [%s] is unprocessable for status not valid".formatted(initiative.getInitiativeId(),initiative.getStatus()), exception.getMessage());
    }

    @Test
    void givenAnyInitiative_whenNextStatusIsNotSetOfInitiativeStatus_thenThrowInitiativeException() {
        //Instruct Initiative to have a status Not Valid
        Initiative initiative = createStep5Initiative();

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, ANY_NOT_INITIATIVE_STATE, ROLE);

        InitiativeStatusNotValidException exception = Assertions.assertThrows(InitiativeStatusNotValidException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_VALID, exception.getCode());
        assertEquals("Initiative [%s] with status [%s] is unprocessable for status not valid".formatted(initiative.getInitiativeId(),initiative.getStatus()), exception.getMessage());
    }

    @Test
    void givenInitiativeAPPROVEDandNextStatusPUBLISHED_throwsException() {
        Initiative initiative = createStep5Initiative();

        try {
            initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, STATUS,
                    InitiativeConstants.Role.PAGOPA_ADMIN);
        } catch (AdminPermissionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_ADMIN_ROLE_NOT_ALLOWED, e.getCode());
            assertEquals("Admin permission not allowed for current initiative [%s]".formatted(initiative.getInitiativeId()),e.getMessage());
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
        String serviceId = serviceResponseDTOexpected.getId();

        when(initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO)).thenReturn(serviceRequestDTOexpected);
        when(ioManageBackEndRestConnector.createService(serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);
        when(ioManageBackEndRestConnector.updateService(serviceId,serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);

        Initiative initiativeActual = initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
        assertEquals(SERVICE_ID, initiativeActual.getAdditionalInfo().getServiceId());

        //Expecting connector to be called once with correct param
        verify(ioManageBackEndRestConnector, times(1)).createService(serviceRequestDTOexpected);
        verify(ioManageBackEndRestConnector, times(1)).updateService(serviceId,serviceRequestDTOexpected);
    }

    @Test
    void sendInitiativeInfoToIOBackendServiceAndUpdateInitiative_withUploadServiceIOKO() {
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
        String serviceId = serviceResponseDTOexpected.getId();

        when(initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO)).thenReturn(serviceRequestDTOexpected);
        when(ioManageBackEndRestConnector.createService(serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);
        when(ioManageBackEndRestConnector.updateService(serviceId,serviceRequestDTOexpected)).thenThrow(new RuntimeException());

        Initiative initiativeActual = initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
        assertEquals(SERVICE_ID, initiativeActual.getAdditionalInfo().getServiceId());

        //Expecting connector to be called once with correct param
        verify(ioManageBackEndRestConnector, times(1)).createService(serviceRequestDTOexpected);
        verify(ioManageBackEndRestConnector, times(1)).updateService(serviceId,serviceRequestDTOexpected);
    }

    @Test
    void sendInitiativeInfoToIOBackEndServiceAndUpdateInitiativeWithLogo_serviceIdAlreadyExisting() {
        Initiative initiative = createStep5Initiative();
        InitiativeAdditional initiativeAdditional = createInitiativeAdditional();
        initiativeAdditional.setLogoFileName("logo file name");
        initiativeAdditional.setServiceId(SERVICE_ID);
        initiative.setAdditionalInfo(initiativeAdditional);
        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        ServiceRequestDTO serviceRequestDTOexpected = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();
        String serviceId = serviceResponseDTOexpected.getId();

        when(initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(initiativeAdditional, initiativeOrganizationInfoDTO)).thenReturn(serviceRequestDTOexpected);
        when(ioManageBackEndRestConnector.updateService(serviceId,serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);
        Mockito.doNothing().when(ioManageBackEndRestConnector).sendLogoIo(anyString(),any());

        Initiative initiativeActual = initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
        assertEquals(SERVICE_ID, initiativeActual.getAdditionalInfo().getServiceId());

        verify(ioManageBackEndRestConnector, times(1)).updateService(serviceId,serviceRequestDTOexpected);
    }

    @Test
    void getRankingList_ok() {
        Initiative initiative = this.createFullInitiative();
        RankingRequestDTO rankingRequestDTO = new RankingRequestDTO(USER_ID,INITIATIVE_ID,ORGANIZATION_ID,LocalDateTime.now(),LocalDateTime.now(),1,1,"test", null, null);
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
            initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (InitiativeNotFoundException e) {
            assertEquals(NotFound.INITIATIVE_NOT_FOUND,e.getCode());
            assertEquals(NotFound.INITIATIVE_NOT_FOUND_MESSAGE.formatted(INITIATIVE_ID),e.getMessage());
        }
    }
    @Test
    void getRankingList_ko_encrypt() {
        Initiative initiative = this.createFullInitiative();
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new EncryptInvocationException(InternalServerError.INITIATIVE_GENERIC_ERROR,"An error occurred during the encrypt invocation")).when(encryptRestConnector).upsertToken(Mockito.any());
        try {
            initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (EncryptInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("An error occurred during the encrypt invocation", e.getMessage());
        }
    }

    @Test
    void getRankingList_ko_decrypt() {
        Initiative initiative = this.createFullInitiative();
        RankingRequestDTO rankingRequestDTO = new RankingRequestDTO(USER_ID,INITIATIVE_ID,ORGANIZATION_ID,LocalDateTime.now(),LocalDateTime.now(),1,1,"test", null, null);
        RankingPageDTO rankingPageDTO =new RankingPageDTO();
        rankingPageDTO.setContent(List.of(rankingRequestDTO));
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(rankingRestConnector.getRankingList(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(rankingPageDTO);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new DecryptInvocationException(InternalServerError.INITIATIVE_GENERIC_ERROR,"An error occurred during the decrypt invocation")).when(decryptRestConnector).getPiiByToken(Mockito.anyString());
        try {
            initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (DecryptInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("An error occurred during the decrypt invocation", e.getMessage());
        }
    }

    @Test
    void getRankingList_ko_ranking() {
        Initiative initiative = this.createFullInitiative();
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new RankingInvocationException(InternalServerError.INITIATIVE_GENERIC_ERROR,"An error occurred in the microservice ranking")).when(rankingRestConnector).getRankingList(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.anyString(),Mockito.anyString());
        try {
            initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (RankingInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("An error occurred in the microservice ranking", e.getMessage());
        }
    }

    @Test
    void getRankingList_ko_ranking_disabled() {
        Initiative initiative = this.createFullInitiative();
        initiative.setGeneral(new InitiativeGeneral());
        initiative.getGeneral().setRankingEnabled(false);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        try {
            initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                    Status.PUBLISHED);
            Assertions.fail();
        } catch (InitiativeNoRankingException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("Initiative [%s] is without ranking".formatted(INITIATIVE_ID),e.getMessage());
        }
    }
    @Test
    void getRankingList_familyUnit() {
        Initiative initiative = this.createFullInitiative();
        initiative.setGeneral(new InitiativeGeneral());
        initiative.getGeneral().setRankingEnabled(true);
        initiative.getGeneral().setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.NF);
        initiative.getGeneral().setFamilyUnitComposition(InitiativeConstants.FamilyUnitCompositionConstant.INPS);

        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));

        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);

        RankingRequestDTO rankingRequestDTO = new RankingRequestDTO(USER_ID,INITIATIVE_ID,ORGANIZATION_ID,LocalDateTime.now(),LocalDateTime.now(),1,1,"test", "FAMILY_ID", Set.of(USER_ID, "USER_ID_2"));
        RankingPageDTO rankingPageDTO =new RankingPageDTO();
        rankingPageDTO.setContent(List.of(rankingRequestDTO));
        Mockito.when(rankingRestConnector.getRankingList(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(rankingPageDTO);

        Mockito.when(decryptRestConnector.getPiiByToken(USER_ID)).thenReturn(new DecryptCfDTO(CF));
        Mockito.when(decryptRestConnector.getPiiByToken("USER_ID_2")).thenReturn(new DecryptCfDTO("CF_2"));


        BeneficiaryRankingPageDTO beneficiaryRankingDTO = initiativeService.getRankingList(ORGANIZATION_ID,INITIATIVE_ID,null, "",
                Status.PUBLISHED);
        assertEquals(CF,beneficiaryRankingDTO.getContent().get(0).getBeneficiary());
        assertEquals("FAMILY_ID", beneficiaryRankingDTO.getContent().get(0).getFamilyId());
        assertTrue(beneficiaryRankingDTO.getContent().get(0).getMemberIds().containsAll(List.of(CF, "CF_2")));
        }

    @Test
    void getOnboardingStatusList_ok() {
        Initiative initiative = this.createFullInitiative();
        OnboardingStatusCitizenDTO onboardingStatusCitizenDTO = new OnboardingStatusCitizenDTO(USER_ID, STATUS, LocalDateTime.now().toString(), "familyId");
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
        assertEquals("familyId", onboardingDTO1.getContent().get(0).getFamilyId());

    }

    @Test
    void getOnboardingStatusList_ko_encrypt() {
        Initiative initiative = this.createFullInitiative();
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new EncryptInvocationException(InternalServerError.INITIATIVE_GENERIC_ERROR,"An error occurred during the encrypt invocation")).when(encryptRestConnector).upsertToken(Mockito.any());
        try {
            initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS,
                    Pageable.ofSize(21));
        } catch (EncryptInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("An error occurred during the encrypt invocation", e.getMessage());
        }
    }

    @Test
    void getOnboardingStatusList_ko_decrypt() {
        Initiative initiative = this.createFullInitiative();
        OnboardingStatusCitizenDTO onboardingStatusCitizenDTO = new OnboardingStatusCitizenDTO(USER_ID, STATUS, LocalDateTime.now().toString(), null);
        List<OnboardingStatusCitizenDTO> onboardingStatusCitizenDTOS = new ArrayList<>();
        onboardingStatusCitizenDTOS.add(onboardingStatusCitizenDTO);
        ResponseOnboardingDTO onboardingDTO = new ResponseOnboardingDTO(onboardingStatusCitizenDTOS, 1, 1, 1, 1);
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.when(onboardingRestConnector.getOnboarding(INITIATIVE_ID, Pageable.ofSize(21), USER_ID, STARTDATE, ENDDATE, STATUS)).thenReturn(onboardingDTO);
        Mockito.doThrow(new DecryptInvocationException(InternalServerError.INITIATIVE_GENERIC_ERROR,"An error occurred during the decrypt invocation")).when(decryptRestConnector).getPiiByToken(Mockito.anyString());
        try {
            initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS,
                    Pageable.ofSize(21));
        } catch (DecryptInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("An error occurred during the decrypt invocation", e.getMessage());
        }
    }

    @Test
    void getOnboardingStatusList_ko_onboarding() {
        Initiative initiative = this.createFullInitiative();
        EncryptedCfDTO encryptedCfDTO = new EncryptedCfDTO(USER_ID);
        Mockito.when(encryptRestConnector.upsertToken(Mockito.any())).thenReturn(encryptedCfDTO);
        Mockito.when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new OnboardingInvocationException(InternalServerError.INITIATIVE_GENERIC_ERROR,"An error occurred in the microservice onboarding")).when(onboardingRestConnector).getOnboarding(INITIATIVE_ID, null, USER_ID, STARTDATE, ENDDATE, STATUS);
        try {
            initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS, null);
            Assertions.fail();
        } catch (OnboardingInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("An error occurred in the microservice onboarding", e.getMessage());
        }
    }

    @Test
    void getOnboardingStatusList_initiative_internal_server_error() {
        try {
            initiativeService.getOnboardingStatusList(ORGANIZATION_ID, INITIATIVE_ID, CF, STARTDATE, ENDDATE, STATUS, null);
            Assertions.fail();
        } catch (EncryptInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR,e.getCode());
            assertEquals("An error occurred during the encrypt invocation", e.getMessage());
        }
    }

    @Test
    void deleteInitiative_initiative_no_service_id_sendMessageOnCommandQueueError() {
        when(initiativeRepository.findById(INITIATIVE_ID)).thenReturn(Optional.ofNullable(createStep1Initiative()));
        when(commandsProducer.sendCommand(any()))
                .thenReturn(false);

        try {
            initiativeService.deleteInitiative(INITIATIVE_ID);
            Assertions.fail();
        } catch (CommandProducerException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR, e.getCode());
            assertEquals("Something went wrong while sending the message with entityId [%s] and operationType [%s] on the Commands Queue".formatted(INITIATIVE_ID,"DELETE_INITIATIVE"),e.getMessage());
            log.info(e.getMessage());
        }

        verify(ioManageBackEndRestConnector, times(0)).deleteService(anyString());
        verify(commandsProducer, times(1)).sendCommand(any());
    }


    @Test
    void deleteInitiative() {
        Initiative initiative = createFullInitiative();
        initiative.getAdditionalInfo().setServiceId("test");
        when(initiativeRepository.findById(INITIATIVE_ID)).thenReturn(Optional.of(initiative));
        when(commandsProducer.sendCommand(any()))
                .thenReturn(true);

        initiativeService.deleteInitiative(INITIATIVE_ID);

        verify(ioManageBackEndRestConnector, times(1)).deleteService("test");
        verify(commandsProducer, times(1)).sendCommand(any());
        verify(initiativeRepository, times(1)).deleteById(INITIATIVE_ID);
    }

    @Test
    void deleteInitiative_initiative_not_found() {
        when(commandsProducer.sendCommand(any()))
                .thenReturn(true);

        initiativeService.deleteInitiative(INITIATIVE_ID);

        verify(ioManageBackEndRestConnector, times(0)).deleteService(anyString());
        verify(commandsProducer, times(1)).sendCommand(any());
        verify(initiativeRepository, times(1)).deleteById(INITIATIVE_ID);
    }

    @Test
    void deleteInitiative_initiative_no_additional_info() {
        Initiative initiative = createStep1Initiative();
        initiative.setAdditionalInfo(null);
        when(initiativeRepository.findById(INITIATIVE_ID)).thenReturn(Optional.of(initiative));

        when(commandsProducer.sendCommand(any()))
                .thenReturn(true);

        initiativeService.deleteInitiative(INITIATIVE_ID);

        verify(ioManageBackEndRestConnector, times(0)).deleteService(anyString());
        verify(commandsProducer, times(1)).sendCommand(any());
        verify(initiativeRepository, times(1)).deleteById(INITIATIVE_ID);
    }

    @Test
    void deleteInitiative_throw_ioManageBackEndRestConnector_exception() {
        Initiative initiative = createFullInitiative();
        initiative.getAdditionalInfo().setServiceId("test");
        when(initiativeRepository.findById(INITIATIVE_ID)).thenReturn(Optional.of(initiative));

        Mockito.doThrow(new RuntimeException()).when(ioManageBackEndRestConnector).deleteService("test");

        when(commandsProducer.sendCommand(any()))
                .thenReturn(true);

        initiativeService.deleteInitiative(INITIATIVE_ID);

        verify(commandsProducer, times(1)).sendCommand(any());
        verify(initiativeRepository, times(1)).deleteById(INITIATIVE_ID);
    }

    @Test
    void initializeStatistics() {
        when(commandsProducer.sendCommand(any())).thenReturn(true);

        initiativeService.initializeStatistics(INITIATIVE_ID, ORGANIZATION_ID);

        verify(commandsProducer, times(1)).sendCommand(any());
    }

    @Test
    void initializeStatistics_exception() {
        String entityId = INITIATIVE_ID.concat("_").concat(ORGANIZATION_ID);
        when(commandsProducer.sendCommand(any())).thenReturn(false);

        try {
            initiativeService.initializeStatistics(INITIATIVE_ID, ORGANIZATION_ID);
            Assertions.fail();
        } catch (CommandProducerException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR, e.getCode());
            assertEquals("Something went wrong while sending the message with entityId [%s] and operationType [%s] on the Commands Queue".formatted(entityId,"CREATE_INITIATIVE_STATISTICS"),e.getMessage());
        }
    }

    @Test
    void getTokenKeys_ok(){
        try{
            KeysDTO expectedKeysDTO= KeysDTO.builder()
                    .primaryKey("key1")
                    .secondaryKey("key2")
                    .build();
            Initiative initiative = createFullInitiative();
            initiative.getAdditionalInfo().setServiceId("test");
            when(initiativeRepository.findById(INITIATIVE_ID)).thenReturn(Optional.of(initiative));
            when(ioManageBackEndRestConnector.getServiceKeys("test")).thenReturn(expectedKeysDTO);
            KeysDTO actualKeysDTO = initiativeService.getTokenKeys(INITIATIVE_ID);
            assertEquals(expectedKeysDTO, actualKeysDTO);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    void getTokenKeys_not_found(){
        try{
            initiativeService.getTokenKeys(INITIATIVE_ID);
            Assertions.fail();
        } catch (InitiativeNotFoundException e) {
            assertEquals(NotFound.INITIATIVE_NOT_FOUND, e.getCode());
            assertEquals(String.format(NotFound.INITIATIVE_NOT_FOUND_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void getTokenKeys_exception(){
        Initiative initiative = createFullInitiative();
        initiative.getAdditionalInfo().setServiceId("test");
        when(initiativeRepository.findById(INITIATIVE_ID)).thenReturn(Optional.of(initiative));
        Mockito.doThrow(new RuntimeException())
                .when(ioManageBackEndRestConnector)
                .getServiceKeys(Mockito.any());
        try{
            initiativeService.getTokenKeys(INITIATIVE_ID);
            Assertions.fail();
        } catch (IOBackEndInvocationException e) {
            assertEquals(InternalServerError.INITIATIVE_GENERIC_ERROR, e.getCode());
            assertEquals("An error occurred during the IO Back-end invocation", e.getMessage());
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
                .description(DESCRIPTION)
                .organization(createOrganizationDTO())
                .build();
    }

    private ServiceRequestDTO createServiceRequestDTO() {
        ServiceMetadataDTO serviceMetadataDTO = createServiceMetadataDTO();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(SERVICE_NAME)
                .description(DESCRIPTION)
                .organization(createOrganizationDTO())
                .build();
    }

    private ServiceMetadataDTO createServiceMetadataDTO() {
        return ServiceMetadataDTO.builder()
                .email(EMAIL)
                .phone(PHONE)
                .supportUrl(SUPPORT_URL)
                .privacyUrl(PRIVACY_URL)
                .tosUrl(TOS_URL)
                .scope(SCOPE)
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
        return ServiceResponseDTO.builder()
                .id(SERVICE_ID)
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
    private InitiativeGeneral createInitiativeGeneralFamilyUnitComposition() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.NF);
        initiativeGeneral.setFamilyUnitComposition(InitiativeConstants.FamilyUnitCompositionConstant.INPS);
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
        return InitiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName(INITIATIVE_NAME)
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .additionalInfo(createInitiativeAdditionalDTO()).build();
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
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
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

    private InitiativeDetailDTO createInitiativeDetailDTO() {
        InitiativeDetailDTO initiativeDetailDTO = new InitiativeDetailDTO();
        initiativeDetailDTO.setInitiativeName("TEST");
        initiativeDetailDTO.setStatus("APPROVED");
        initiativeDetailDTO.setDescription("test test");
        initiativeDetailDTO.setOnboardingStartDate(LocalDate.now().minusDays(25));
        initiativeDetailDTO.setOnboardingEndDate(LocalDate.now());
        initiativeDetailDTO.setFruitionStartDate(LocalDate.now());
        initiativeDetailDTO.setFruitionEndDate(LocalDate.now().plusDays(40));
        initiativeDetailDTO.setRewardRule(createRewardRuleDTO(false));
        initiativeDetailDTO.setRefundRule(null);
        initiativeDetailDTO.setPrivacyLink("privacy.it");
        initiativeDetailDTO.setTcLink("tc.it");
        initiativeDetailDTO.setLogoURL("logo.png");
        initiativeDetailDTO.setUpdateDate(LocalDateTime.now());
        initiativeDetailDTO.setServiceId("SERVICE_ID");
        return initiativeDetailDTO;
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
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        return initiative;
    }
}
