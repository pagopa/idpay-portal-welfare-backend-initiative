package it.gov.pagopa.initiative.service;

import com.google.common.io.Files;
import feign.FeignException;
import it.gov.pagopa.common.web.exception.ClientExceptionWithBody;
import it.gov.pagopa.initiative.connector.decrypt.DecryptRestConnector;
import it.gov.pagopa.initiative.connector.encrypt.EncryptRestConnector;
import it.gov.pagopa.initiative.connector.file_storage.FileStorageConnector;
import it.gov.pagopa.initiative.connector.group.GroupRestConnector;
import it.gov.pagopa.initiative.connector.io_service.IOManageBackEndRestConnector;
import it.gov.pagopa.initiative.connector.onboarding.OnboardingRestConnector;
import it.gov.pagopa.initiative.connector.ranking.RankingRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Status;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.io.service.KeysDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import it.gov.pagopa.initiative.event.CommandsProducer;
import it.gov.pagopa.initiative.event.InitiativeProducer;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeAdditionalDTOsToIOServiceRequestDTOMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.AutomatedCriteria;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import it.gov.pagopa.initiative.utils.AuditUtilities;
import it.gov.pagopa.initiative.utils.InitiativeUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Email.*;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest.INITIATIVE_CANNOT_BE_PUBLISHED_BC_FINISHED;


@Service
@Slf4j
public class InitiativeServiceImpl extends InitiativeServiceRoot implements InitiativeService {

    private final boolean notifyEmail;
    private final InitiativeRepository initiativeRepository;
    private final InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;
    private final InitiativeProducer initiativeProducer;
    private final CommandsProducer commandsProducer;
    private final IOManageBackEndRestConnector ioManageBackEndRestConnector;
    private final GroupRestConnector groupRestConnector;
    private final OnboardingRestConnector onboardingRestConnector;
    private final RankingRestConnector rankingRestConnector;
    private final EncryptRestConnector encryptRestConnector;
    private final DecryptRestConnector decryptRestConnector;
    private final FileStorageConnector fileStorageConnector;
    private final EmailNotificationService emailNotificationService;
    private final InitiativeValidationService initiativeValidationService;
    private final InitiativeUtils initiativeUtils;
    private final AuditUtilities auditUtilities;

    private final InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    private static final String DELETE_INITIATIVE_SERVICE = "DELETE_INITIATIVE";
    private static final String DELETE_INITIATIVE_OPERATION_TYPE = "DELETE_INITIATIVE";
    private static final String CREATE_STATISTICS_OPERATION_TYPE = "CREATE_INITIATIVE_STATISTICS";

    public InitiativeServiceImpl(
            @Value("${app.initiative.conditions.notifyEmail}") boolean notifyEmail,
            InitiativeRepository initiativeRepository,
            InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper,
            InitiativeProducer initiativeProducer,
            GroupRestConnector groupRestConnector,
            OnboardingRestConnector onboardingRestConnector,
            RankingRestConnector rankingRestConnector, EncryptRestConnector encryptRestConnector,
            DecryptRestConnector decryptRestConnector,
            FileStorageConnector fileStorageConnector,
            EmailNotificationService emailNotificationService,
            InitiativeValidationService initiativeValidationService,
            InitiativeUtils initiativeUtils,
            AuditUtilities auditUtilities,
            InitiativeModelToDTOMapper initiativeModelToDTOMapper,
            CommandsProducer commandsProducer, IOManageBackEndRestConnector ioManageBackEndRestConnector){
        this.notifyEmail = notifyEmail;
        this.initiativeRepository = initiativeRepository;
        this.initiativeProducer = initiativeProducer;
        this.initiativeAdditionalDTOsToIOServiceRequestDTOMapper = initiativeAdditionalDTOsToIOServiceRequestDTOMapper;
        this.groupRestConnector = groupRestConnector;
        this.onboardingRestConnector = onboardingRestConnector;
        this.rankingRestConnector = rankingRestConnector;
        this.encryptRestConnector = encryptRestConnector;
        this.decryptRestConnector = decryptRestConnector;
        this.fileStorageConnector = fileStorageConnector;
        this.emailNotificationService = emailNotificationService;
        this.initiativeValidationService = initiativeValidationService;
        this.initiativeUtils = initiativeUtils;
        this.auditUtilities = auditUtilities;
        this.initiativeModelToDTOMapper = initiativeModelToDTOMapper;
        this.commandsProducer = commandsProducer;
        this.ioManageBackEndRestConnector = ioManageBackEndRestConnector;
    }

