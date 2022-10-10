package it.gov.pagopa.initiative.controller;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.exception.IntegrationException;
import it.gov.pagopa.initiative.mapper.InitiativeDTOsToModelMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.service.InitiativeService;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class InitiativeApiController implements InitiativeApi {

    private final boolean notifyRE;
    private final InitiativeService initiativeService;
    private final InitiativeModelToDTOMapper initiativeModelToDTOMapper;
    private final InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    public InitiativeApiController(@Value("${app.initiative.conditions.notifyRE}") boolean notifyRE, InitiativeService initiativeService, InitiativeModelToDTOMapper initiativeModelToDTOMapper, InitiativeDTOsToModelMapper initiativeDTOsToModelMapper) {
        this.notifyRE = notifyRE;
        this.initiativeService = initiativeService;
        this.initiativeModelToDTOMapper = initiativeModelToDTOMapper;
        this.initiativeDTOsToModelMapper = initiativeDTOsToModelMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<List<InitiativeSummaryDTO>> getInitiativeSummary(String organizationId, String role) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeSummaryDTOList(
                this.initiativeService.retrieveInitiativeSummary(organizationId, role)
        ));
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeDTO> getInitiativeDetail(String organizationId, String initiativeId) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiative(organizationId, initiativeId)));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<InitiativeDTO> saveInitiativeServiceInfo(String organizationId, @RequestBody @Validated(ValidationOnGroup.class) InitiativeAdditionalDTO initiativeAdditionalDTO) {
        Initiative initiativeToSave = this.initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO);
        initiativeToSave.setOrganizationId(organizationId);
        initiativeToSave.setCreationDate(LocalDateTime.now());
        initiativeToSave.setUpdateDate(LocalDateTime.now());
        initiativeToSave.setEnabled(true);
        //TODO verificare se necessario controllo per serviceId e organization non sovrapposti prima di creare una ulteriore iniziativa
        Initiative insertedInitiative = initiativeService.insertInitiative(initiativeToSave);
        return new ResponseEntity<>(this.initiativeModelToDTOMapper.toDtoOnlyId(insertedInitiative), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeGeneralInfo(String organizationId, String initiativeId, @RequestBody @Validated(ValidationOnGroup.class) InitiativeGeneralDTO initiativeGeneralDTO) {
        this.initiativeService.updateInitiativeGeneralInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeGeneralDTO));
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeAdditionalInfo(String organizationId, String initiativeId, @RequestBody @Validated(ValidationOnGroup.class) InitiativeAdditionalDTO initiativeAdditionalDTO) {
        this.initiativeService.updateInitiativeAdditionalInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeAdditionalDTO));
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeGeneralInfoDraft(String organizationId, String initiativeId, @RequestBody InitiativeGeneralDTO initiativeGeneralDTO) {
        this.initiativeService.updateInitiativeGeneralInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeGeneralDTO));
        return ResponseEntity.noContent().build();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeBeneficiary(String organizationId, String initiativeId, @RequestBody @Validated(ValidationOnGroup.class) InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        if(Boolean.TRUE.equals(this.initiativeService.getInitiative(organizationId, initiativeId).getGeneral().getBeneficiaryKnown())){
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_PROPERTIES_NOT_VALID, initiativeId),
                    HttpStatus.BAD_REQUEST);
        }
        this.initiativeService.updateInitiativeBeneficiary(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDto));
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeBeneficiaryDraft(String organizationId, String initiativeId, InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        this.initiativeService.updateInitiativeBeneficiary(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDto));
        return ResponseEntity.noContent().build();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateTrxAndRewardRules(String organizationId, String initiativeId, @RequestBody @Validated(ValidationOnGroup.class) InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO) {
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO);
        this.initiativeService.updateTrxAndRewardRules(organizationId, initiativeId, initiative);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateTrxAndRewardRulesDraft(String organizationId, String initiativeId, InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO){
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(rewardAndTrxRulesDTO);
        this.initiativeService.updateTrxAndRewardRules(organizationId, initiativeId, initiative);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeRefundRule(String organizationId, String initiativeId, @RequestBody @Validated(ValidationOnGroup.class) InitiativeRefundRuleDTO initiativeRefundRuleDTO){
        log.info("[UPDATE_TO_IN_REVISION_STATUS]-[UPDATE_REFUND_RULE] - Initiative: {}. Start processing...", initiativeId);
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTO);
        this.initiativeService.updateInitiativeRefundRules(organizationId, initiativeId, initiative, true);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeRefundRuleDraft(String organizationId, String initiativeId, @RequestBody InitiativeRefundRuleDTO initiativeRefundRuleDTO){
        Initiative initiative = this.initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTO);
        this.initiativeService.updateInitiativeRefundRules(organizationId, initiativeId, initiative, false);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeApprovedStatus(String organizationId, String initiativeId){
        log.info("[UPDATE_TO_APPROVED_STATUS] - Initiative: {}. Start processing...", initiativeId);
        this.initiativeService.updateInitiativeApprovedStatus(organizationId, initiativeId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativeToCheckStatus(String organizationId, String initiativeId ){
        log.info("[UPDATE_TO_CHECK_STATUS] - Initiative: {}. Start processing...", initiativeId);
        this.initiativeService.updateInitiativeToCheckStatus(organizationId, initiativeId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> logicallyDeleteInitiative(String organizationId, String initiativeId){
        log.info("[LOGICAL_INITIATIVE_ELIMINATION] - Initiative: {}. Start processing...", initiativeId);
        this.initiativeService.logicallyDeleteInitiative(organizationId, initiativeId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeDTO> getInitiativeBeneficiaryView(String initiativeId) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiativeBeneficiaryView(initiativeId)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> updateInitiativePublishedStatus(String organizationId, String initiativeId, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO) {
        //Retrieve Initiative
        log.info("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Start processing...", initiativeId);
        Initiative initiative = this.initiativeService.getInitiative(organizationId, initiativeId);
        log.debug("Initiative retrieved");

        //Validation for current Status
        log.debug("Validating current Status");
        initiativeService.isInitiativeAllowedToBeNextStatusThenThrows(initiative, InitiativeConstants.Status.PUBLISHED);
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
            //2. Sprint 9: In caso di beneficiaryKnown a true -> Invio al MS-Gruppi via API la richiesta di notifica della pubblicazione e, MS Gruppi la invia via coda al NotificationManager
            if(initiative.getAdditionalInfo().getServiceIO()) {
                log.info("[UPDATE_TO_PUBLISHED_STATUS] - Initiative: {}. Notification to IO BackEnd of the published Initiative", initiativeId);
                initiative = initiativeService.sendInitiativeInfoToIOBackEndServiceAndUpdateInitiative(initiative, initiativeOrganizationInfoDTO);
                initiativeService.updateInitiative(initiative);
                //Invio al MS-Gruppi via API
                //This integration necessarily takes place in succession to having created the service with IO in order not to send "orphan" resources (not associated with any Initiative known by IO).
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

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeDTO> getInitiativeIdFromServiceId(String organizationId, String serviceId){
        log.info("[GET_INITIATIVE_ID_FROM_SERVICE_ID] - Start searching the initiativeId for serviceId {}...", serviceId);
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiativeIdFromServiceId(serviceId)));
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeAdditionalDTO> getPrimaryAndSecondaryTokenIO(String organizationId, String initiativeId){
        log.info("[GET_PRIMARY_AND_SECONDARY_TOKEN] - Start searching tokens for initiativeId {}...", initiativeId);
//        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeAdditionalDTOOnlyTokens(this.initiativeService.getPrimaryAndSecondaryTokenIO(initiativeId)));
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeAdditionalDTOOnlyTokens(this.initiativeService.getPrimaryAndSecondaryTokenIO(organizationId, initiativeId)));
    }

}
