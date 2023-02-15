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
    private Integer version;
    @JsonProperty("service_name")
    private String serviceName;
    @JsonProperty("is_visible")
    private Boolean isVisible;
    @JsonProperty("department_name")
    private String departmentName;
    @JsonProperty("organization_name")
    private String organizationName;
    @JsonProperty("organization_fiscal_code")
    private String organizationFiscalCode;
    @JsonProperty("require_secure_channels")
    private Boolean requireSecureChannels;
    @JsonProperty("authorized_cidrs")
    private final List<String> authorizedCidrs = new ArrayList<>();
    @JsonProperty("authorized_recipients")
    private List<String> authorizedRecipients = new ArrayList<>();
    @JsonProperty("service_metadata")
    private ServiceMetadataDTO serviceMetadata;


}

