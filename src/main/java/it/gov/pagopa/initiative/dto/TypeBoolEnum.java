package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets _type
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeBoolEnum {

    @JsonProperty("boolean")
    BOOLEAN("boolean");

    @JsonProperty("_type")
    private String type;

    @Override
    public String toString() {
        return String.valueOf(type);
    }

    TypeBoolEnum(String type) {
        this.type = type;
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
