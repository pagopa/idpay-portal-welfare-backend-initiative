package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.*;

/**
 * Gets or Sets _type
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum TypeTextEnum {

    @JsonProperty("text")
    TEXT();

    @JsonProperty("_type")
    private final String type;

    @Override
    public String toString() {
        return type;
    }

    TypeTextEnum() {
        this.type = "text";
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeTextEnum fromValue(String type) {
        for (TypeTextEnum b : TypeTextEnum.values()) {
            if (String.valueOf(b.type).equals(type)) {
                return b;
            }
        }
        return null;
    }
}
