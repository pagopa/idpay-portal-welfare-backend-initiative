package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Channel {
    @JsonProperty("type")
    private TypeEnum type;

    @JsonProperty("contact")
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
}
