package it.gov.pagopa.initiative.dto.io.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A payload used to create/update a service for a user.
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequestDTO {
    @JsonProperty("name")
    private String serviceName;
    private String description;
    private OrganizationDTO organization;
    @JsonProperty("require_secure_channel")
    private Boolean requireSecureChannels;
    @JsonProperty("authorized_cidrs")
    private final List<String> authorizedCidrs = new ArrayList<>();
    @JsonProperty("authorized_recipients")
    private List<String> authorizedRecipients = new ArrayList<>();
    @JsonProperty("metadata")
    private ServiceRequestMetadataDTO serviceMetadata;

}

