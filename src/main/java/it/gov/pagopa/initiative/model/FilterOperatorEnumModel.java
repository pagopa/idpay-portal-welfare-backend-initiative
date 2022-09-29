package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterOperatorEnumModel {
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

    FilterOperatorEnumModel(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static FilterOperatorEnumModel fromValue(String text) {
        for (FilterOperatorEnumModel b : FilterOperatorEnumModel.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
