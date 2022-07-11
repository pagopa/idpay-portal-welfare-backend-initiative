package it.gov.pagopa.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.dto.InitiativeBeneficiaryRuleDTO;
import it.gov.pagopa.dto.InitiativeDTO;
import it.gov.pagopa.dto.InitiativeInfoDTO;
import it.gov.pagopa.dto.InitiativeSummaryDTO;
import it.gov.pagopa.mapper.InitiativeMapper;
import it.gov.pagopa.model.Initiative;
import it.gov.pagopa.service.InitiativeService;
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

    public ResponseEntity<InitiativeDTO> getInitiativeDetail(@Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId) {
        return new ResponseEntity<InitiativeDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InitiativeDTO> saveInitiativeGeneralInfo(@PathVariable("organizationId") String organizationId, @Valid @RequestBody InitiativeInfoDTO initiativeInfoDTO) {
        Initiative initiativeToSave = this.mapper.toInitiativeInfoModel(initiativeInfoDTO);
        initiativeToSave.setOrganizationId(organizationId);
        Initiative insertedInitiative = initiativeService.insertInitiative(initiativeToSave);
        return ResponseEntity.ok(this.mapper.toDtoOnlyId(insertedInitiative));
    }

    public ResponseEntity<Void> updateInitiativeBeneficiary(@Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, instrument HPAN", schema = @Schema()) @Valid @RequestBody InitiativeBeneficiaryRuleDTO body) {
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateInitiativeGeneralInfo(@Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema = @Schema()) @Valid @RequestBody InitiativeInfoDTO body) {
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
