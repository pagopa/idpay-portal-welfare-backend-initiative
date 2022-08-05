package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
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
import static org.junit.jupiter.api.Assertions.*;

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
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void when_rewardGroupsDTOAreValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTO_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);

    }


    @Test
    void when_RewardValueDTOareValid_thenValidationIsPassed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO_ok();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertTrue(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(0);

    }

    @Test
    void when_rewardValueDTOTooBigAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTOvalueTooBig_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void when_rewardValueDTOTooSmallAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTOvalueTooSmall_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void when_rewardValueDTORateAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTORateNotValid_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    void when_rewardValueDTOFromAndToAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTOFromAndToNotValid_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationOnGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(2);
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
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(-10), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(40), BigDecimal.valueOf(20), BigDecimal.valueOf(40));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO_ok(){
        RewardValueDTO rewardValueDTO = new RewardValueDTO(BigDecimal.valueOf(50));
        return rewardValueDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTOvalueTooBig_ko(){
        RewardValueDTO rewardValueDTO = new RewardValueDTO(BigDecimal.valueOf(150));
        return rewardValueDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTOvalueTooSmall_ko(){
        RewardValueDTO rewardValueDTO = new RewardValueDTO(BigDecimal.valueOf(-10));
        return rewardValueDTO;
    }
}
