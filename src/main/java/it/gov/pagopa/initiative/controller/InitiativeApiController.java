package it.gov.pagopa.initiative.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.initiative.dto.InitiativeBeneficiaryRuleDTO;
import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.dto.InitiativeInfoDTO;
import it.gov.pagopa.initiative.dto.InitiativeSummaryDTO;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.service.InitiativeService;
import it.gov.pagopa.initiative.mapper.InitiativeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class InitiativeApiController implements InitiativeApi {

    private static final Logger log = LoggerFactory.getLogger(InitiativeApiController.class);

    @Autowired
    private InitiativeService initiativeService;

    @Autowired
    private InitiativeMapper mapper;

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InitiativeSummaryDTO>> getInitativeSummary(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(this.mapper.toInitiativeSummaryDtoList(
                this.initiativeService.retrieveInitiativeSummary(organizationId)
        ));
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InitiativeDTO> getInitiativeDetail(@PathVariable("organizationId") String organizationId, @PathVariable("initiativeId") String initiativeId) {
        return ResponseEntity.ok(this.mapper.toInitiativeDto(this.initiativeService.getInitiative(organizationId, initiativeId)));
    }

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InitiativeDTO> saveInitiativeGeneralInfo(@PathVariable("organizationId") String organizationId, @Valid @RequestBody InitiativeInfoDTO initiativeInfoDTO) {
        Initiative initiativeToSave = this.mapper.toInitiativeInfoModel(initiativeInfoDTO);
        initiativeToSave.setOrganizationId(organizationId);
        //TODO verificare se necessario controllo per serviceId e organization non sovrapposti prima di creare una ulteriore iniziativa
        Initiative insertedInitiative = initiativeService.insertInitiative(initiativeToSave);
        return ResponseEntity.ok(this.mapper.toDtoOnlyId(insertedInitiative));
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateInitiativeBeneficiary(@PathVariable("organizationId") String organizationId,  @PathVariable("initiativeId") String initiativeId, @Valid @RequestBody InitiativeBeneficiaryRuleDTO beneficiaryRuleDto) {
        this.initiativeService.updateInitiativeBeneficiary(organizationId, initiativeId, this.mapper.toBeneficiaryRuleModel(beneficiaryRuleDto));
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateInitiativeGeneralInfo(@PathVariable("organizationId") String organizationId,  @PathVariable("initiativeId") String initiativeId, @Valid @RequestBody InitiativeInfoDTO initiativeInfoDto) {
        this.initiativeService.updateInitiativeGeneralInfo(organizationId, initiativeId, this.mapper.toInitiativeInfoModel(initiativeInfoDto));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<InitiativeDTO> getInitiativeBeneficiaryView(String initiativeId) {
        return ResponseEntity.ok(this.mapper.toInitiativeDto(this.initiativeService.getInitiativeBeneficiaryView(initiativeId)));
    }

}
