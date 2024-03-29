package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.utils.constraint.RewardGroupFromToValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class RewardGroupFromToValidator implements ConstraintValidator<RewardGroupFromToValue, RewardGroupsDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private String groupValueList;

    @Override
    public void initialize(RewardGroupFromToValue constraintAnnotation) {
        groupValueList = constraintAnnotation.listOfGroupValue();
    }

    @Override
    public boolean isValid(RewardGroupsDTO value, ConstraintValidatorContext context) {
        List<RewardGroupsDTO.RewardGroupDTO> listTmp = (List<RewardGroupsDTO.RewardGroupDTO>)PARSER.parseExpression(groupValueList).getValue(value);
        assert listTmp != null;
        if (listTmp.isEmpty()){
            return false;
        }
        for (int j = 0; j < listTmp.size()-1; j++){
            if(listTmp.get(j).getFrom().compareTo(listTmp.get(j).getTo()) > 0)
                return false;
            for (int i = j+1; i <= listTmp.size()-1; i++){
                if ((listTmp.get(j).getFrom().equals(listTmp.get(i).getFrom()) && listTmp.get(j).getTo().equals(listTmp.get(i).getTo()))){
                    return false;
                }
            }
        }
        return true;
    }
}
