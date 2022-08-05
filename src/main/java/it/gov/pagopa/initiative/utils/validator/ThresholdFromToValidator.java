package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.ThresholdDTO;
import it.gov.pagopa.initiative.utils.constraint.ThresholdFromToValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ThresholdFromToValidator implements ConstraintValidator<ThresholdFromToValue, ThresholdDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private String _from;
    private String _to;

    @Override
    public void initialize(ThresholdFromToValue constraintAnnotation) {
        _from = constraintAnnotation.from();
        _to = constraintAnnotation.to();
    }

    @Override
    public boolean isValid(ThresholdDTO value, ConstraintValidatorContext context) {
        BigDecimal fromTmp = (BigDecimal)  PARSER.parseExpression(_from).getValue(value);
        BigDecimal toTmp = (BigDecimal)  PARSER.parseExpression(_to).getValue(value);
        return fromTmp.compareTo(toTmp) == -1;
    }
}
