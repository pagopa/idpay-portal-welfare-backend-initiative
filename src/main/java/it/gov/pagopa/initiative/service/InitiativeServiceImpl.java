package it.gov.pagopa.initiative.service;

import feign.FeignException;
import it.gov.pagopa.initiative.connector.decrypt.DecryptRestConnector;
import it.gov.pagopa.initiative.connector.encrypt.EncryptRestConnector;
import it.gov.pagopa.initiative.connector.group.GroupRestConnector;
import it.gov.pagopa.initiative.connector.io_service.IOBackEndRestConnector;
import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;
import it.gov.pagopa.initiative.controller.filter.LoginThreadLocal;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import it.gov.pagopa.initiative.event.InitiativeProducer;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeAdditionalDTOsToIOServiceRequestDTOMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.EmailTemplate.EMAIL_INITIATIVE_CREATED;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.EmailTemplate.EMAIL_INITIATIVE_STATUS;


@Service
@Slf4j
public class InitiativeServiceImpl extends InitiativeServiceRoot implements InitiativeService {

    private final boolean notifyEmail;
    private final InitiativeRepository initiativeRepository;
    private final InitiativeModelToDTOMapper initiativeModelToDTOMapper;
    private final InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;
    private final InitiativeProducer initiativeProducer;
    private final IOBackEndRestConnector ioBackEndRestConnector;
    private final GroupRestConnector groupRestConnector;
    private final OnboardingRestConnector onboardingRestConnector;
    private final EncryptRestConnector encryptRestConnector;
    private final DecryptRestConnector decryptRestConnector;
    private final EmailNotificationService emailNotificationService;
    private final IOTokenService ioTokenService;
    private final InitiativeValidationService initiativeValidationService;
    private final LoginThreadLocal loginThreadLocal;

    public InitiativeServiceImpl(
            @Value("${app.initiative.conditions.notifyEmail}") boolean notifyEmail,
            InitiativeRepository initiativeRepository,
            InitiativeModelToDTOMapper initiativeModelToDTOMapper,
            InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper,
            InitiativeProducer initiativeProducer,
            IOBackEndRestConnector ioBackEndRestConnector,
            GroupRestConnector groupRestConnector,
            OnboardingRestConnector onboardingRestConnector,
            EncryptRestConnector encryptRestConnector,
            DecryptRestConnector decryptRestConnector,
            EmailNotificationService emailNotificationService,
            IOTokenService ioTokenService,
            InitiativeValidationService initiativeValidationService,
            LoginThreadLocal LoginThreadLocal
    ){
        this.notifyEmail = notifyEmail;
        this.initiativeRepository = initiativeRepository;
        this.initiativeModelToDTOMapper = initiativeModelToDTOMapper;
        this.initiativeProducer = initiativeProducer;
        this.initiativeAdditionalDTOsToIOServiceRequestDTOMapper = initiativeAdditionalDTOsToIOServiceRequestDTOMapper;
        this.ioBackEndRestConnector = ioBackEndRestConnector;
        this.groupRestConnector = groupRestConnector;
        this.onboardingRestConnector = onboardingRestConnector;
        this.encryptRestConnector = encryptRestConnector;
        this.decryptRestConnector = decryptRestConnector;
        this.emailNotificationService = emailNotificationService;
        this.ioTokenService = ioTokenService;
        this.initiativeValidationService = initiativeValidationService;
        loginThreadLocal = LoginThreadLocal;
    }

