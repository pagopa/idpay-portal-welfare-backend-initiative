package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.ToString;

/**
 * Gets or Sets _type
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeEnum {

    @JsonProperty("boolean")
    BOOLEAN("boolean"),
    @JsonProperty("multi")
    MULTI("multi");

    @JsonProperty("_type")
    private String type;

    @Override
    public String toString() {
        return String.valueOf(type);
    }

    TypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeEnum fromValue(String type) {
        for (TypeEnum b : TypeEnum.values()) {
            if (String.valueOf(b.type).equals(type)) {
                return b;
            }
        }
        return null;
    }
}
