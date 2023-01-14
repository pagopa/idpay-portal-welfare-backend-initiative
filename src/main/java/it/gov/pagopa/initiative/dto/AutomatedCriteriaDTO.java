package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary.IseeCodeMustHaveFieldNullConstraint;
import it.gov.pagopa.initiative.utils.constraint.SecondValueGreaterThanFirstWithBTW;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * AutomatedCriteriaDTO
 */
@Data @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
@Builder
@SecondValueGreaterThanFirstWithBTW(groups = ValidationOnGroup.class)
@IseeCodeMustHaveFieldNullConstraint(groups = ValidationOnGroup.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutomatedCriteriaDTO {

  private String authority;

  @NotBlank(groups = ValidationOnGroup.class)
  private String code;

  private String field;

  @NotNull(groups = ValidationOnGroup.class)
  private FilterOperatorEnum operator;

  @NotBlank(groups = ValidationOnGroup.class)
  private String value;

  private String value2;

  @Nullable
  private OrderDirection orderDirection;

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
