package it.gov.pagopa.initiative.utils.validator.initiative;

import it.gov.pagopa.initiative.dto.ChannelDTO;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.utils.validator.URLValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatedURL implements
        ConstraintValidator<URLValidation, InitiativeAdditionalDTO> {

    private static final String TYPE_WEB = "web";
    private static final String TYPE_EMAIL = "email";
    private static final String TYPE_MOBILE = "mobile";
    private static final String VALID_LINK = "^(https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static final String VALID_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String VALID_MOBILE = "^[+]?\\d+";

    @Override
    public boolean isValid(InitiativeAdditionalDTO value, ConstraintValidatorContext context) {
        List<ChannelDTO> list = value.getChannels();

        boolean valid = true;

        for(ChannelDTO c : list){
            String code = c.getType().toString();
            if (notValid(c, code)) return false;
        }

        return valid;
    }

    private static boolean notValid(ChannelDTO c, String code) {
        if(TYPE_WEB.equals(code)){
            Pattern patt = Pattern.compile(VALID_LINK);
            Matcher matcher = patt.matcher(c.getContact());
            if(!matcher.matches())
                return true;
        }
        if(TYPE_EMAIL.equals(code)){
            Pattern patt = Pattern.compile(VALID_EMAIL);
            Matcher matcher = patt.matcher(c.getContact());
            if(!matcher.matches())
                return true;
        }
        if(TYPE_MOBILE.equals(code)){
            Pattern patt = Pattern.compile(VALID_MOBILE);
            Matcher matcher = patt.matcher(c.getContact());
            return !matcher.matches();
        }
        return false;
    }

}
