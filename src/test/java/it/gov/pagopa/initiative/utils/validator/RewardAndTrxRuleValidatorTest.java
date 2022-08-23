package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.InitiativeTrxConditionsDTO;
import it.gov.pagopa.initiative.dto.rule.trx.ThresholdDTO;
import it.gov.pagopa.initiative.dto.rule.trx.TrxCountDTO;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class RewardAndTrxRuleValidatorTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void when_RewardGroupsDTOAreValid_thenValidationIsPassed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTO_ok();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_rewardGroupsDTOAreValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTO_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);

    }


    @Test
    void when_RewardValueDTOareValid_thenValidationIsPassed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO_ok();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.isEmpty());

    }

    @Test
    void when_rewardValueDTOTooBigAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTOvalueTooBig_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_rewardValueDTOTooSmallAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTOvalueTooSmall_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_rewardValueDTO_RateAreNotValid_then_ValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTORateNotValid_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void when_rewardValueDTO_FromAndToAreNotValid_then_ValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTOFromAndToNotValid_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(4);
    }

    @Test
    void when_trxCount_FromIsLessThanTo_then_ValidationAreOk(){
        TrxCountDTO trxCountDTO = TrxCountDTO.builder()
                .from(1L)
                .to(10L)
                .fromIncluded(false)
                .toIncluded(true).build();
        Set<ConstraintViolation<TrxCountDTO>> violations = validator.validate(trxCountDTO, ValidationOnGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_trxCount_FromIsGreaterThanTo_then_ValidationAreFailed(){
        TrxCountDTO trxCountDTO = TrxCountDTO.builder()
                .from(10L)
                .to(0L)
                .fromIncluded(false)
                .toIncluded(true).build();
        Set<ConstraintViolation<TrxCountDTO>> violations = validator.validate(trxCountDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void when_threshold_FromIsLessThanTo_then_ValidationAreOk(){
        ThresholdDTO thresholdDTO = ThresholdDTO.builder()
                .from(BigDecimal.ONE)
                .to(BigDecimal.TEN)
                .fromIncluded(false)
                .toIncluded(true).build();
        Set<ConstraintViolation<ThresholdDTO>> violations = validator.validate(thresholdDTO, ValidationOnGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_threshold_FromIsGreaterThanTo_then_ValidationAreFailed(){
        ThresholdDTO thresholdDTO = ThresholdDTO.builder()
                .from(BigDecimal.TEN)
                .to(BigDecimal.ZERO)
                .fromIncluded(false)
                .toIncluded(true).build();
        Set<ConstraintViolation<ThresholdDTO>> violations = validator.validate(thresholdDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }


    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTO_ok(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(40));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTO_ko(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(40));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }

    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTORateNotValid_ko(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(20), BigDecimal.valueOf(230));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(5), BigDecimal.valueOf(20), BigDecimal.valueOf(140));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTOFromAndToNotValid_ko(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(-10), BigDecimal.valueOf(-20), BigDecimal.valueOf(-30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(40), BigDecimal.valueOf(20), BigDecimal.valueOf(40));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO3 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(30), BigDecimal.valueOf(20), BigDecimal.valueOf(40));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO4 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(40), BigDecimal.valueOf(20), BigDecimal.valueOf(40));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupDTOList.add(rewardGroupDTO3);
        rewardGroupDTOList.add(rewardGroupDTO4);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO_ok(){
        return new RewardValueDTO(BigDecimal.valueOf(50));
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTOvalueTooBig_ko(){
        return new RewardValueDTO(BigDecimal.valueOf(150));
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTOvalueTooSmall_ko(){
        return new RewardValueDTO(BigDecimal.valueOf(-10));
    }
    void createInitiativeTrxCondition(InitiativeRewardRuleDTO initiativeRewardRuleDTO, InitiativeTrxConditionsDTO initiativeTrxConditionsDTO){

    }
}
