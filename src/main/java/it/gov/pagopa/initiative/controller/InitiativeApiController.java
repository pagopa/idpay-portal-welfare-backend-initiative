package it.gov.pagopa.initiative.controller;

import it.gov.pagopa.initiative.dto.*;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<List<InitiativeSummaryDTO>> getInitativeSummary(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeSummaryDTOList(
                this.initiativeService.retrieveInitiativeSummary(organizationId)
        ));
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InitiativeDTO> getInitiativeDetail(@PathVariable("organizationId") String organizationId, @PathVariable("initiativeId") String initiativeId) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiative(organizationId, initiativeId)));
    }

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InitiativeDTO> saveInitiativeGeneralInfo(@PathVariable("organizationId") String organizationId, /*@Valid*/@Validated(ValidationOnGroup.class) @RequestBody InitiativeInfoDTO initiativeInfoDTO) {
        Initiative initiativeToSave = this.initiativeDTOsToModelMapper.toInitiative(initiativeInfoDTO);
        initiativeToSave.setOrganizationId(organizationId);
        initiativeToSave.setCreationDate(LocalDateTime.now());
        initiativeToSave.setUpdateDate(LocalDateTime.now());
        //TODO verificare se necessario controllo per serviceId e organization non sovrapposti prima di creare una ulteriore iniziativa
        Initiative insertedInitiative = initiativeService.insertInitiative(initiativeToSave);
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toDtoOnlyId(insertedInitiative));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateInitiativeBeneficiary(@PathVariable("organizationId") String organizationId,  @PathVariable("initiativeId") String initiativeId, @Validated(ValidationOnGroup.class) @RequestBody InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        this.initiativeService.updateInitiativeBeneficiary(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDto));
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateInitiativeGeneralInfo(@PathVariable("organizationId") String organizationId,  @PathVariable("initiativeId") String initiativeId, @Validated({ValidationOnGroup.class}) @RequestBody InitiativeInfoDTO initiativeInfoDto) {
        this.initiativeService.updateInitiativeGeneralInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeInfoDto));
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateInitiativeBeneficiaryDraft(@PathVariable("organizationId") String organizationId,  @PathVariable("initiativeId") String initiativeId, @RequestBody InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        this.initiativeService.updateInitiativeBeneficiary(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDto));
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateInitiativeGeneralInfoDraft(@PathVariable("organizationId") String organizationId,  @PathVariable("initiativeId") String initiativeId, @RequestBody InitiativeInfoDTO initiativeInfoDto) {
        this.initiativeService.updateInitiativeGeneralInfo(organizationId, initiativeId, this.initiativeDTOsToModelMapper.toInitiative(initiativeInfoDto));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<InitiativeDTO> getInitiativeBeneficiaryView(String initiativeId) {
        return ResponseEntity.ok(this.initiativeModelToDTOMapper.toInitiativeDTO(this.initiativeService.getInitiativeBeneficiaryView(initiativeId)));
    }

}
