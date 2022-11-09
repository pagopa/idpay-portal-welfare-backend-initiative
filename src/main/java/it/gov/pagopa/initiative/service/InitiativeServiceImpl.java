package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.connector.group.GroupRestConnector;
import it.gov.pagopa.initiative.connector.io_service.IOBackEndRestConnector;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class InitiativeServiceImpl implements InitiativeService {

    private final boolean notifyEmail;
    private final InitiativeRepository initiativeRepository;
    private final InitiativeModelToDTOMapper initiativeModelToDTOMapper;
    private final InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper;
    private final InitiativeProducer initiativeProducer;
    private final IOBackEndRestConnector ioBackEndRestConnector;
    private final GroupRestConnector groupRestConnector;
    private final EmailNotificationService emailNotificationService;
    private final IOTokenService ioTokenService;

    public InitiativeServiceImpl(
            @Value("${app.initiative.conditions.notifyEmail}") boolean notifyEmail,
            InitiativeRepository initiativeRepository,
            InitiativeModelToDTOMapper initiativeModelToDTOMapper,
            InitiativeAdditionalDTOsToIOServiceRequestDTOMapper initiativeAdditionalDTOsToIOServiceRequestDTOMapper,
            InitiativeProducer initiativeProducer,
            IOBackEndRestConnector ioBackEndRestConnector,
            GroupRestConnector groupRestConnector,
            EmailNotificationService emailNotificationService,
            IOTokenService ioTokenService
    ){
        this.notifyEmail = notifyEmail;
        this.initiativeRepository = initiativeRepository;
        this.initiativeModelToDTOMapper = initiativeModelToDTOMapper;
        this.initiativeProducer = initiativeProducer;
        this.initiativeAdditionalDTOsToIOServiceRequestDTOMapper = initiativeAdditionalDTOsToIOServiceRequestDTOMapper;
        this.ioBackEndRestConnector = ioBackEndRestConnector;
        this.groupRestConnector = groupRestConnector;
        this.emailNotificationService = emailNotificationService;
        this.ioTokenService = ioTokenService;
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
    public Initiative insertInitiative(Initiative initiative) {
        if (StringUtils.isBlank(initiative.getStatus())) {
            initiative.setStatus(InitiativeConstants.Status.DRAFT);
        }
        Initiative initiativeReturned = initiativeRepository.insert(initiative);
        if(notifyEmail){
            emailNotificationService.sendInitiativeEnteCreated(initiative, initiative.getOrganizationName());
        }
        return initiativeReturned;
    }

    @Override
    public Initiative getInitiative(String organizationId, String initiativeId, String role) {
        Initiative initiative = initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        if (InitiativeConstants.Role.OPE_BASE.equals(role)){
            if (initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION) || initiative.getStatus().equals(InitiativeConstants.Status.TO_CHECK) || initiative.getStatus().equals(InitiativeConstants.Status.APPROVED)){
                return initiative;
            }else {
                throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role),
                        HttpStatus.BAD_REQUEST
                );
            }
        }else{
            return initiative;
        }
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
    public void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel) {
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setGeneral(initiativeInfoModel.getGeneral());
        initiative.setUpdateDate(LocalDateTime.now());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
    }

    /*Primo salvataggio in Draft tramite Wizard*/
    @Override
    public void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setAdditionalInfo(initiativeAdditionalInfo.getAdditionalInfo());
        initiative.setUpdateDate(LocalDateTime.now());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
        if(notifyEmail){
            emailNotificationService.sendInitiativeEnte(initiative, initiative.getOrganizationName());
        }
    }

    @Override
    public void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule initiativeBeneficiaryRuleModel){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        //Check Initiative Status
        isInitiativeAllowedToBeEditableThenThrows(initiative);
        initiative.setBeneficiaryRule(initiativeBeneficiaryRuleModel);
        initiative.setUpdateDate(LocalDateTime.now());
        initiative.setStatus(InitiativeConstants.Status.DRAFT);
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules) {
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
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
    public void updateInitiativeRefundRules(String organizationId, String organizationName, String initiativeId, Initiative refundRule, boolean changeInitiativeStatus){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
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
            emailNotificationService.sendInitiativeEntePagoPA(initiative, organizationName);
        }
    }

    @Override
    @Transactional
    public void updateInitiativeApprovedStatus(String organizationId, String organizationName, String initiativeId){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.APPROVED);
        initiative.setStatus(InitiativeConstants.Status.APPROVED);
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
        log.info("[UPDATE_TO_APPROVED_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        if(notifyEmail){
            emailNotificationService.sendInitiativeEnte(initiative, organizationName);
        }
    }

    @Override
    @Transactional
    public void updateInitiativeToCheckStatus(String organizationId, String organizationName, String initiativeId){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        isInitiativeStatusNotInRevisionThenThrow(initiative, InitiativeConstants.Status.TO_CHECK);
        initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
        log.info("[UPDATE_TO_CHECK_STATUS] - Initiative: {}. Status successfully changed", initiative.getInitiativeId());
        if(notifyEmail){
            emailNotificationService.sendInitiativeEnte(initiative, organizationName);
        }
    }

    @Override
    public void logicallyDeleteInitiative(String organizationId, String initiativeId){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        if (
                initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION) ||
                initiative.getStatus().equals(InitiativeConstants.Status.PUBLISHED) ||
                initiative.getStatus().equals(InitiativeConstants.Status.CLOSED) ||
                initiative.getStatus().equals(InitiativeConstants.Status.SUSPENDED)
        ){
            log.error("[LOGICAL_INITIATIVE_ELIMINATION] - Initiative: {}. Cannot be deleted. Current status is {}.", initiative.getInitiativeId(), initiative.getStatus());
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_CANNOT_BE_DELETED, initiativeId),
                    HttpStatus.BAD_REQUEST
            );
        }else{
            initiative.setEnabled(false);
            initiative.setUpdateDate(LocalDateTime.now());
            this.initiativeRepository.save(initiative);
            log.info("[LOGICAL_INITIATIVE_ELIMINATION] - Initiative: {}. Successfully logical elimination.", initiative.getInitiativeId());
        }
        if(notifyEmail){
            emailNotificationService.sendInitiativeEnte(initiative, initiative.getOrganizationName());
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
    public void isInitiativeAllowedToBeNextStatusThenThrows(Initiative initiative, String nextStatus) {
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
            emailNotificationService.sendInitiativeEntePagoPA(initiative, initiative.getOrganizationName());
        }
        return initiative;
    }

    private void isInitiativeAllowedToBeEditableThenThrows(Initiative initiative){
        if(Arrays.asList(InitiativeConstants.Status.Validation.INITIATIVES_ALLOWED_STATES_TO_BE_EDITABLE_ARRAY).contains(initiative.getStatus())){
            return;
        }
        throw new InitiativeException(
                InitiativeConstants.Exception.BadRequest.CODE,
                String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()),
                HttpStatus.BAD_REQUEST);
    }

    private void isInitiativeStatusNotInRevisionThenThrow(Initiative initiative, String nextStatus){
        if (initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION)){
            log.info("[UPDATE_TO_{}_STATUS] - Initiative: {}. Current status is valid", nextStatus, initiative.getInitiativeId());
            return;
        }
        log.info("[UPDATE_TO_{}_STATUS] - Initiative: {}. Current status is not IN_REVISION", nextStatus, initiative.getInitiativeId());
        throw new InitiativeException(
                InitiativeConstants.Exception.BadRequest.CODE,
                InitiativeConstants.Exception.BadRequest.INITIATIVE_STATUS_NOT_IN_REVISION,
                HttpStatus.BAD_REQUEST);
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
}
