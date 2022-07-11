package it.gov.pagopa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.gov.pagopa.controller.InitiativeApi;
import it.gov.pagopa.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-11T11:28:33.400Z[GMT]")
@RestController
public class InitiativeApiController implements InitiativeApi {

    private static final Logger log = LoggerFactory.getLogger(InitiativeApiController.class);


    public ResponseEntity<InitiativeSummaryDTO> getInitativeSummary() {
        return new ResponseEntity<InitiativeSummaryDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<InitiativeDTO> getInitiativeDetail(@Parameter(in = ParameterIn.PATH, description = "The initiative ID", required=true, schema=@Schema()) @PathVariable("initiativeId") String initiativeId) {
        return new ResponseEntity<InitiativeDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> saveInitiativeGeneralInfo(@Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema=@Schema()) @Valid @RequestBody InitiativeGeneralDTO body) {
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateInitiativeBeneficiary(@Parameter(in = ParameterIn.PATH, description = "The initiative ID", required=true, schema=@Schema()) @PathVariable("initiativeId") String initiativeId,@Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, instrument HPAN", schema=@Schema()) @Valid @RequestBody InitiativeBeneficiaryRuleDTO body) {
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateInitiativeGeneralInfo(@Parameter(in = ParameterIn.PATH, description = "The initiative ID", required=true, schema=@Schema()) @PathVariable("initiativeId") String initiativeId,@Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema=@Schema()) @Valid @RequestBody InitiativePatchDTO body) {
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
