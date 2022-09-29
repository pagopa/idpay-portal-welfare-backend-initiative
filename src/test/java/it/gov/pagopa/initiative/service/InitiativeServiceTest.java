package it.gov.pagopa.initiative.service;


import it.gov.pagopa.initiative.connector.io.service.IOBackEndRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(value = {
        InitiativeService.class})
@Slf4j
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

    @Autowired
    InitiativeService initiativeService;

    @MockBean
    InitiativeRepository initiativeRepository;

    @MockBean
    InitiativeProducer initiativeProducer;

    @MockBean
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @MockBean
    InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;

    @MockBean
    IOBackEndRestConnector ioBackEndRestConnector;

    @Test
    void retrieveInitiativeSummary_ok() throws Exception {
        Initiative step2Initiative1 = createStep2Initiative();
        Initiative step2Initiative2 = createStep2Initiative();
        List<Initiative> initiativeList = Arrays.asList(step2Initiative1, step2Initiative2);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeSummary(ORGANIZATION_ID, false)).thenReturn(initiativeList);

        //Try to call the Real Service (which is using the instructed Repo)
        List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID);

        //Check the equality of the results
        assertEquals(initiativeList, initiatives);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeSummary(ORGANIZATION_ID, false); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void retrieveInitiativeSummary_ko() {
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            List<Initiative> initiatives = initiativeService.retrieveInitiativeSummary(ORGANIZATION_ID);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void insertInitiative_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.insert(any(Initiative.class))).thenReturn(step2Initiative);

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.insertInitiative(step2Initiative);

        //Check the equality of the results
        assertEquals(step2Initiative, initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).insert(any(Initiative.class));
    }

    @Test
    void getInitiative_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID);

        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void getInitiative_ko() throws Exception {
        //Try to call the Real Service (which is using the instructed Repo)
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenThrow(new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                "anyString()",
                HttpStatus.NOT_FOUND
        ));
        try {
            Initiative initiative = initiativeService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void getInitiativeBeneficiaryView_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.retrieveInitiativeBeneficiaryView(INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeService.getInitiativeBeneficiaryView(INITIATIVE_ID);

        //Check the equality of the results
        assertEquals(Optional.ofNullable(step2Initiative).get(), initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).retrieveInitiativeBeneficiaryView(INITIATIVE_ID, false); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void getInitiativeBeneficiaryView_ko() throws Exception {
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            Initiative initiative = initiativeService.getInitiativeBeneficiaryView(INITIATIVE_ID);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeGeneralInfo_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeGeneralInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }

    @Test
    void updateInitiativeGeneralInfo_ko() throws Exception {
        Initiative fullInitiative = createStep2Initiative();
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeGeneralInfo("Ente1", INITIATIVE_ID, fullInitiative);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeAdditionalInfo_ok() throws Exception {
        Initiative step2Initiative = createStep1Initiative();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeAdditionalInfo(ORGANIZATION_ID, INITIATIVE_ID, step2Initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }

    @Test
    void updateInitiativeAdditionalInfo_ko() throws Exception {
        Initiative fullInitiative = createStep1Initiative();
        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeAdditionalInfo(ORGANIZATION_ID, INITIATIVE_ID, fullInitiative);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
        }
    }

    @Test
    void updateInitiativeBeneficiary_ok() throws Exception {
        Initiative step2Initiative = createStep2Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(step2Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }

    @Test
    void updateInitiativeBeneficiary_ko() throws Exception {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateInitiativeBeneficiary(ORGANIZATION_ID, INITIATIVE_ID, initiativeBeneficiaryRule);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateInitiativeRewardAndTrxRules_ok() throws Exception {
        Initiative step3Initiative = createStep3Initiative();

        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(step3Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }

    @Test
    void updateInitiativeRewardAndTrxRules_ko() throws Exception {
        InitiativeRewardRule rewardRule = createRewardRule(false);
        InitiativeTrxConditions trxRuleCondition = createTrxRuleCondition();
        Initiative initiative = Initiative.builder().rewardRule(rewardRule).trxRule(trxRuleCondition).build();

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        //Try to call the Real Service (which is using the instructed Repo)
        try {
            initiativeService.updateTrxAndRewardRules(ORGANIZATION_ID, INITIATIVE_ID, initiative);
        } catch (InitiativeException e) {
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateRefundRules_ok(){
        Initiative initiative = createInitiativeOnlyRefundRule();
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(initiative));
        initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, false);
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }

    @Test
    void updateRefundRules_ko(){
        Initiative initiative = createInitiativeOnlyRefundRule();
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        try{
            initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, false);
        }catch (InitiativeException e){
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateRefundRule_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException(){
        Initiative initiative = Initiative.builder().initiativeId(INITIATIVE_ID).status(InitiativeConstants.Status.PUBLISHED).build();
        Initiative initiativeNotProcessable = Initiative.builder().initiativeId(INITIATIVE_ID).status(InitiativeConstants.Status.PUBLISHED).build();

        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(initiativeNotProcessable));

        InitiativeException exception = assertThrows(InitiativeException.class, () -> initiativeService.updateInitiativeRefundRules(ORGANIZATION_ID, INITIATIVE_ID, initiative, true));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void updateInitiativeApprovedStatus_thenStatusIsChangedWithSuccess(){
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.IN_REVISION);
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(initiative));
        initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID);
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }

    @Test
    void updateInitiativeApprovedStatus_thenThrowInitiativeException(){
        Initiative initiative = createStep4Initiative();
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.of(initiative));

        try{
            initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID);
        }catch (InitiativeException e){
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.BadRequest.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION), e.getMessage());
        }
    }

    @Test
    void updateInitiativeApprovedStatus_then400isRaisedForInitiativeException(){
        Initiative initiative = createStep4Initiative();
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        try{
            initiativeService.updateInitiativeApprovedStatus(ORGANIZATION_ID, INITIATIVE_ID);
        }catch (InitiativeException e){
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void logicallyDeleteInitiative_thenDeletedIsSettedToTrueWithSuccess(){
        Initiative initiative = createStep5Initiative();
        initiative.setDeleted(false);

        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(initiative));
        initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID);

        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }

    @Test
    void logicallyDeleteInitiative_thenThrowNewInitiativeException(){
        Initiative initiative = createStep5Initiative();
        initiative.setDeleted(false);
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.empty());

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID);

        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.NotFound.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE.formatted(initiative.getInitiativeId()), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void logicallyDeleteInitiativeStatusInRevision_thenThrowNewInitiativeException(){
        Initiative initiative = createStep5Initiative();
        initiative.setDeleted(false);
        initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.of(initiative));

        try{
            initiativeService.logicallyDeleteInitiative(ORGANIZATION_ID, INITIATIVE_ID);
        }catch (InitiativeException e){
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.BadRequest.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_CANNOT_BE_DELETED, INITIATIVE_ID), e.getMessage());
        }

    }

    @Test
    void updateInitiativeStatusToCheck_thenStatusIsUpdatedWithSuccess(){
        Initiative step4Initiative = createStep4Initiative();
        step4Initiative.setStatus(InitiativeConstants.Status.IN_REVISION);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenReturn(Optional.ofNullable(step4Initiative));

        //Try to call the Real Service (which is using the instructed Repo)
        initiativeService.updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository, times(1)).findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false);
    }
    @Test
    void updateInitiativeStatusToCheck_thenThrowInitiativeExceptionStatusIsNotInRevision(){
        Initiative step4Initiative = createStep4Initiative();
        step4Initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION, INITIATIVE_ID),
                        HttpStatus.BAD_REQUEST
                )
        );

        try{
            initiativeService.updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID);
        }catch (InitiativeException e){
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.BadRequest.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateInitiativeStatusToCheck_thenThrowExceptionAndStatusIsNotUpdated(){
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndDeleted(ORGANIZATION_ID, INITIATIVE_ID, false)).thenThrow(
                new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID),
                        HttpStatus.NOT_FOUND)
        );

        try{
            initiativeService.updateInitiativeToCheckStatus(ORGANIZATION_ID, INITIATIVE_ID);
        }catch (InitiativeException e){
            log.info("InitiativeException: " + e.getCode());
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertEquals(InitiativeConstants.Exception.NotFound.CODE, e.getCode());
            assertEquals(String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, INITIATIVE_ID), e.getMessage());
        }
    }

    @Test
    void updateInitiativeStatusToPUBLISHED_thenInitiativeIsUpdated(){
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
    void givenInitiativeAPPROVEDandNextStatusPUBLISHED_whenInitiativeIsAllowedToBeNextStatus_thenOk(){
        //Instruct Initiative to have a status Valid (APPROVED)
        Initiative initiative = createStep5Initiative();
        initiative.setStatus(InitiativeConstants.Status.APPROVED);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED);
        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void givenOneOfInitiativeStatusNotAPPROVEDandNextStatusPUBLISHED_whenInitiativeIsNOTAllowedToBeNextStatus_thenThrowInitiativeException(){
        //Instruct Initiative to have a status Not Valid
        Initiative initiative = createStep5Initiative();
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED);

        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID.formatted(initiative.getInitiativeId()), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void givenAnyInitiative_whenNextStatusIsNotSetOfInitiativeStatus_thenThrowInitiativeException(){
        //Instruct Initiative to have a status Not Valid
        Initiative initiative = createStep5Initiative();

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, ANY_NOT_INITIATIVE_STATE);

        InitiativeException exception = Assertions.assertThrows(InitiativeException.class, executable);
        assertEquals(InitiativeConstants.Exception.BadRequest.CODE, exception.getCode());
        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID.formatted(initiative.getInitiativeId()), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void givenInitiativeDTO_whenRuleEngineProduceIsValid_thenOk(){
        //Instruct Initiative
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO();

        when(initiativeProducer.sendPublishInitiative(initiativeDTO)).thenReturn(true);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.sendInitiativeInfoToRuleEngine(initiativeDTO);

        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void givenInitiativeDTO_whenRuleEngineProduceIsNotValid_thenThrowException(){
        //Instruct Initiative
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO();

        when(initiativeProducer.sendPublishInitiative(initiativeDTO)).thenReturn(false);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.sendInitiativeInfoToRuleEngine(initiativeDTO);

        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, executable);
    }

    @Test //Mancano i test con Exception
    void givenDTOsInitiativeAndInitiativeOrganizationInfo_whenIntegrationWithIOBackEndIsOK_thenReturnInitiativeUpdated(){
        //Instruct Initiative
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO();

        //FIXME Should not be created with ServiceId, but raise Exception because during the main calling, additioanlInfo.serviceId are added in execution
        InitiativeAdditionalDTO initiativeAdditionalDTO = createInitiativeAdditionalDTO();

        InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO = InitiativeOrganizationInfoDTO.builder()
                .organizationName(ORGANIZATION_NAME)
                .organizationVat(ORGANIZATION_VAT)
                .organizationUserId(ORGANIZATION_USER_ID)
                .organizationUserRole(ORGANIZATION_USER_ROLE)
                .build();

        ServiceRequestDTO serviceRequestDTOexpected = createServiceRequestDTO();
        ServiceResponseDTO serviceResponseDTOexpected = createServiceResponseDTO();

        when(initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServicePayloadDTO(initiativeAdditionalDTO, initiativeOrganizationInfoDTO)).thenReturn(serviceRequestDTOexpected);
        when(ioBackEndRestConnector.createService(serviceRequestDTOexpected)).thenReturn(serviceResponseDTOexpected);

        //Try to call the Real Service
        //Prepare Executable with invocation of the method on your system under test
        Executable executable = () -> initiativeService.sendInitiativeInfoToIOBackEndServiceAndSaveItOnInitiative(initiativeDTO, initiativeOrganizationInfoDTO);
        Assertions.assertDoesNotThrow(executable);

        //FIXME Eventuale costruzione del serviceResponse expected da confrontare con l'actual in assertEquals
//        InitiativeDTO initiativeDTOactual = initiativeService.sendInitiativeInfoToIOBackEndService(initiativeDTO, initiativeOrganizationInfoDTO);
//        assertEquals(serviceResponseDTOexpected, initiativeDTOactual);

        //Expecting mapper to be called once with correct param
        verify(initiativeAdditionalDTOsToIOServiceRequestDTOMapper, times(1)).toServicePayloadDTO(initiativeAdditionalDTO, initiativeOrganizationInfoDTO);

        //Expecting connector to be called once with correct param
        verify(ioBackEndRestConnector, times(1)).createService(serviceRequestDTOexpected);
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
                .build();
    }

    Initiative createFullInitiative () {
        Initiative initiative = createStep5Initiative();
        return initiative;
    }

    InitiativeDTO createFullInitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO();
        return initiativeDTO;
    }

    /*
     * Step 1
     */

    Initiative createStep1Initiative () {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setInitiativeName(INITIATIVE_NAME);
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setStatus("DRAFT");
        initiative.setPdndToken("pdndToken1");
        initiative.setAdditionalInfo(createInitiativeAdditional());
//        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
//        initiative.setLegal(createInitiativeLegal());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(boolean beneficiaryKnown) {
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        initiativeGeneral.setBudget(new BigDecimal(1000000000));
        initiativeGeneral.setEndDate(LocalDate.of(2022, 9, 8));
        initiativeGeneral.setStartDate(LocalDate.of(2022, 8, 8));
        initiativeGeneral.setRankingStartDate(LocalDate.of(2022, 9, 18));
        initiativeGeneral.setRankingEndDate(LocalDate.of(2022, 8, 18));
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

    InitiativeDTO createStep1InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        initiativeDTO = initiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName(INITIATIVE_NAME)
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .pdndCheck(true)
                .pdndToken("pdndToken1")
                .additionalInfo(createInitiativeAdditionalDTO()).build();
        return initiativeDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(boolean beneficiaryKnown) {
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
        return initiativeGeneralDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId(SERVICE_ID);
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

    /*
     * Step 2
     */

    Initiative createStep2Initiative () {
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
        return initiativeBeneficiaryRule;
    }

    InitiativeDTO createStep2InitiativeDTO () {
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
        return initiativeBeneficiaryRuleDTO;
    }

    /*
    * Step 3
     */

    Initiative createStep3Initiative () {
        Initiative initiative = createStep2Initiative();
        //TODO ora settato con l'utilizzo dei RewardGroups. Associare un faker booleano per i casi OK, altrimenti separare i 2 casi
        initiative.setRewardRule(createRewardRule(false));
        initiative.setTrxRule(createTrxRuleCondition());
        return initiative;
    }

    private InitiativeRewardRule createRewardRule(boolean isRewardFixedValue) {
        if(isRewardFixedValue){
            //TODO Aggiungere RewardValue
            return null;
        }
        else {
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

    InitiativeDTO createStep3InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        //TODO ora settato con l'utilizzo dei RewardGroups. Associare un faker booleano per i casi OK, altrimenti separare i 2 casi
        initiativeDTO.setRewardRule(createRewardRuleDTO(false));
        initiativeDTO.setTrxRule(createTrxRuleConditionDTO());
        return initiativeDTO;
    }

    private InitiativeRewardRuleDTO createRewardRuleDTO(boolean isRewardFixedValue) {
        if(isRewardFixedValue){
            //TODO Aggiungere RewardValue
            return null;
        }
        else {
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

    private Initiative createStep4Initiative () {
        Initiative initiative = createStep3Initiative();
        return initiative;
    }

    private InitiativeDTO createStep4InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        return initiativeDTO;
    }

    private Initiative createStep5Initiative () {
        Initiative initiative = createStep4Initiative();
        return initiative;
    }

    private InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep4InitiativeDTO();
        return initiativeDTO;
    }

    AccumulatedAmount createAccumulatedAmountValid(){
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccomulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThreshold(BigDecimal.valueOf(100000));
        return amount;
    }

    TimeParameter createTimeParameterValid(){
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    AdditionalInfo createAdditionalInfoValid(){
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }

    InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmountValid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }
    InitiativeRefundRule createRefundRuleValidWithTimeParameter(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameterValid());
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }

    Initiative createInitiativeOnlyRefundRule(){
        Initiative initiative = createStep1Initiative();
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }
}
