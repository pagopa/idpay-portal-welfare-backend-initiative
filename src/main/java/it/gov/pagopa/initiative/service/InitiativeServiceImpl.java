package it.gov.pagopa.initiative.service;

import com.google.common.io.Files;
import feign.FeignException;
import it.gov.pagopa.initiative.connector.decrypt.DecryptRestConnector;
import it.gov.pagopa.initiative.connector.encrypt.EncryptRestConnector;
import it.gov.pagopa.initiative.connector.file_storage.FileStorageConnector;
import it.gov.pagopa.initiative.connector.group.GroupRestConnector;
import it.gov.pagopa.initiative.connector.io_service.IOBackEndRestConnector;
import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import it.gov.pagopa.initiative.event.InitiativeProducer;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeAdditionalDTOsToIOServiceRequestDTOMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.AutomatedCriteria;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import it.gov.pagopa.initiative.utils.InitiativeUtils;
import it.gov.pagopa.initiative.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Email.*;


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
    private final FileStorageConnector fileStorageConnector;
    private final EmailNotificationService emailNotificationService;
    private final IOTokenService ioTokenService;
    private final InitiativeValidationService initiativeValidationService;
    private final Set<String> allowedInitiativeLogoMimeTypes;
    private final Set<String> allowedInitiativeLogoExtensions;
    private final InitiativeUtils initiativeUtils;
    private final Utilities utilities;

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
            FileStorageConnector fileStorageConnector,
            @Value("${app.initiative.logo.allowed-mime-types}") String[] allowedInitiativeLogoMimeTypes,
            @Value("${app.initiative.logo.allowed-extensions}") String[] allowedInitiativeLogoExtensions,
            EmailNotificationService emailNotificationService,
            IOTokenService ioTokenService,
            InitiativeValidationService initiativeValidationService,
            InitiativeUtils initiativeUtils,
            Utilities utilities){
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
        this.fileStorageConnector = fileStorageConnector;
        this.allowedInitiativeLogoMimeTypes = Set.of(allowedInitiativeLogoMimeTypes);
        this.allowedInitiativeLogoExtensions = Set.of(allowedInitiativeLogoExtensions);
        this.emailNotificationService = emailNotificationService;
        this.ioTokenService = ioTokenService;
        this.initiativeValidationService = initiativeValidationService;
        this.initiativeUtils = initiativeUtils;
        this.utilities = utilities;
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
            emailNotificationService.sendInitiativeToCurrentOrganization(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_CREATED, SUBJECT_INITIATIVE_CREATED);
        }
        utilities.newInitiative(this.getUserId(), initiative.getInitiativeId());
        return initiativeReturned;
    }

    @Override
    public Initiative getInitiative(String organizationId, String initiativeId, String role) {
        utilities.getIniziative(this.getUserId(), initiativeId);
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
        if (initiativeInfoModel.getGeneral().getDescriptionMap().get(Locale.ITALIAN.getLanguage()) == null) {
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    InitiativeConstants.Exception.BadRequest.INITIATIVE_DESCRIPTION_LANGUAGE_MESSAGE,
                    HttpStatus.BAD_REQUEST);
        }
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setGeneral(initiativeInfoModel.getGeneral());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        InitiativeAdditional infoOriginal = initiative.getAdditionalInfo();
        InitiativeAdditional infoNew =  initiativeAdditionalInfo.getAdditionalInfo();
        BeanUtils.copyProperties(infoNew, infoOriginal, "logoFileName", "logoUploadDate", "serviceId", "primaryTokenIO", "secondaryTokenIO");
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateStep3InitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule initiativeBeneficiaryRuleModel, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        List<AutomatedCriteria> automatedCriteriaList = initiativeBeneficiaryRuleModel.getAutomatedCriteria();
        initiativeValidationService.checkAutomatedCriteriaOrderDirectionWithRanking(initiative, automatedCriteriaList);
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
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        initiative.setRewardRule(rewardAndTrxRules.getRewardRule());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateInitiativeRefundRules(String organizationId, String initiativeId, String role, Initiative refundRule, boolean changeInitiativeStatus){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(initiative);
        //initiativeValidationService.validateAllWizardSteps(initiativeDTO);
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setRefundRule(refundRule.getRefundRule());
        log.info("[UPDATE_REFUND_RULE] - Initiative: {}. Refund rules successfully set.", initiativeId);
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        if (changeInitiativeStatus) {
            initiative.setStatus(InitiativeConstants.Status.IN_REVISION);
            utilities.initiativeInRevision(this.getUserId(),initiativeId);
            log.info("[UPDATE_TO_IN_REVISION_STATUS] - Initiative: {}. Status successfully set to IN_REVISION.", initiativeId);
        }
        this.initiativeRepository.save(initiative);
        if(changeInitiativeStatus && notifyEmail){
            try {
                emailNotificationService.sendInitiativeToCurrentOrganization(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
                emailNotificationService.sendInitiativeToPagoPA(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
            } catch (FeignException e) {
                log.error("[UPDATE_REFUND_RULE]-[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
            }
        }
    }

    @Override
    public void updateInitiativeApprovedStatus(String organizationId, String initiativeId, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.APPROVED);
        initiative.setStatus(InitiativeConstants.Status.APPROVED);
        this.initiativeRepository.save(initiative);
        utilities.initiativeApproved(this.getUserId(),initiativeId);
        log.info("[UPDATE_TO_APPROVED_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        if(notifyEmail){
            try {
                emailNotificationService.sendInitiativeToCurrentOrganization(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
            } catch (FeignException e) {
                log.error("[UPDATE_TO_APPROVED_STATUS]-[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
            }
        }
    }

    @Override
    public void updateInitiativeToCheckStatus(String organizationId, String initiativeId, String role){
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.TO_CHECK);
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        this.initiativeRepository.save(initiative);
        utilities.initiativeToCheck(this.getUserId(), initiativeId);
        log.info("[UPDATE_TO_CHECK_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        if(notifyEmail){
            try {
                emailNotificationService.sendInitiativeToCurrentOrganization(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
            } catch (FeignException e) {
                log.error("[UPDATE_TO_CHECK_STATUS]-[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
            }
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
            this.initiativeRepository.save(initiative);
            log.info("[LOGICAL_DELETE_INITIATIVE] - Initiative: {}. Successfully logical elimination.", initiative.getInitiativeId());
        }
        if(notifyEmail){
            try {
                emailNotificationService.sendInitiativeToPagoPA(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
            } catch (FeignException e) {
                log.error("[LOGICAL_DELETE_INITIATIVE]-[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
            }
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
    public LogoDTO storeInitiativeLogo(String organizationId, String initiativeId, InputStream logo,
            String contentType, String fileName) {

        Initiative initiative = initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(
                        organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(
                                InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE,
                                initiativeId),
                        HttpStatus.NOT_FOUND));

        try {
            this.validate(contentType,fileName);
            fileStorageConnector.uploadInitiativeLogo(logo, String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE, organizationId, initiativeId, InitiativeConstants.Logo.LOGO_NAME), contentType);
            initiative.getAdditionalInfo().setLogoFileName(fileName);
            LocalDateTime localDateTime = LocalDateTime.now();
            initiative.getAdditionalInfo().setLogoUploadDate(localDateTime);
            initiative.setUpdateDate(localDateTime);
            initiativeRepository.save(initiative);
            return new LogoDTO(fileName, initiativeUtils.createLogoUrl(organizationId,initiativeId),localDateTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Initiative sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(Initiative initiative, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO) {
        InitiativeAdditional additionalInfo = initiative.getAdditionalInfo();
        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(additionalInfo, initiativeOrganizationInfoDTO);
        ServiceResponseDTO serviceResponseDTO = ioBackEndRestConnector.createService(serviceRequestDTO);

        try{
            ByteArrayOutputStream byteArrayOutputStream = fileStorageConnector.downloadInitiativeLogo(initiativeUtils.getPathLogo(initiative.getOrganizationId(),initiative.getInitiativeId()));
            ioBackEndRestConnector.sendLogoIo(serviceResponseDTO.getServiceId(),serviceResponseDTO.getPrimaryKey(), LogoIODTO.builder().logo(new String (Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()))).build());
        }catch(Exception e){
            log.error("[UPLOAD_LOGO] - Initiative: {}. Error: "+e.getMessage(), initiative.getInitiativeId());
        }
        log.debug("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Start ServiceIO Keys encryption...", initiative.getInitiativeId());
        String encryptedPrimaryToken = ioTokenService.encrypt(serviceResponseDTO.getPrimaryKey());
        String encryptedSecondaryToken = ioTokenService.encrypt(serviceResponseDTO.getSecondaryKey());
        log.debug("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Encryption completed.", initiative.getInitiativeId());
        initiative.getAdditionalInfo().setPrimaryTokenIO(encryptedPrimaryToken);
        initiative.getAdditionalInfo().setSecondaryTokenIO(encryptedSecondaryToken);
        additionalInfo.setServiceId(serviceResponseDTO.getServiceId());
        utilities.initiativePublished(this.getUserId(),initiative.getInitiativeId());
        if(notifyEmail){
            try {
                emailNotificationService.sendInitiativeToCurrentOrganization(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
                emailNotificationService.sendInitiativeToPagoPA(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
            } catch (FeignException e) {
                log.error("[UPDATE_TO_PUBLISHED_STATUS]-[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
            }
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
    utilities.onboardingCitizen(this.getUserId(), initiativeId);
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
    } catch (Exception e) {
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

      } catch (Exception e) {
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

    private void validate(String contentType, String fileName) {
        Assert.notNull(fileName, "file name cannot be null");
        if (!allowedInitiativeLogoMimeTypes.contains(contentType)) {
            throw new InvalidMimeTypeException(contentType, String.format("allowed only %s",
                    allowedInitiativeLogoMimeTypes));
        }

        String fileExtension = Files.getFileExtension(fileName).toLowerCase();
        if (!allowedInitiativeLogoExtensions.contains(fileExtension)) {
            throw new IllegalArgumentException(String.format("Invalid file extension \"%s\": allowed only %s", fileExtension,
                    allowedInitiativeLogoExtensions));
        }
    }

    private String getUserId(){
        RequestAttributes requestAttributes =RequestContextHolder.getRequestAttributes();
        return (String) requestAttributes.getAttribute("organizationUserId", RequestAttributes.SCOPE_REQUEST);
    }
}
