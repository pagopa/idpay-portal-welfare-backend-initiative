package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.*;

/**
 * Gets or Sets _type
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum TypeBoolEnum {

    @JsonProperty("boolean")
    BOOLEAN();

    @JsonProperty("_type")
    private final String type;

    @Override
    public String toString() {
        return type;
    }

    TypeBoolEnum() {
        this.type = "boolean";
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeBoolEnum fromValue(String type) {
        for (TypeBoolEnum b : TypeBoolEnum.values()) {
            if (String.valueOf(b.type).equals(type)) {
                return b;
            }
        }
        return null;
    }
}
