package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class AutomatedCriteria {

  private String authority;
  private String code ;
  private String field;
  private FilterOperatorEnumModel operator;
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