    public List<Initiative> retrieveInitiativeSummary(String organizationId, String role) {
        List<Initiative> initiatives = initiativeRepository.retrieveInitiativeSummary(organizationId, true);
//        if(initiatives.isEmpty()){
//            throw new InitiativeException(
//                    InitiativeConstants.Exception.NotFound.CODE,
//                    String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE, organizationId),
//                    HttpStatus.NOT_FOUND);
//        }
        return InitiativeConstants.Role.OPE_BASE.equals(role) ? initiatives.stream().filter(
                initiative -> (
                        initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION) ||
                        initiative.getStatus().equals(InitiativeConstants.Status.TO_CHECK) ||
                        initiative.getStatus().equals(InitiativeConstants.Status.APPROVED)))
                .toList() : initiatives;
    }

    @Override
    public Initiative insertInitiative(Initiative initiative, String organizationId, String organizationName, String role) {
        initiativeValidationService.checkPermissionBeforeInsert(role);
        initiative.setOrganizationId(organizationId);
        initiative.setOrganizationName(organizationName);
        insertTechnicalData(initiative);
        if (StringUtils.isBlank(initiative.getStatus())) {
            initiative.setStatus(InitiativeConstants.Status.DRAFT);
        }
        Initiative initiativeReturned = initiativeRepository.insert(initiative);
        if(notifyEmail){
            emailNotificationService.sendInitiativeToCurrentOrganization(initiative, EMAIL_INITIATIVE_CREATED);
        }
        return initiativeReturned;
    }

    @Override
    public Initiative getInitiative(String organizationId, String initiativeId, String role) {
        return initiativeValidationService.getInitiative(organizationId, initiativeId, role);
    }

    @Override
    public Initiative getInitiativeBeneficiaryView(String initiativeId) {
        return initiativeRepository.retrieveInitiativeBeneficiaryView(initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel, String role) {
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setGeneral(initiativeInfoModel.getGeneral());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
    }

    /*Primo salvataggio in Draft tramite Wizard*/
    @Override
    public void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setAdditionalInfo(initiativeAdditionalInfo.getAdditionalInfo());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
        if(notifyEmail){
            emailNotificationService.sendInitiativeToCurrentOrganization(initiative, EMAIL_INITIATIVE_STATUS);
        }
    }

    @Override
    public void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule initiativeBeneficiaryRuleModel, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setBeneficiaryRule(initiativeBeneficiaryRuleModel);
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules, String role) {
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setTrxRule(rewardAndTrxRules.getTrxRule());
        initiative.setUpdateDate(LocalDateTime.now());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        initiative.setRewardRule(rewardAndTrxRules.getRewardRule());
        this.initiativeRepository.save(initiative);
    }

    @Override
    @Transactional
    public void updateInitiativeRefundRules(String organizationId, String initiativeId, String role, Initiative refundRule, boolean changeInitiativeStatus){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setRefundRule(refundRule.getRefundRule());
        log.info("[UPDATE_REFUND_RULE] - Initiative: {}. Refund rules successfully set.", initiativeId);
        initiative.setUpdateDate(LocalDateTime.now());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        if (changeInitiativeStatus) {
            initiative.setStatus(InitiativeConstants.Status.IN_REVISION);
            log.info("[UPDATE_TO_IN_REVISION_STATUS] - Initiative: {}. Status successfully set to IN_REVISION.", initiativeId);
        }
        this.initiativeRepository.save(initiative);
        if(changeInitiativeStatus && notifyEmail){
            emailNotificationService.sendInitiativeToCurrentOrganizationAndPagoPA(initiative, EMAIL_INITIATIVE_STATUS);
        }
    }

    @Override
    @Transactional
    public void updateInitiativeApprovedStatus(String organizationId, String initiativeId, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.APPROVED);
        initiative.setStatus(InitiativeConstants.Status.APPROVED);
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
        log.info("[UPDATE_TO_APPROVED_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        if(notifyEmail){
            emailNotificationService.sendInitiativeToCurrentOrganization(initiative, EMAIL_INITIATIVE_STATUS);
        }
    }

    @Override
    @Transactional
    public void updateInitiativeToCheckStatus(String organizationId, String initiativeId, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.TO_CHECK);
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
        log.info("[UPDATE_TO_CHECK_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        if(notifyEmail){
            emailNotificationService.sendInitiativeToCurrentOrganization(initiative, EMAIL_INITIATIVE_STATUS);
        }
    }

    @Override
    public void logicallyDeleteInitiative(String organizationId, String initiativeId, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        if (
                initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION) ||
                initiative.getStatus().equals(InitiativeConstants.Status.PUBLISHED) ||
                initiative.getStatus().equals(InitiativeConstants.Status.CLOSED) ||
                initiative.getStatus().equals(InitiativeConstants.Status.SUSPENDED)
        ){
            log.error("[LOGICAL_DELETE_INITIATIVE] - Initiative: {}. Cannot be deleted. Current status is {}.", initiative.getInitiativeId(), initiative.getStatus());
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_CANNOT_BE_DELETED, initiativeId),
                    HttpStatus.BAD_REQUEST
            );
        }else{
            initiative.setEnabled(false);
            initiative.setUpdateDate(LocalDateTime.now());
            this.initiativeRepository.save(initiative);
            log.info("[LOGICAL_DELETE_INITIATIVE] - Initiative: {}. Successfully logical elimination.", initiative.getInitiativeId());
        }
        if(notifyEmail){
            emailNotificationService.sendInitiativeToCurrentOrganization(initiative, EMAIL_INITIATIVE_STATUS);
        }
    }

    @Override
    public void sendInitiativeInfoToRuleEngine(Initiative initiative) {
        if(!initiativeProducer.sendPublishInitiative(initiative)){
            throw new IllegalStateException("[UPDATE_TO_PUBLISHED_STATUS] - Something gone wrong while notify Initiative to RuleEngine");
        }
    }

    @Override
    public void sendInitiativeInfoToNotificationManager(Initiative initiative) {
        groupRestConnector.notifyInitiativeToGroup(initiative);
    }

    @Override
    public void isInitiativeAllowedToBeNextStatusThenThrows(Initiative initiative, String nextStatus, String role) {
        if(InitiativeConstants.Role.OPE_BASE.equals(role)){
            log.info("[UPDATE_TO_{}_STATUS] - Initiative: {} Status: {}. Not processable status", nextStatus, initiative.getInitiativeId(), initiative.getStatus());
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role),
                    HttpStatus.BAD_REQUEST);
        }
        switch (nextStatus){
//            case InitiativeConstants.Status.DRAFT:
//                isInitiativeAllowedToBeEditableThenThrows(initiative);
//                break;
//            case InitiativeConstants.Status.TO_CHECK:
//                isInitiativeAllowedToBeEditableThenThrows(initiative);
//                isInitiativeStatusNotInRevisionThenThrow(initiative, nextStatus);
//                break;
//            case InitiativeConstants.Status.IN_REVISION:
//                break;
//            case InitiativeConstants.Status.APPROVED:
//                isInitiativeStatusNotInRevisionThenThrow(initiative, nextStatus);
//                break;
            case InitiativeConstants.Status.PUBLISHED:
                if(!Arrays.asList(InitiativeConstants.Status.Validation.INITIATIVE_ALLOWED_STATES_TO_BECOME_PUBLISHED_ARRAY).contains(initiative.getStatus())) {
                    log.info("[UPDATE_TO_{}_STATUS] - Initiative: {} Status: {}. Not processable status", nextStatus, initiative.getInitiativeId(), initiative.getStatus());
                    throw new InitiativeException(
                            InitiativeConstants.Exception.BadRequest.CODE,
                            String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()),
                            HttpStatus.BAD_REQUEST);
                }
                break;
