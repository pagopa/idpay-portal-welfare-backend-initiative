package it.gov.pagopa.initiative.dto.io.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseDTO {

    private String id;
    private Integer version;
    @JsonProperty("service_id")
    private String serviceId;
    @JsonProperty("service_name")
    private String serviceName;
    @JsonProperty("service_metadata")
    private ServiceMetadataDTO serviceMetadata;
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
    @JsonProperty("max_allowed_payment_amount")
    private Integer maxAllowedPaymentAmount;
    @JsonProperty("authorized_recipients")
    private List<String> authorizedRecipients;
    @JsonProperty("authorized_cidrs")
    private List<String> authorizedCidrs;
    @JsonProperty("primary_key")
    private String primaryKey;
    @JsonProperty("secondary_key")
    private String secondaryKey;

}



