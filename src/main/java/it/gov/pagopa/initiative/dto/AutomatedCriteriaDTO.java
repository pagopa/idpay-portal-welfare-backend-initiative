package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.initiative.model.IseeTypologyEnum;
import it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary.IseeCodeMustHaveFieldNullConstraint;
import it.gov.pagopa.initiative.utils.constraint.SecondValueGreaterThanFirstWithBTW;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.*;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * AutomatedCriteriaDTO
 */
@Data @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
@Builder
@SecondValueGreaterThanFirstWithBTW(groups = ValidationApiEnabledGroup.class)
@IseeCodeMustHaveFieldNullConstraint(groups = ValidationApiEnabledGroup.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutomatedCriteriaDTO {

  private String authority;

  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String code;

  private String field;

  @NotNull(groups = ValidationApiEnabledGroup.class)
  private FilterOperatorEnum operator;

  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String value;

  private String value2;

  @Nullable
  private OrderDirection orderDirection;
  private List<IseeTypologyEnum> iseeTypes;

  public enum OrderDirection {
    ASC,
    DESC;

    OrderDirection() {
    }

    public boolean isAscending() {
      return this.equals(ASC);
    }

    public boolean isDescending() {
      return this.equals(DESC);
    }
  }

}
