package it.gov.pagopa.initiative.dto.rule.refund;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundAdditionalInfoDTO {
    @NotNull(groups = ValidationOnGroup.class)
    @JsonProperty("identificationCode")
    private String identificationCode;
}
