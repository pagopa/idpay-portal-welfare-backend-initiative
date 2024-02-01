package it.gov.pagopa.initiative.utils.validator.initiative;

import it.gov.pagopa.initiative.dto.ChannelDTO;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.utils.validator.URLValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatedURL implements
        ConstraintValidator<URLValidation, InitiativeAdditionalDTO> {

    private static final Map<String, String> typeAndRegex = Map.of(
            "web", "^(https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
            "email", "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            "mobile", "^[+]?\\d+");

    @Override
    public boolean isValid(InitiativeAdditionalDTO value, ConstraintValidatorContext context) {
        for(ChannelDTO c : value.getChannels()){
            if (!validContact(c, c.getType().toString())) return false;
        }
        return true;
    }

    private static boolean validContact(ChannelDTO c, String code) {
        if(!typeAndRegex.containsKey(code)) return false;

        Pattern patt = Pattern.compile(typeAndRegex.get(code));
        Matcher matcher = patt.matcher(c.getContact());
        return matcher.matches();
    }

}