    public List<Initiative> retrieveInitiativeSummary(String organizationId, String role) {
        List<Initiative> initiatives = initiativeRepository.retrieveInitiativeSummary(organizationId, true);
        return InitiativeConstants.Role.PAGOPA_ADMIN.equals(role) ? initiatives.stream().filter(
                        initiative -> (
                                initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION) ||
                                        initiative.getStatus().equals(InitiativeConstants.Status.TO_CHECK) ||
                                        initiative.getStatus().equals(InitiativeConstants.Status.APPROVED) ||
                                        initiative.getStatus().equals(InitiativeConstants.Status.PUBLISHED)))
                .toList() : initiatives;
    }

    public List<Initiative> getPublishedInitiativesList() {
        return initiativeRepository.findByEnabledAndStatus(true, Status.PUBLISHED);
    }
    @Override
    public InitiativeDetailDTO getInitiativeBeneficiaryDetail(String initiativeId, Locale acceptLanguage) {

        Initiative initiativeDetail = initiativeRepository.findByInitiativeIdAndStatusIn(initiativeId, List.of(Status.PUBLISHED, Status.CLOSED)).orElseThrow(() -> new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                HttpStatus.NOT_FOUND));


        return initiativeModelToDTOMapper.toInitiativeDetailDTO(initiativeDetail, acceptLanguage);

    }

    @Override
    public Initiative insertInitiative(Initiative initiative, String organizationId, String organizationName, String role) {
        long startTime = System.currentTimeMillis();
        initiativeValidationService.checkPermissionBeforeInsert(role);
        initiative.setOrganizationId(organizationId);
        initiative.setOrganizationName(organizationName);
        insertTechnicalData(initiative);
        if (StringUtils.isBlank(initiative.getStatus())) {
            initiative.setStatus(InitiativeConstants.Status.DRAFT);
        }
        Initiative initiativeReturned = initiativeRepository.insert(initiative);
        this.sendEmailToCurrentOrg(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_CREATED, SUBJECT_INITIATIVE_CREATED);
        auditUtilities.logNewInitiative(this.getUserId(), initiative.getInitiativeId(), organizationId);
        performanceLog(startTime, "CREATE_INITIATIVE");
        return initiativeReturned;
    }

    @Override
    public Initiative getInitiative(String organizationId, String initiativeId, String role) {
        long startTime = System.currentTimeMillis();
        auditUtilities.logGetInitiative(this.getUserId(), initiativeId, organizationId);
        performanceLog(startTime, "GET_INITIATIVE_DETAIL");
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
    public void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel, String role, boolean isDraft) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);

        isInitiativeAllowedToBeEditableThenThrows(initiative);

        if (!isDraft) {
            initiativeValidationService.checkBeneficiaryTypeAndFamilyUnit(initiativeInfoModel);
            initiativeValidationService.checkStartDateAndEndDate(initiativeInfoModel);
        }
        initiative.setGeneral(initiativeInfoModel.getGeneral());
        if (!initiative.getAdditionalInfo().getServiceName().equals(initiative.getInitiativeName())) {
            initiative.setInitiativeName(initiative.getAdditionalInfo().getServiceName());
        }
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
        auditUtilities.logEditInitiative(this.getUserId(), initiativeId, organizationId);
        performanceLog(startTime, "UPDATE_INITIATIVE_GENERAL_INFO");
    }

    @Override
    public void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo, String role) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        InitiativeAdditional infoOriginal = initiative.getAdditionalInfo();
        InitiativeAdditional infoNew = initiativeAdditionalInfo.getAdditionalInfo();
        BeanUtils.copyProperties(infoNew, infoOriginal, "logoFileName", "logoUploadDate", "serviceId", "primaryTokenIO", "secondaryTokenIO");
        initiative.setInitiativeName(infoNew.getServiceName());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
        auditUtilities.logEditInitiative(this.getUserId(), initiativeId, organizationId);
        performanceLog(startTime, "UPDATE_INITIATIVE_ADDITIONAL_INFO");
    }

    @Override
    public void updateStep3InitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule initiativeBeneficiaryRuleModel, String role, boolean isDraft) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        List<AutomatedCriteria> automatedCriteriaList = initiativeBeneficiaryRuleModel.getAutomatedCriteria();
        if (!isDraft) {
            initiativeValidationService.checkFieldYearLengthAndValues(automatedCriteriaList);
            initiativeValidationService.checkAutomatedCriteria(initiative, automatedCriteriaList);
        }
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setBeneficiaryRule(initiativeBeneficiaryRuleModel);
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
        auditUtilities.logEditInitiative(this.getUserId(), initiativeId, organizationId);
        performanceLog(startTime, "UPDATE_INITIATIVE_BENEFICIARY_RULES");
    }

    @Override
    public void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules, String role, boolean isDraft) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        if (!isDraft) {
            initiativeValidationService.checkReward(rewardAndTrxRules);
            initiativeValidationService.checkRewardRuleAbsolute(rewardAndTrxRules);
        }
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setTrxRule(rewardAndTrxRules.getTrxRule());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        initiative.setRewardRule(rewardAndTrxRules.getRewardRule());
        initiative.setInitiativeRewardType(rewardAndTrxRules.getInitiativeRewardType());
        this.initiativeRepository.save(initiative);
        auditUtilities.logEditInitiative(this.getUserId(), initiativeId, organizationId);
        performanceLog(startTime, "UPDATE_INITIATIVE_TRX_REWARD_RULES");
    }


    @Override
    public void updateInitiativeRefundRules(String organizationId, String initiativeId, String role, Initiative refundRule, boolean changeInitiativeStatus) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        log.info("[UPDATE_REFUND_RULE] - Initiative: {}. Refund rules successfully set.", initiativeId);
        if (changeInitiativeStatus) {
            initiativeValidationService.checkRefundRuleDiscountInitiative(initiative.getInitiativeRewardType().name(), refundRule.getRefundRule());
            //Insert [All Steps validation -> validateAllWizardSteps with @Validated(value = ValidationOnGroup.class)]
            //Move this validation in [All Steps validation -> .validateAllWizardSteps()]
            if (initiative.getGeneral().getDescriptionMap().get(Locale.ITALIAN.getLanguage()) == null) {
                auditUtilities.logInitiativeError(this.getUserId(), initiativeId, organizationId,
                        InitiativeConstants.Exception.BadRequest.INITIATIVE_DESCRIPTION_LANGUAGE_MESSAGE);
                performanceLog(startTime, "UPDATE_INITIATIVE_REFUND_RULES");
                throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        InitiativeConstants.Exception.BadRequest.INITIATIVE_DESCRIPTION_LANGUAGE_MESSAGE,
                        HttpStatus.BAD_REQUEST);
            }
            initiative.setStatus(InitiativeConstants.Status.IN_REVISION);
            auditUtilities.logInitiativeInRevision(this.getUserId(), initiativeId, organizationId);
            if (Boolean.TRUE.equals(initiative.getGeneral().getBeneficiaryKnown())) {
                groupRestConnector.setGroupStatusToValidated(initiativeId);
            }
            log.info("[UPDATE_TO_IN_REVISION_STATUS] - Initiative: {}. Status successfully set to IN_REVISION.", initiativeId);
        } else {
            initiative.setStatus(InitiativeConstants.Status.DRAFT);
            auditUtilities.logEditInitiative(this.getUserId(), initiativeId, organizationId);
        }
        initiative.setRefundRule(refundRule.getRefundRule());
        this.initiativeRepository.save(initiative);
        if (changeInitiativeStatus) {
            this.sendEmailToPagoPA(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
            this.sendEmailToCurrentOrg(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
        }
        performanceLog(startTime, "UPDATE_INITIATIVE_REFUND_RULES");
    }


    @Override
    public void updateInitiativeApprovedStatus(String organizationId, String initiativeId, String role) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.APPROVED);
        initiative.setStatus(InitiativeConstants.Status.APPROVED);
        this.initiativeRepository.save(initiative);
        auditUtilities.logInitiativeApproved(this.getUserId(), initiativeId, organizationId);
        log.info("[UPDATE_TO_APPROVED_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        this.sendEmailToCurrentOrg(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
        performanceLog(startTime, "UPDATE_INITIATIVE_APPROVED");
    }

    @Override
    public void updateInitiativeToCheckStatus(String organizationId, String initiativeId, String role) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.TO_CHECK);
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        this.initiativeRepository.save(initiative);
        auditUtilities.logInitiativeToCheck(this.getUserId(), initiativeId, organizationId);
        log.info("[UPDATE_TO_CHECK_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        this.sendEmailToCurrentOrg(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
        performanceLog(startTime, "UPDATE_INITIATIVE_TO_CHECK");
    }

    @Override
    public void logicallyDeleteInitiative(String organizationId, String initiativeId, String role) {
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeValidationService.getInitiative(organizationId, initiativeId, role);
        if (List.of(InitiativeConstants.Status.IN_REVISION, InitiativeConstants.Status.PUBLISHED,
                InitiativeConstants.Status.CLOSED, InitiativeConstants.Status.SUSPENDED).contains(initiative.getStatus())) {
            log.error("[LOGICAL_DELETE_INITIATIVE] - Initiative: {}. Cannot be deleted. Current status is {}.", initiative.getInitiativeId(), initiative.getStatus());
            auditUtilities.logInitiativeError(this.getUserId(), initiativeId, organizationId, "initiative cannot be deleted");
            performanceLog(startTime, DELETE_INITIATIVE_SERVICE);
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_CANNOT_BE_DELETED, initiativeId),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            initiative.setEnabled(false);
            this.initiativeRepository.save(initiative);
            auditUtilities.logInitiativeDeleted(this.getUserId(), initiativeId, organizationId);
            log.info("[LOGICAL_DELETE_INITIATIVE] - Initiative: {}. Successfully logical elimination.", initiative.getInitiativeId());
        }
        this.sendEmailToPagoPA(initiative, TEMPLATE_NAME_EMAIL_INITIATIVE_STATUS, SUBJECT_CHANGE_STATE);
        performanceLog(startTime, DELETE_INITIATIVE_SERVICE);
    }

    @Override
    public void sendInitiativeInfoToRuleEngine(Initiative initiative) {
        if (!initiativeProducer.sendPublishInitiative(initiative)) {
            throw new IllegalStateException("[UPDATE_TO_PUBLISHED_STATUS] - Something gone wrong while notify Initiative to RuleEngine");
        }
    }

    @Override
    public void sendInitiativeInfoToNotificationManager(Initiative initiative) {
        groupRestConnector.notifyInitiativeToGroup(initiative);
    }

    @Override
    public void isInitiativeAllowedToBeNextStatusThenThrows(Initiative initiative, String nextStatus, String role) {
        //validation role
        if (InitiativeConstants.Role.PAGOPA_ADMIN.equals(role)) {
            log.info("[UPDATE_TO_{}_STATUS] - Initiative: {} Status: {}. Not processable status", nextStatus, initiative.getInitiativeId(), initiative.getStatus());
            auditUtilities.logInitiativeError(this.getUserId(), initiative.getInitiativeId(), initiative.getOrganizationId(),
                    String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role));
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role),
                    HttpStatus.BAD_REQUEST);
        }

        //validation status
        if (InitiativeConstants.Status.PUBLISHED.equals(nextStatus)) {
            if (!Arrays.asList(InitiativeConstants.Status.Validation.INITIATIVE_ALLOWED_STATES_TO_BECOME_PUBLISHED_ARRAY).contains(initiative.getStatus())) {
                log.info("[UPDATE_TO_{}_STATUS] - Initiative: {} Status: {}. Not processable status", nextStatus, initiative.getInitiativeId(), initiative.getStatus());
                auditUtilities.logInitiativeError(this.getUserId(), initiative.getInitiativeId(), initiative.getOrganizationId(),
                        String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()));
                throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            auditUtilities.logInitiativeError(this.getUserId(), initiative.getInitiativeId(), initiative.getOrganizationId(),
                    String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()));
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()),
                    HttpStatus.BAD_REQUEST);
        }

        //validation end date
        LocalDate initiativeEndDate = initiative.getGeneral().getEndDate();
        if(LocalDate.now().isAfter(initiativeEndDate)){
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(INITIATIVE_CANNOT_BE_PUBLISHED_BC_FINISHED, initiative.getInitiativeId(), initiativeEndDate),
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
        long startTime = System.currentTimeMillis();
        Initiative initiative = initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(
                        organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(
                                InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE,
                                initiativeId),
                        HttpStatus.NOT_FOUND));

        try {
            this.validate(contentType, fileName);
            fileStorageConnector.uploadInitiativeLogo(logo, String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE, organizationId, initiativeId, InitiativeConstants.Logo.LOGO_NAME), contentType);
            initiative.getAdditionalInfo().setLogoFileName(fileName);
            LocalDateTime localDateTime = LocalDateTime.now();
            initiative.getAdditionalInfo().setLogoUploadDate(localDateTime);
            initiative.setUpdateDate(localDateTime);
            initiativeRepository.save(initiative);
            performanceLog(startTime, "STORE_INITIATIVE_LOGO");
            return new LogoDTO(fileName, initiativeUtils.createLogoUrl(organizationId, initiativeId), localDateTime);
        } catch (Exception e) {
            performanceLog(startTime, "STORE_INITIATIVE_LOGO");
            throw new InitiativeException(InternalServerError.CODE,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Initiative sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(Initiative initiative, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO) {
        InitiativeAdditional additionalInfo = initiative.getAdditionalInfo();
        String serviceId = additionalInfo.getServiceId();

        ServiceRequestDTO serviceRequestDTO = initiativeAdditionalDTOsToIOServiceRequestDTOMapper.toServiceRequestDTO(additionalInfo, initiativeOrganizationInfoDTO);

        if (StringUtils.isBlank(serviceId)) {
            ServiceResponseDTO serviceResponseDTO = ioManageBackEndRestConnector.createService(serviceRequestDTO);
            serviceId = serviceResponseDTO.getId();
            log.info("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Created new service to ServiceIO", initiative.getInitiativeId());
            additionalInfo.setServiceId(serviceId);
            this.updateInitiative(initiative);
        }

        if (additionalInfo.getLogoFileName() != null) {
            try {
                log.info("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Update logo to ServiceIO", initiative.getInitiativeId());
                ByteArrayOutputStream byteArrayOutputStream = fileStorageConnector.downloadInitiativeLogo(
                        initiativeUtils.getPathLogo(initiative.getOrganizationId(),
                                initiative.getInitiativeId()));
                ioManageBackEndRestConnector.sendLogoIo(serviceId, LogoIODTO.builder().logo(new String(
                                Base64.getEncoder().encode(byteArrayOutputStream.toByteArray())))
                        .build());
            } catch (Exception e) {
                auditUtilities.logInitiativeError(this.getUserId(), initiative.getInitiativeId(), initiative.getOrganizationId(), "upload logo failed");
                log.error("[UPLOAD_LOGO] - Initiative: {}. Error: " + e.getMessage(),
                        initiative.getInitiativeId(), e);
            }
        }

        log.info("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Update CTA to ServiceIO", initiative.getInitiativeId());
        serviceRequestDTO.getServiceMetadata().setCta(
                InitiativeConstants.CtaConstant.START +
                        InitiativeConstants.CtaConstant.IT + InitiativeConstants.CtaConstant.CTA_1_IT + InitiativeConstants.CtaConstant.TEXT_IT + InitiativeConstants.CtaConstant.ACTION_IT + serviceId +
                        InitiativeConstants.CtaConstant.EN + InitiativeConstants.CtaConstant.CTA_1_EN + InitiativeConstants.CtaConstant.TEXT_EN + InitiativeConstants.CtaConstant.ACTION_EN + serviceId +
                        InitiativeConstants.CtaConstant.END
        );

        try{
            ioManageBackEndRestConnector.updateService(serviceId, serviceRequestDTO);
        } catch (Exception e) {
            log.error("[UPLOAD_SERVICE_IO] - Initiative: {}. Error: " + e.getMessage(),
                    initiative.getInitiativeId(), e);
        }

        auditUtilities.logInitiativePublished(this.getUserId(), initiative.getInitiativeId(), initiative.getOrganizationId());
        return initiative;
    }

    @Override
    public void sendEmailToCurrentOrg(Initiative initiative, String template,  String subject){
        if(notifyEmail){
            try {
                emailNotificationService.sendInitiativeToCurrentOrganization(initiative, template, subject);
            } catch (FeignException e) {
                log.error("[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
            }
        }
    }

    @Override
    public void sendEmailToPagoPA(Initiative initiative, String template,  String subject){
        if(notifyEmail){
            try {
                emailNotificationService.sendInitiativeToPagoPA(initiative, template, subject);
            } catch (FeignException e) {
                log.error("[EMAIL-NOTIFICATION] Message: {}", e.getMessage());
            }
        }
    }


    @Override
    public Initiative getInitiativeIdFromServiceId(String serviceId) {
        return initiativeRepository.retrieveByServiceId(serviceId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_ID_BY_SERVICE_ID_MESSAGE, serviceId),
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public InitiativeAdditional getPrimaryAndSecondaryTokenIO(String initiativeId) {
        Initiative initiative = initiativeRepository.findByInitiativeIdAndEnabled(initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.PRIMARY_AND_SECONDARY_TOKEN_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        return initiative.getAdditionalInfo();
    }

    @Override
    public OnboardingDTO getOnboardingStatusList(String organizationId, String initiativeId, String cf,
                                                 LocalDateTime startDate, LocalDateTime endDate, String status, Pageable pageable) {

        log.info("start get status onboarding, initiative: " + initiativeId);

        String userId = null;
        if (cf != null) {
            cf = cf.toUpperCase();
            try {
                EncryptedCfDTO encryptedCfDTO = encryptRestConnector.upsertToken(
                        new CFDTO(cf));
                userId = encryptedCfDTO.getToken();
            } catch (Exception e) {
                throw new InitiativeException(
                        InternalServerError.CODE,
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        new ResponseOnboardingDTO();
        ResponseOnboardingDTO responseOnboardingDTO;
        try {
            responseOnboardingDTO = onboardingRestConnector.getOnboarding(initiativeId, pageable,
                    userId,
                    startDate, endDate, status);
            log.info("response onbording: " + responseOnboardingDTO);
        } catch (Exception e) {
            auditUtilities.logInitiativeError(this.getUserId(), initiativeId, organizationId, "request for onboarding list failed");
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
                        onboardingStatusCitizenDTO.getStatus(), onboardingStatusCitizenDTO.getStatusDate(),
                        onboardingStatusCitizenDTO.getFamilyId());
                statusOnboardingDTOS.add(statusOnboardingDTO);

            } catch (Exception e) {
                throw new InitiativeException(
                        InternalServerError.CODE,
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        auditUtilities.logOnboardingCitizen(this.getUserId(), initiativeId, organizationId);
        return new OnboardingDTO(statusOnboardingDTOS, responseOnboardingDTO.getPageNo(),
                responseOnboardingDTO.getPageSize(), responseOnboardingDTO.getTotalElements(),
                responseOnboardingDTO.getTotalPages());
    }

    @Override
    public BeneficiaryRankingPageDTO getRankingList(String organizationId, String initiativeId,
                                                    Pageable pageable, String beneficiary, String state) {
        log.info("start get ranking, initiative: " + initiativeId);
        Initiative initiative = initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(
                        organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(
                                InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE,
                                initiativeId),
                        HttpStatus.NOT_FOUND));
        if (initiative.getGeneral() != null && !initiative.getGeneral().getRankingEnabled()) {
            throw new InitiativeException(
                    InternalServerError.CODE,
                    InternalServerError.NO_RANKING,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String userId = null;
        if (beneficiary != null) {
            try {
                EncryptedCfDTO encryptedCfDTO = encryptRestConnector.upsertToken(
                        new CFDTO(beneficiary));
                userId = encryptedCfDTO.getToken();
            } catch (Exception e) {
                throw new InitiativeException(
                        InternalServerError.CODE,
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        RankingPageDTO rankingPageDTO;
        try {
            rankingPageDTO = rankingRestConnector.getRankingList(organizationId, initiativeId, pageable, state, userId);
            log.info("response ranking: " + rankingPageDTO);
        } catch (Exception e) {
            auditUtilities.logInitiativeError(this.getUserId(), initiativeId, organizationId, "request for ranking list failed");
            throw new InitiativeException(
                    InternalServerError.CODE,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<BeneficiaryRankingDTO> beneficiaryRankingDTOS = new ArrayList<>();
        for (RankingRequestDTO rankingRequestDTO : rankingPageDTO.getContent()) {
            try {
                DecryptCfDTO decryptedCfDTO = decryptRestConnector.getPiiByToken(
                        rankingRequestDTO.getUserId());
                BeneficiaryRankingDTO beneficiaryRankingDTO = BeneficiaryRankingDTO.builder()
                        .beneficiary(decryptedCfDTO.getPii())
                        .criteriaConsensusTimestamp(rankingRequestDTO.getCriteriaConsensusTimestamp())
                        .rankingValue(rankingRequestDTO.getRankingValue())
                        .ranking(rankingRequestDTO.getRanking())
                        .beneficiaryRankingStatus(rankingRequestDTO.getBeneficiaryRankingStatus())
                        .build();
                if(StringUtils.isNotBlank(rankingRequestDTO.getFamilyId())){
                    beneficiaryRankingDTO.setFamilyId(rankingRequestDTO.getFamilyId());
                    beneficiaryRankingDTO.setMemberIds(rankingRequestDTO.getMemberIds().stream()
                            .map(member -> decryptRestConnector.getPiiByToken(member).getPii()).toList());
                }
                beneficiaryRankingDTOS.add(beneficiaryRankingDTO);

            } catch (Exception e) {
                throw new InitiativeException(
                        InternalServerError.CODE,
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        auditUtilities.logDetailUser(this.getUserId(), initiativeId, organizationId);
        return new BeneficiaryRankingPageDTO(beneficiaryRankingDTOS, rankingPageDTO.getPageNumber(),
                rankingPageDTO.getPageSize(), rankingPageDTO.getTotalElements(),
                rankingPageDTO.getTotalPages(), rankingPageDTO.getRankingStatus(), rankingPageDTO.getRankingPublishedTimestamp(), rankingPageDTO.getRankingGeneratedTimestamp(), rankingPageDTO.getTotalEligibleOk(), rankingPageDTO.getTotalEligibleKo(), rankingPageDTO.getTotalOnboardingKo(),
                rankingPageDTO.getRankingFilePath());
    }

    @Override
    public void deleteInitiative(String initiativeId){
        long startTime = System.currentTimeMillis();

        try{
            Optional<Initiative> initiative = initiativeRepository.findById(initiativeId);
            if (initiative.isPresent() && initiative.get().getAdditionalInfo() != null &&
                    initiative.get().getAdditionalInfo().getServiceId() !=null) {
                ioManageBackEndRestConnector.deleteService(initiative.get().getAdditionalInfo().getServiceId());
                log.info("[DELETE_INITIATIVE] - Service deleted for initiative: {}", initiativeId);
            }
        } catch (Exception e){
            log.info("[DELETE_INITIATIVE] - Error while deleting service - Initiative: {}. Error: " + e.getMessage(),
                    initiativeId, e);
        }

        QueueCommandOperationDTO deleteInitiativeCommand = QueueCommandOperationDTO.builder()
                .entityId(initiativeId)
                .operationType(DELETE_INITIATIVE_OPERATION_TYPE)
                .operationTime(LocalDateTime.now())
                .build();
        if(!commandsProducer.sendCommand(deleteInitiativeCommand)){
            log.error("[DELETE_INITIATIVE] - Initiative: {}. Something went wrong while sending the message on Commands Queue", initiativeId);
            throw new InitiativeException(InitiativeConstants.Exception.Publish.InternalServerError.CODE,
                    String.format(InitiativeConstants.Exception.Publish.InternalServerError.COMMANDS_QUEUE, deleteInitiativeCommand.getEntityId(), deleteInitiativeCommand.getOperationType()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        initiativeRepository.deleteById(initiativeId);
        log.info("[DELETE_INITIATIVE] Deleted initiative {} from collection: initiative", initiativeId);
        auditUtilities.logDeletedInitiative(initiativeId);

        performanceLog(startTime, DELETE_INITIATIVE_SERVICE);
    }

    @Override
    public void initializeStatistics(String initiativeId, String organizationId) {
        QueueCommandOperationDTO createInitiativeStatistics = QueueCommandOperationDTO.builder()
                .entityId(initiativeId.concat("_").concat(organizationId))
                .operationType(CREATE_STATISTICS_OPERATION_TYPE)
                .operationTime(LocalDateTime.now())
                .build();
        if(!commandsProducer.sendCommand(createInitiativeStatistics)){
            log.error("[CREATE_INITIATIVE_STATISTICS] - Initiative: {}. Something went wrong while sending the message on Commands Queue", initiativeId);
            throw new InitiativeException(InitiativeConstants.Exception.Publish.InternalServerError.CODE,
                    String.format(InitiativeConstants.Exception.Publish.InternalServerError.COMMANDS_QUEUE,
                            createInitiativeStatistics.getEntityId(),
                            createInitiativeStatistics.getOperationType()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public KeysDTO getTokenKeys(String initiativeId) {
        log.info("[GET_SERVICE_IO_TOKEN_KEYS] - Initiative: {}. Getting serviceIO token keys", initiativeId);
        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        try {
            return ioManageBackEndRestConnector.getServiceKeys(initiative.getAdditionalInfo().getServiceId());
        } catch (Exception e) {
            throw new InitiativeException(InternalServerError.CODE,
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void validate(String contentType, String fileName) {
        Assert.notNull(fileName, "file name cannot be null");

        if (!initiativeUtils.getAllowedInitiativeLogoMimeTypes().contains(contentType)) {
            throw new InvalidMimeTypeException(contentType, String.format("allowed only %s",
                    initiativeUtils.getAllowedInitiativeLogoMimeTypes()));
        }

        String fileExtension = Files.getFileExtension(fileName).toLowerCase();
        if (!initiativeUtils.getAllowedInitiativeLogoExtensions().contains(fileExtension)) {
            throw new IllegalArgumentException(String.format("Invalid file extension \"%s\": allowed only %s", fileExtension,
                    initiativeUtils.getAllowedInitiativeLogoExtensions()));
        }
    }

    private String getUserId() {
        String userId = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                userId = (String) requestAttributes.getAttribute("organizationUserId",
                        RequestAttributes.SCOPE_REQUEST);
            }
        }
        return userId;
    }

    private void performanceLog(long startTime, String service) {
        log.info(
                "[PERFORMANCE_LOG] [{}] Time occurred to perform business logic: {} ms",
                service,
                System.currentTimeMillis() - startTime);
    }

}
