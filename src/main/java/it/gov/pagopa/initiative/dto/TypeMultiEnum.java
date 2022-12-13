package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.*;

/**
 * Gets or Sets _type
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum TypeMultiEnum {

    @JsonProperty("multi")
    MULTI();

    @JsonProperty("_type")
    private final String type;

    @Override
    public String toString() {
        return type;
    }

    TypeMultiEnum() {
        this.type = "multi";
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeMultiEnum fromValue(String type) {
        for (TypeMultiEnum b : TypeMultiEnum.values()) {
            if (String.valueOf(b.type).equals(type)) {
                return b;
            }
        }
        return null;
    }
}
