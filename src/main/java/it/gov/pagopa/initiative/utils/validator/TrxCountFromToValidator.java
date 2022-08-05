package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.TrxCountDTO;
import it.gov.pagopa.initiative.utils.constraint.TrxCountFromToValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class TrxCountFromToValidator implements ConstraintValidator<TrxCountFromToValue, TrxCountDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private String _from;
    private String _to;

    @Override
    public void initialize(TrxCountFromToValue constraintAnnotation) {
        _from = constraintAnnotation.from();
        _to = constraintAnnotation.to();
    }

    @Override
    public boolean isValid(TrxCountDTO value, ConstraintValidatorContext context) {
        Long fromTmp = (Long)  PARSER.parseExpression(_from).getValue(value);
        Long toTmp = (Long)  PARSER.parseExpression(_to).getValue(value);
        return fromTmp.compareTo(toTmp) == -1;
    }
}
