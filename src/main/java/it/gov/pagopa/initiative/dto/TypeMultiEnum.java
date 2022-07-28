package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.*;

/**
 * Gets or Sets _type
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum TypeMultiEnum {

    @JsonProperty("multi")
    MULTI("multi");

    @JsonProperty("_type")
    private String type;

    @Override
    public String toString() {
        return String.valueOf(type);
    }

    TypeMultiEnum(String type) {
        this.type = type;
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
