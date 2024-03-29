package it.gov.pagopa.initiative.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterOperatorEnum {
    EQ("EQ"),
    NOT_EQ("NOT_EQ"),
    LT("LT"),
    LE("LE"),
    GT("GT"),
    GE("GE"),
    INSTANCE_OF("INSTANCE_OF"),
    BTW_CLOSED("BTW_CLOSED"),
    BTW_OPEN("BTW_OPEN");

    private final String value;

    FilterOperatorEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static FilterOperatorEnum fromValue(String text) {
        for (FilterOperatorEnum b : FilterOperatorEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
