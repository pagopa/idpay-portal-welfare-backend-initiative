package it.gov.pagopa.initiative.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface InitiativeApi {

    @Operation(summary = "Returns the list of initiatives names for a specific organization", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InitiativeSummaryDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping(value = "/idpay/organization/{organizationId}/initiative/summary",
            produces = {"application/json"})
    ResponseEntity<List<InitiativeSummaryDTO>> getInitativeSummary(@PathVariable("organizationId") String organizationId);

    @Operation(summary = "Returns the detail of an active initiative", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InitiativeDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}",
            produces = {"application/json"})
    ResponseEntity<InitiativeDTO> getInitiativeDetail(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId);

    @Operation(summary = "Save initiative and first subset of data 'general info'", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PostMapping(value = "/idpay/organization/{organizationId}/initiative/general",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<InitiativeDTO> saveInitiativeGeneralInfo(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema = @Schema()) @RequestBody InitiativeInfoDTO body);

    @Operation(summary = "Update initiative and first subset of data 'general info'", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/general",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Void> updateInitiativeGeneralInfo(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema = @Schema()) @RequestBody InitiativeInfoDTO body);

    @Operation(summary = "Association of beneficiary rules to a draft initiative without validation", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/beneficiary/draft",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Void> updateInitiativeBeneficiaryDraft(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, instrument HPAN", schema = @Schema()) @RequestBody InitiativeBeneficiaryRuleDTO body);

    @Operation(summary = "Association of beneficiary rules to an initiative", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/beneficiary",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Void> updateInitiativeBeneficiary(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, instrument HPAN", schema = @Schema()) @RequestBody InitiativeBeneficiaryRuleDTO body);

    @Operation(summary = "Association of transaction and reward rules to a draft initiative without validation", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/reward/draft",
            produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<Void> updateTrxAndRewardRulesDraft(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO);

    @Operation(summary = "Association of transaction and reward rules to an initiative", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/reward",
            produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<Void> updateTrxAndRewardRules(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO);

    @Operation(summary = "Save the refund rule of the initiative", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/refund",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Void> updateInitiativeRefundRule(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody InitiativeRefundRuleDTO initiativeRefundRuleDTO);

    @Operation(summary = "Save the draft refund rule of the initiative", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/refund/draft",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Void> updateInitiativeRefundRuleDraft(@PathVariable("organizationId") String organizationId, @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId, @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody InitiativeRefundRuleDTO initiativeRefundRuleDTO);

    @Operation(summary = "Returns the detail of an active initiative", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InitiativeDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @GetMapping(value = "/idpay/initiative/{initiativeId}/beneficiary/view",
            produces = {"application/json"})
    ResponseEntity<InitiativeDTO> getInitiativeBeneficiaryView(@PathVariable("initiativeId") String initiativeId);

}

