package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets _type
 */
public enum TypeBoolEnum {

    BOOLEAN();

    private final String value;

    TypeBoolEnum() {
        this.value = "boolean";
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static TypeBoolEnum fromValue(String text) {
        for (TypeBoolEnum b : TypeBoolEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
