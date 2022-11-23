package it.gov.pagopa.initiative.controller;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.exception.IntegrationException;
import it.gov.pagopa.initiative.mapper.InitiativeDTOsToModelMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.service.InitiativeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
public class InitiativeApiController implements InitiativeApi {

    private final boolean notifyRE;
    private final boolean notifyIO;
    private final boolean notifyInternal;
    private final InitiativeService initiativeService;
    private final InitiativeModelToDTOMapper initiativeModelToDTOMapper;
    private final InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    public InitiativeApiController(
            @Value("${app.initiative.conditions.notifyRE}") boolean notifyRE,
            @Value("${app.initiative.conditions.notifyIO}") boolean notifyIO,
            @Value("${app.initiative.conditions.notifyInternal}") boolean notifyInternal,
            InitiativeService initiativeService,
            InitiativeModelToDTOMapper initiativeModelToDTOMapper,
            InitiativeDTOsToModelMapper initiativeDTOsToModelMapper) {
        this.notifyRE = notifyRE;
        this.notifyIO = notifyIO;
        this.notifyInternal = notifyInternal;
        this.initiativeService = initiativeService;
        this.initiativeModelToDTOMapper = initiativeModelToDTOMapper;
        this.initiativeDTOsToModelMapper = initiativeDTOsToModelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<List<InitiativeSummaryDTO>> getInitiativeSummary(String organizationId, String role) {
        log.info("[{}][GET_INITIATIVES] - InitiativeSummary: Start processing...", role);
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeSummaryDTOList(
                this.initiativeService.retrieveInitiativeSummary(organizationId, role)
        ));
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InitiativeDTO> getInitiativeDetail(String organizationId, String initiativeId, String role) {
        log.info("[{}][GET_INITIATIVE_DETAIL] - Initiative: {}. Start processing...", role, initiativeId);
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiative(organizationId, initiativeId, role)));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<InitiativeDTO> saveInitiativeServiceInfo(String organizationId, InitiativeAdditionalDTO initiativeAdditionalDTO) {
        String role = initiativeAdditionalDTO.getOrganizationUserRole();
        log.info("[{}][SAVE_ADDITIONAL_INFO]-[FIRST_CREATION_TO_DRAFT_STATUS] - Initiative: Start processing...", role);
        Initiative initiativeToSave = this.initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO);
        Initiative insertedInitiative = initiativeService.insertInitiative(initiativeToSave, organizationId, initiativeAdditionalDTO.getOrganizationName(), role);
        return new ResponseEntity<>(this.initiativeModelToDTOMapper.toDtoOnlyId(insertedInitiative), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeGeneralInfo(String organizationId, String initiativeId, InitiativeGeneralDTO initiativeGeneralDTO) {
        String role = initiativeGeneralDTO.getOrganizationUserRole();
        log.info("[{}][UPDATE_GENERAL_INFO]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        this.initiativeService.updateInitiativeGeneralInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeGeneralDTO), role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeAdditionalInfo(String organizationId, String initiativeId, InitiativeAdditionalDTO initiativeAdditionalDTO) {
        String role = initiativeAdditionalDTO.getOrganizationUserRole();
        log.info("[{}][UPDATE_ADDITIONAL_INFO]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        this.initiativeService.updateInitiativeAdditionalInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO), role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeGeneralInfoDraft(String organizationId, String initiativeId, InitiativeGeneralDTO initiativeGeneralDTO) {
        String role = initiativeGeneralDTO.getOrganizationUserRole();
        log.info("[{}][API-DRAFT]-[UPDATE_GENERAL_INFO]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        this.initiativeService.updateInitiativeGeneralInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeGeneralDTO), role);
        return ResponseEntity.noContent().build();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRuleDTO beneficiaryRuleDto, String role) {
        role = beneficiaryRuleDto.getOrganizationUserRole();
        log.info("[{}][UPDATE_BENEFICIARY_RULE]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        if(Boolean.TRUE.equals(this.initiativeService.getInitiative(organizationId, initiativeId, role).getGeneral().getBeneficiaryKnown())){
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_PROPERTIES_NOT_VALID, initiativeId),
                    HttpStatus.BAD_REQUEST);
        }
        this.initiativeService.updateStep3InitiativeBeneficiary(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDto), role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeBeneficiaryDraft(String organizationId, String initiativeId, InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        String role = beneficiaryRuleDto.getOrganizationUserRole();
        log.info("[{}][API-DRAFT]-[UPDATE_BENEFICIARY_RULE]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        this.initiativeService.updateStep3InitiativeBeneficiary(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDto), role);
        return ResponseEntity.noContent().build();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateTrxAndRewardRules(String organizationId, String initiativeId, InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO) {
        String role = rewardAndTrxRulesDTO.getOrganizationUserRole();
        log.info("[{}][UPDATE_TRX_REWARD_RULE]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO);
        this.initiativeService.updateTrxAndRewardRules(organizationId, initiativeId, initiative, role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateTrxAndRewardRulesDraft(String organizationId, String initiativeId, InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO){
        String role = rewardAndTrxRulesDTO.getOrganizationUserRole();
        log.info("[{}][API-DRAFT]-[UPDATE_TRX_REWARD_RULE]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO);
        this.initiativeService.updateTrxAndRewardRules(organizationId, initiativeId, initiative, role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeRefundRule(String organizationId, String initiativeId, InitiativeRefundRuleDTO initiativeRefundRuleDTO){
        String role = initiativeRefundRuleDTO.getOrganizationUserRole();
        log.info("[{}][UPDATE_REFUND_RULE]-[UPDATE_TO_IN_REVISION_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTO);
        this.initiativeService.updateInitiativeRefundRules(organizationId, initiativeId, role, initiative, true);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeRefundRuleDraft(String organizationId, String initiativeId, InitiativeRefundRuleDTO initiativeRefundRuleDTO){
        String role = initiativeRefundRuleDTO.getOrganizationUserRole();
        log.info("[{}][API-DRAFT]-[UPDATE_REFUND_RULE]-[UPDATE_TO_DRAFT_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTO);
        this.initiativeService.updateInitiativeRefundRules(organizationId, initiativeId, role, initiative, false);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeApprovedStatus(String organizationId, String initiativeId, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO){
        String role = initiativeOrganizationInfoDTO.getOrganizationUserRole();
        log.info("[{}][UPDATE_TO_APPROVED_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        this.initiativeService.updateInitiativeApprovedStatus(organizationId, initiativeId, role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeToCheckStatus(String organizationId, String initiativeId, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO){
        String role = initiativeOrganizationInfoDTO.getOrganizationUserRole();
        log.info("[{}][UPDATE_TO_CHECK_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        this.initiativeService.updateInitiativeToCheckStatus(organizationId, initiativeId, role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> logicallyDeleteInitiative(String organizationId, String initiativeId, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO){
        String role = initiativeOrganizationInfoDTO.getOrganizationUserRole();
        log.info("[{}][LOGICAL_DELETE_INITIATIVE] - Initiative: {}. Start processing...", role, initiativeId);
        this.initiativeService.logicallyDeleteInitiative(organizationId, initiativeId, role);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeDTO> getInitiativeBeneficiaryView(String initiativeId) {
        log.info("[GET_INITIATIVE_DETAIL_FOR_BENEFICIARY] - Initiative: {}. Start processing...", initiativeId);
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiativeBeneficiaryView(initiativeId)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativePublishedStatus(String organizationId, String initiativeId, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO, String role) {
        role = initiativeOrganizationInfoDTO.getOrganizationUserRole();
        //Retrieve Initiative
        log.info("[{}][UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Start processing...", role, initiativeId);
        Initiative initiative = this.initiativeService.getInitiative(organizationId, initiativeId, role);
        log.debug("Initiative retrieved");

        //Validation for current Status
        log.debug("Validating current Status");
        initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED, role);
        log.debug("Current Status validated");

        log.debug("Retrieve current state and save it as TEMP");
        String statusTemp = initiative.getStatus();
        LocalDateTime updateDateTemp = initiative.getUpdateDate();

        initiative.setStatus(InitiativeConstants.Status.PUBLISHED);
        initiative.setUpdateDate(LocalDateTime.now());
        initiativeService.updateInitiative(initiative);
        log.debug("Initiative saved in status PUBLISHED");
        try {
            if(notifyRE) {
                log.info("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Notification to Rule Engine of the published Initiative", initiativeId);
                initiativeService.sendInitiativeInfoToRuleEngine(initiative);
            }
            //1. Only for the Initiatives to be provided to IO, the integration is carried out with the creation of the Initiative Service to IO BackEnd
            if(initiative.getAdditionalInfo().getServiceIO()) {
                if(notifyIO) {
                    log.info("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Notification to IO BackEnd of the published Initiative", initiativeId);
                    initiative = initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
                }
                initiativeService.updateInitiative(initiative);
                //Send citizen to MS-Group via API
                //This integration necessarily takes place in succession to having created the service with IO in order not to send "orphan" resources (not associated with any Initiative known by IO).
                //2. BeneficiaryKnown is true -> Send to MS-Group via API about the publishing of Initiative. Then, MS Groups will send it via Topics to NotificationManager and Onboarding
                if (null != initiative.getGeneral() && initiative.getGeneral().getBeneficiaryKnown()) {
                    if(notifyInternal) {
                        initiativeService.sendInitiativeInfoToNotificationManager(initiative);
                    }
                }
            }
        } catch (Exception e) {
            //In case one of the previous Integrations ends badly, the Initiative is rolled back to the initial TEMP state
            log.error("[UPDATE_TO_PUBLISHED_STATUS] - [ROLLBACK STATUS] Initiative: {}. Generic Error: {}", initiativeId, e.getMessage());
            initiative.setStatus(statusTemp);
            initiative.setUpdateDate(updateDateTemp);
            initiativeService.updateInitiative(initiative);
            log.debug("Initiative Status has been roll-backed to {}", statusTemp);
            throw new IntegrationException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<LogoDTO> addLogo(String organizationId, String initiativeId,
            MultipartFile logo) throws IOException {
        log.info("[ADD_LOGO_TO_INITIATIVE] - Initiative: {}. Start processing...", initiativeId);
        return ResponseEntity.ok(initiativeService.storeInitiativeLogo(organizationId,initiativeId,logo.getInputStream(),logo.getContentType(),logo.getOriginalFilename()));
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeDataDTO> getInitiativeIdFromServiceId(String acceptLanguage, String serviceId) {
        log.info("[GET_INITIATIVE_ID_FROM_SERVICE_ID] - Start searching the initiativeId for serviceId {}", serviceId);
        //check if valid locale
        Locale inputLocale = LocaleUtils.toLocale(acceptLanguage);
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDataDTO(this.initiativeService.getInitiativeIdFromServiceId(serviceId), inputLocale));
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeAdditionalDTO> getPrimaryAndSecondaryTokenIO(String initiativeId){
        log.info("[GET_PRIMARY_AND_SECONDARY_TOKEN] - Start searching tokens for initiativeId {}...", initiativeId);
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeAdditionalDTOOnlyTokens(this.initiativeService.getPrimaryAndSecondaryTokenIO(initiativeId)));
    }

    @Override
    public ResponseEntity<OnboardingDTO> getOnboardingStatus(String organizationId,String initiativeId, Pageable pageable,
        String beneficiary, LocalDateTime dateFrom, LocalDateTime dateTo, String state) {
        return ResponseEntity.ok(this.initiativeService.getOnboardingStatusList(organizationId,initiativeId,beneficiary,dateFrom,dateTo,state,pageable));
    }

}