//            case InitiativeConstants.Status.CLOSED:
//                break;
//            case InitiativeConstants.Status.SUSPENDED:
//                break;
            default:
                throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()),
                        HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updateInitiative(Initiative initiative) {
        initiativeRepository.save(initiative);
        log.debug("Initiative {} updated", initiative.getInitiativeId());
    }

    @Override
    public Initiative sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(Initiative initiative, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO) {
        InitiativeAdditional additionalInfo = initiative.getAdditionalInfo();
        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(additionalInfo, initiativeOrganizationInfoDTO);
        ServiceResponseDTO serviceResponseDTO = ioBackEndRestConnector.createService(serviceRequestDTO);
        log.debug("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Start ServiceIO Keys encryption...", initiative.getInitiativeId());
        String encryptedPrimaryToken = ioTokenService.encrypt(serviceResponseDTO.getPrimaryKey());
        String encryptedSecondaryToken = ioTokenService.encrypt(serviceResponseDTO.getSecondaryKey());
        log.debug("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Encryption completed.", initiative.getInitiativeId());
        initiative.getAdditionalInfo().setPrimaryTokenIO(encryptedPrimaryToken);
        initiative.getAdditionalInfo().setSecondaryTokenIO(encryptedSecondaryToken);
        additionalInfo.setServiceId(serviceResponseDTO.getServiceId());
        initiative.setUpdateDate(LocalDateTime.now());
        if(notifyEmail){
            emailNotificationService.sendInitiativeToCurrentOrganizationAndPagoPA(initiative, EMAIL_INITIATIVE_STATUS);
        }
        return initiative;
    }

    @Override
    public Initiative getInitiativeIdFromServiceId(String serviceId){
        return initiativeRepository.retrieveServiceId(serviceId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_ID_BY_SERVICE_ID_MESSAGE, serviceId),
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public InitiativeAdditional getPrimaryAndSecondaryTokenIO(String initiativeId){
        Initiative initiative = initiativeRepository.findByInitiativeIdAndEnabled(initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.PRIMARY_AND_SECONDARY_TOKEN_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        return initiative.getAdditionalInfo();
    }

  @Override
  public OnboardingDTO getOnboardingStatusList(String organizationId,String initiativeId, String CF,
      LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable) {

    log.info("start get status onboarding, initiative: "+initiativeId);
    Initiative initiative = initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(
            organizationId, initiativeId, true)
        .orElseThrow(() -> new InitiativeException(
            InitiativeConstants.Exception.NotFound.CODE,
            String.format(
                InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE,
                initiativeId),
            HttpStatus.NOT_FOUND));
    String userId = null;
    if (CF != null) {
      try {
        EncryptedCfDTO encryptedCfDTO = encryptRestConnector.upsertToken(
            new CFDTO(CF));
        userId = encryptedCfDTO.getToken();
      } catch (Exception e) {
        throw new InitiativeException(
            InternalServerError.CODE,
            e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    ResponseOnboardingDTO responseOnboardingDTO = new ResponseOnboardingDTO();
    try {
      responseOnboardingDTO = onboardingRestConnector.getOnboarding(initiativeId, pageable,
          userId,
          startDate, endDate, status);
      log.info("response onbording: "+responseOnboardingDTO);
    } catch (FeignException e) {
      throw new InitiativeException(
          InternalServerError.CODE,
          e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    List<StatusOnboardingDTO> statusOnboardingDTOS = new ArrayList<>();
    for (OnboardingStatusCitizenDTO onboardingStatusCitizenDTO : responseOnboardingDTO.getOnboardingStatusCitizenDTOList()) {
      try {
        DecryptCfDTO decryptedCfDTO = decryptRestConnector.getPiiByToken(
            onboardingStatusCitizenDTO.getUserId());
        StatusOnboardingDTO statusOnboardingDTO = new StatusOnboardingDTO(decryptedCfDTO.getPii(),
            onboardingStatusCitizenDTO.getStatus(), onboardingStatusCitizenDTO.getStatusDate());
        statusOnboardingDTOS.add(statusOnboardingDTO);

      } catch (FeignException e) {
        throw new InitiativeException(
            InternalServerError.CODE,
            e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return new OnboardingDTO(statusOnboardingDTOS, responseOnboardingDTO.getPageNo(),
        responseOnboardingDTO.getPageSize(), responseOnboardingDTO.getTotalElements(),
        responseOnboardingDTO.getTotalPages());
  }
}
