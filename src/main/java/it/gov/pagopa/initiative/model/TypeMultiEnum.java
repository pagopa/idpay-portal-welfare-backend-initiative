package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets _type
 */
public enum TypeMultiEnum {

    MULTI();

    private final String value;

    TypeMultiEnum() {
        this.value = "multi";
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static TypeMultiEnum fromValue(String text) {
        for (TypeMultiEnum b : TypeMultiEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
