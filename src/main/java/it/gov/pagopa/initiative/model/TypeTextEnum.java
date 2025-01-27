package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets _type
 */
public enum TypeTextEnum {

    TEXT();

    private final String value;

    TypeTextEnum() {
        this.value = "text";
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static TypeTextEnum fromValue(String text) {
        for (TypeTextEnum b : TypeTextEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
