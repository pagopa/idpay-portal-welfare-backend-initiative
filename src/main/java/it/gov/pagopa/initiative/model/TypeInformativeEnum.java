package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// BND-1883: informative self declaration criteria type (model side)
/**
 * Gets or Sets _type
 */
public enum TypeInformativeEnum {

    INFORMATIVE();

    private final String value;

    TypeInformativeEnum() {
        this.value = "informative";
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static TypeInformativeEnum fromValue(String text) {
        for (TypeInformativeEnum b : TypeInformativeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

