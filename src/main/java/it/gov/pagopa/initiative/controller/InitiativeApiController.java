package it.gov.pagopa.initiative.controller;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeDTOsToModelMapper;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.service.InitiativeService;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private InitiativeService initiativeService;

    @Autowired
    private InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @Autowired
    private InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<List<InitiativeSummaryDTO>> getInitativeSummary(String organizationId) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeSummaryDTOList(
                this.initiativeService.retrieveInitiativeSummary(organizationId)
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
        this.initiativeService.updateInitiativeToCheckStatus(organizationId, initiativeId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<InitiativeDTO> getInitiativeBeneficiaryView(String initiativeId) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiativeBeneficiaryView(initiativeId)));
    }

}
