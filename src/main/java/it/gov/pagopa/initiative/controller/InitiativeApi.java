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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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
  ResponseEntity<List<InitiativeSummaryDTO>> getInitiativeSummary(
      @PathVariable("organizationId") String organizationId,
      @RequestParam(required = false) String role);

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
  ResponseEntity<InitiativeDTO> getInitiativeDetail(
      @PathVariable("organizationId") String organizationId,
      @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
      @RequestParam(required = false) String role);

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
    @PostMapping(value = "/idpay/organization/{organizationId}/initiative/info",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<InitiativeDTO> saveInitiativeServiceInfo(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema = @Schema()) @RequestBody @Validated(ValidationOnGroup.class) InitiativeAdditionalDTO body);

  @Operation(summary = "Update the additional info of the initiative", description = "", security = {
      @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
  @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/info",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<Void> updateInitiativeAdditionalInfo(
      @PathVariable("organizationId") String organizationId,
      @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
      @RequestBody @Validated(ValidationOnGroup.class) InitiativeAdditionalDTO initiativeAdditionalDTO);


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
    ResponseEntity<Void> updateInitiativeGeneralInfo(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema = @Schema()) @RequestBody @Validated(ValidationOnGroup.class) InitiativeGeneralDTO body);

  @Operation(summary = "Update initiative and first subset of draft data 'general info'", description = "", security = {
      @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
  @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/general/draft",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<Void> updateInitiativeGeneralInfoDraft(
      @PathVariable("organizationId") String organizationId,
      @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
      @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, IBAN of the citizen", schema = @Schema()) @RequestBody InitiativeGeneralDTO initiativeGeneralDTO);

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
  ResponseEntity<Void> updateInitiativeBeneficiaryDraft(
      @PathVariable("organizationId") String organizationId,
      @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
      @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, instrument HPAN", schema = @Schema()) @RequestBody InitiativeBeneficiaryRuleDTO body);

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
    ResponseEntity<Void> updateInitiativeBeneficiary(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Unique identifier of the subscribed initiative, instrument HPAN", schema = @Schema()) @RequestBody @Validated(ValidationOnGroup.class) InitiativeBeneficiaryRuleDTO body,
            @RequestParam(required = false) String role);

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
  public ResponseEntity<Void> updateTrxAndRewardRulesDraft(
      @PathVariable("organizationId") String organizationId,
      @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
      @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO);

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
    public ResponseEntity<Void> updateTrxAndRewardRules(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
            @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody @Validated(ValidationOnGroup.class) InitiativeRewardAndTrxRulesDTO rewardAndTrxRulesDTO);

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
    ResponseEntity<Void> updateInitiativeRefundRule(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
            @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody @Validated(ValidationOnGroup.class) InitiativeRefundRuleDTO initiativeRefundRuleDTO);

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
  ResponseEntity<Void> updateInitiativeRefundRuleDraft(
      @PathVariable("organizationId") String organizationId,
      @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
      @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody InitiativeRefundRuleDTO initiativeRefundRuleDTO);

    @Operation(summary = "Update Initiative into APPROVED Status", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
@PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/approved")
    ResponseEntity<Void> updateInitiativeApprovedStatus(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
            @RequestBody InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO);

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
    @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/rejected",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Void> updateInitiativeToCheckStatus(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
            @RequestBody InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO);

    @Operation(summary = "Logically Delete Initiative", description = "", security = {
            @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Initiative ID not found or already deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
    @DeleteMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Void> logicallyDeleteInitiative(
            @PathVariable("organizationId") String organizationId,
            @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
            @RequestBody InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO);

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
  ResponseEntity<InitiativeDTO> getInitiativeBeneficiaryView(
      @PathVariable("initiativeId") String initiativeId);

  @Operation(summary = "Update Initiative into PUBLISHED Status", description = "", security = {
      @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "No Content"),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
  @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/published",
      produces = {"application/json"},
      consumes = {"application/json"})
  ResponseEntity<Void> updateInitiativePublishedStatus(
      @PathVariable("organizationId") String organizationId,
      @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
      @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestBody InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO,
      @RequestParam(required = false) String role);

  @Operation(summary = "Add logo to initiative", description = "", security = {
          @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "No Content"),
          @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
          @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
          @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
          @ApiResponse(responseCode = "404", description = "The requested ID was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
          @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
          @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
  @PutMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/logo",
          produces = {"application/json"},
          consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  ResponseEntity<Void> addLogo(
          @PathVariable("organizationId") String organizationId,
          @Parameter(in = ParameterIn.PATH, description = "The initiative ID", required = true, schema = @Schema()) @PathVariable("initiativeId") String initiativeId,
          @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @RequestPart("logo") MultipartFile logo);

  @Operation(summary = "Return the initiative ID by the service ID", description = "", security = {
      @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InitiativeDTO.class))),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "404", description = "Initiative ID not found for this service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
  @GetMapping(value = "/idpay/initiative",
      produces = {"application/json"})
  ResponseEntity<InitiativeDTO> getInitiativeIdFromServiceId(
      @RequestParam(required = true) String serviceId);

  @Operation(summary = "Return primary and secondary token of the specified initiative", description = "", security = {
      @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InitiativeAdditionalDTO.class))),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "404", description = "Initiative ID not found for this service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
  @GetMapping(value = "/idpay/initiative/{initiativeId}/token",
      produces = {"application/json"})
  ResponseEntity<InitiativeAdditionalDTO> getPrimaryAndSecondaryTokenIO(
      @PathVariable("initiativeId") String initiativeId);

  @Operation(summary = "Return list onboarding status of the specified initiative", description = "", security = {
      @SecurityRequirement(name = "Bearer")}, tags = {"initiative"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InitiativeAdditionalDTO.class))),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "404", description = "Initiative ID not found for this service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "429", description = "Too many Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
      @ApiResponse(responseCode = "500", description = "Server ERROR", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))})
  @GetMapping(value = "/idpay/organization/{organizationId}/initiative/{initiativeId}/onboardings",
      produces = {"application/json"})
  ResponseEntity<OnboardingDTO> getOnboardingStatus(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("initiativeId") String initiativeId,
      @PageableDefault(size = 15) Pageable pageable,
      @RequestParam(required = false) String beneficiary,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
      @RequestParam(required = false) String state);
}

