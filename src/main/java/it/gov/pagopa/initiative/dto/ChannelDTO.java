package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * ChannelDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO   {
  @JsonProperty("type")
  @NotNull(groups = ValidationApiEnabledGroup.class)
  private TypeEnum type;

  @JsonProperty("contact")
  @NotBlank(groups = ValidationApiEnabledGroup.class)
  private String contact;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    WEB("web"),
    EMAIL("email"),
    MOBILE("mobile");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  private static final String VALID_WEB = "^https://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
  @AssertTrue(message = "Invalid contact format for the web type", groups = ValidationApiEnabledGroup.class)
  public boolean isContactValid() {
    if (type == TypeEnum.WEB)
      return contact.matches(VALID_WEB);
    else
      return true;
  }

}
