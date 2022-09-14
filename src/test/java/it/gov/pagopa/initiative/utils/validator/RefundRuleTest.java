package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class RefundRuleTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void refundRuleValidWithTimeParameter_ok(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleValidWithTimeParameter_ok();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void refundRuleValidWithAccumulatedAmount_ok(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleValidWithAccumulatedAmount_ok();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void refundRuleWithBothRefundNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleWithBothRefundNotValid_ko();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void refundRuleWithBothRefundNullNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundWithBothRefundNullNotValid_ko();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void refundRuleWithAccumulatedAmountNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleWithAccumulatedAmountNotValid_ko();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void refundRuleWithEmptyAccumulatedAmountNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleWithEmptyAccumulatedAmountNotValid_ko();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void refundRuleWithEmptyTimeParameterNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleWithEmptyTimeParameterNotValid_ko();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void refundRuleWithEmptyAdditionalInfoNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = createRefundRuleWithEmptyAdditionalInfoNotValid_ko();
        Set<ConstraintViolation<InitiativeRefundRuleDTO>> violations = validator.validate(refundRuleDTO, ValidationOnGroup.class);

        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }


    private InitiativeRefundRuleDTO createRefundRuleValidWithTimeParameter_ok(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterValid_ok());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid_ok());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleValidWithAccumulatedAmount_ok(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountValid_ok());
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid_ok());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithBothRefundNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountValid_ok());
        refundRuleDTO.setTimeParameter(createTimeParameterValid_ok());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid_ok());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundWithBothRefundNullNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid_ok());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithAccumulatedAmountNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountNotValid_ko());
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid_ok());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithEmptyAccumulatedAmountNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountNotValidNull_ko());
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid_ok());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithEmptyTimeParameterNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterNotValidNull_ko());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid_ok());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleWithEmptyAdditionalInfoNotValid_ko(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterValid_ok());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTONotValidNull_ko());
        return refundRuleDTO;
    }


    private AccumulatedAmountDTO createAccumulatedAmountValid_ok(){
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
    }

    private TimeParameterDTO createTimeParameterValid_ok(){
        TimeParameterDTO timeParameterDTO = new TimeParameterDTO();
        timeParameterDTO.setTimeType(TimeParameterDTO.TimeTypeEnum.CLOSED);
        return timeParameterDTO;
    }

    private RefundAdditionalInfoDTO createAdditionalInfoDTOValid_ok(){
        RefundAdditionalInfoDTO refundAdditionalInfoDTO = new RefundAdditionalInfoDTO();
        refundAdditionalInfoDTO.setIdentificationCode("B002");
        return refundAdditionalInfoDTO;
    }

    private AccumulatedAmountDTO createAccumulatedAmountNotValid_ko(){
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.BUDGET_EXHAUSTED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
    }

    private AccumulatedAmountDTO createAccumulatedAmountNotValidNull_ko(){
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(null);
        amountDTO.setRefundThreshold(null);
        return amountDTO;
    }

    private TimeParameterDTO createTimeParameterNotValidNull_ko(){
        TimeParameterDTO timeParameterDTO = new TimeParameterDTO();
        timeParameterDTO.setTimeType(null);
        return timeParameterDTO;
    }
    private RefundAdditionalInfoDTO createAdditionalInfoDTONotValidNull_ko(){
        RefundAdditionalInfoDTO refundAdditionalInfoDTO = new RefundAdditionalInfoDTO();
        refundAdditionalInfoDTO.setIdentificationCode(null);
        return refundAdditionalInfoDTO;
    }
}
