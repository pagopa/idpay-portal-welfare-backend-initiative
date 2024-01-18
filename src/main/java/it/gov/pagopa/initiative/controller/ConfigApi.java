package it.gov.pagopa.initiative.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.gov.pagopa.common.web.dto.ErrorDTO;
import it.gov.pagopa.initiative.dto.config.ConfigMccDTO;
import it.gov.pagopa.initiative.dto.config.ConfigTrxRuleDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface ConfigApi {

    @Operation(summary = "Return MCC config contents", description = "Return MCC config contents", security = {
        @SecurityRequirement(name = "Bearer")    }, tags={ "initiative" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConfigMccDTO.class))),
        
        @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
        
        @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
        
        @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) })
    @GetMapping(value = "/idpay/initiative/config/transaction/mcc",
            produces = {"application/json"})
    ResponseEntity<List<ConfigMccDTO>> getMccConfig();


    @Operation(summary = "Return transaction config rules content", description = "Return transaction config rules content", security = {
        @SecurityRequirement(name = "Bearer")    }, tags={ "initiative" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConfigTrxRuleDTO.class))),
        
        @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

        @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
        
        @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))) })
    @GetMapping(value = "/idpay/initiative/config/transaction/rule",
            produces = {"application/json"})
    ResponseEntity<List<ConfigTrxRuleDTO>> getTransactionConfigRules();

}

