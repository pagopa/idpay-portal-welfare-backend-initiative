package it.gov.pagopa.initiative.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IseeTypologyEnum {
    ORDINARIO("Ordinario"),
    MINORENNE("Minorenne"),
    UNIVERSITARIO("Universitario"),
    SOCIOSANITARIO("SocioSanitario"),
    DOTTORATO("Dottorato"),
    RESIDENZIALE("Residenziale"),
    CORRENTE("Corrente");

    private final String value;
    IseeTypologyEnum(String value) {
        this.value = value;
    }
    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static IseeTypologyEnum fromValue(String text) {
        for (IseeTypologyEnum b : IseeTypologyEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
