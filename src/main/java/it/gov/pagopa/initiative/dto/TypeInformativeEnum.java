package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.*;

// BND-1883: informative self declaration criteria type (dto side)
/**
 * Gets or Sets _type
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum TypeInformativeEnum {

    @JsonProperty("informative")
    INFORMATIVE();

    @JsonProperty("_type")
    private final String type;

    @Override
    public String toString() {
        return type;
    }

    TypeInformativeEnum() {
        this.type = "informative";
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeInformativeEnum fromValue(String type) {
        for (TypeInformativeEnum b : TypeInformativeEnum.values()) {
            if (String.valueOf(b.type).equals(type)) {
                return b;
            }
        }
        return null;
    }
}

