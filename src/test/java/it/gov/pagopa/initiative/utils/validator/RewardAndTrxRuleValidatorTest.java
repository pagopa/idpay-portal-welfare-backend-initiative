package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeRewardAndTrxRulesDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RewardAndTrxRuleValidatorTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void when_DayConfigHasWrongIntervals_thenValidationFailed(){
        DayOfWeekDTO.DayConfig dayConfig = createDayConfigWithWrongIntervalsItems();
        Set<ConstraintViolation<DayOfWeekDTO.DayConfig>> violations = validator.validate(dayConfig, ValidationApiEnabledGroup.class);
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_IntervalStartTimeIsNullAndEndTimeIsNotNull(){
        DayOfWeekDTO.Interval interval = createIntervalWithEndTimeNull();
        Set<ConstraintViolation<DayOfWeekDTO.Interval>> violations = validator.validate(interval, ValidationApiEnabledGroup.class);
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_RewardGroupsDTOAreValid_thenValidationIsPassed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTO_ok();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_rewardGroupsDTOAreValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTO_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);

    }

    @Test
    void when_RewardValueDTOareValid_thenValidationIsPassed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO_ok();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void given_typeNULL_when_RewardValueDTO_thenValidationHas1Violation(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO_No_typeField();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_rewardValueDTOTooBigAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTOvalueTooBig_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_rewardValueDTOTooSmallAreNotValid_thenValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTOvalueTooSmall_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_rewardValueDTO_RateAreNotValid_then_ValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTORateNotValid_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void when_rewardValueDTO_FromAndToAreNotValid_then_ValidationAreFailed(){
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupsDTOFromAndToNotValid_ko();
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violations = validator.validate(initiativeRewardRuleDTO, ValidationApiEnabledGroup.class);
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
        Set<ConstraintViolation<TrxCountDTO>> violations = validator.validate(trxCountDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_trxCount_FromIsGreaterThanTo_then_ValidationAreFailed(){
        TrxCountDTO trxCountDTO = TrxCountDTO.builder()
                .from(10L)
                .to(0L)
                .fromIncluded(false)
                .toIncluded(true).build();
        Set<ConstraintViolation<TrxCountDTO>> violations = validator.validate(trxCountDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }
    @Test
    void when_trxCount_onlyFrom_then_ValidationOk(){
        TrxCountDTO trxCountDTO = TrxCountDTO.builder()
                .from(10L)
                .fromIncluded(false).build();
        Set<ConstraintViolation<TrxCountDTO>> violations = validator.validate(trxCountDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_trxCount_onlyTo_then_ValidationOk(){
        TrxCountDTO trxCountDTO = TrxCountDTO.builder()
                .to(10L)
                .toIncluded(false).build();
        Set<ConstraintViolation<TrxCountDTO>> violations = validator.validate(trxCountDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }
    @Test
    void when_threshold_FromIsLessThanTo_then_ValidationAreOk(){
        ThresholdDTO thresholdDTO = ThresholdDTO.builder()
                .from(BigDecimal.ONE)
                .to(BigDecimal.TEN)
                .fromIncluded(false)
                .toIncluded(true).build();
        Set<ConstraintViolation<ThresholdDTO>> violations = validator.validate(thresholdDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_threshold_FromIsGreaterThanTo_then_ValidationAreFailed(){
        ThresholdDTO thresholdDTO = ThresholdDTO.builder()
                .from(BigDecimal.TEN)
                .to(BigDecimal.ZERO)
                .fromIncluded(false)
                .toIncluded(true).build();
        Set<ConstraintViolation<ThresholdDTO>> violations = validator.validate(thresholdDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void when_DaysOfWeekIntervalsIsValid_thenValidationIsPassed(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOValid_ok();
        Set<ConstraintViolation<InitiativeTrxConditionsDTO>> violations = validator.validate(initiativeTrxConditionsDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void when_DaysOfWeekIntervalsIsEmpty_thenValidationAreFailed(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTODaysOfWeekIntervarlsEmpty_ko();
        Set<ConstraintViolation<InitiativeTrxConditionsDTO>> violations = validator.validate(initiativeTrxConditionsDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(2);
    }

    @Test
    void when_DaysOfWeekIntervalsAreNotValid_thenValidationAreFailed(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTODaysOfWeekIntervalsNotValid_ko();
        Set<ConstraintViolation<InitiativeTrxConditionsDTO>> violations = validator.validate(initiativeTrxConditionsDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_ThresholdFromAndToAreTooSmall_thenValidationAreFailed(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOThresholdFromAndToTooSmall_ko();
        Set<ConstraintViolation<InitiativeTrxConditionsDTO>> violations = validator.validate(initiativeTrxConditionsDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(3);
    }

    @Test
    void when_TrxRuleFromAndToAreTooSmall_thenValidationAreFailed(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOTrxRuleFromAndToTooSmall_ko();
        Set<ConstraintViolation<InitiativeTrxConditionsDTO>> violations = validator.validate(initiativeTrxConditionsDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(3);
    }

    @Test
    void when_RewardRuleIsNull_thenValidationAreFailed(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTOWithRewardRuleNull_ko();
        Set<ConstraintViolation<InitiativeRewardAndTrxRulesDTO>> violations = validator.validate(initiativeRewardAndTrxRulesDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_TrxRuleIsNull_thenValidationAreFailed(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTOWithTrxRuleNull_ko();
        Set<ConstraintViolation<InitiativeRewardAndTrxRulesDTO>> violations = validator.validate(initiativeRewardAndTrxRulesDTO, ValidationApiEnabledGroup.class);
        assertFalse(violations.isEmpty());
        assertThat(violations).hasSize(1);
    }

    @Test
    void when_InitiativeRewardAndTrxRulesDTOIsValid_thenValidationIsPassed(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTO_ok();
        Set<ConstraintViolation<InitiativeRewardAndTrxRulesDTO>> violations = validator.validate(initiativeRewardAndTrxRulesDTO, ValidationApiEnabledGroup.class);
        assertTrue(violations.isEmpty());
    }

    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTO_ok(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        rewardGroupsDTO.setType("rewardGroups");
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(1000L, 2000L, BigDecimal.valueOf(30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(1000L, 3000L, BigDecimal.valueOf(40));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTO_ko(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        rewardGroupsDTO.setType("rewardGroups");
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(1000L , 2000L, BigDecimal.valueOf(30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(1000L,2000L, BigDecimal.valueOf(40));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }

    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTORateNotValid_ko(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        rewardGroupsDTO.setType("rewardGroups");
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(1000L, 2000L, BigDecimal.valueOf(230));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(500L,2000L, BigDecimal.valueOf(140));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupsDTOFromAndToNotValid_ko(){
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        rewardGroupsDTO.setType("rewardGroups");
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO1 = new RewardGroupsDTO.RewardGroupDTO(-1000L, -2000L, BigDecimal.valueOf(-30));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO2 = new RewardGroupsDTO.RewardGroupDTO(3000L, 2000L, BigDecimal.valueOf(40));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO3 = new RewardGroupsDTO.RewardGroupDTO(3000L, 2000L, BigDecimal.valueOf(40));
        RewardGroupsDTO.RewardGroupDTO rewardGroupDTO4 = new RewardGroupsDTO.RewardGroupDTO(4000L, 2000L, BigDecimal.valueOf(40));
        List<RewardGroupsDTO.RewardGroupDTO> rewardGroupDTOList = new ArrayList<>();
        rewardGroupDTOList.add(rewardGroupDTO1);
        rewardGroupDTOList.add(rewardGroupDTO2);
        rewardGroupDTOList.add(rewardGroupDTO3);
        rewardGroupDTOList.add(rewardGroupDTO4);
        rewardGroupsDTO.setRewardGroups(rewardGroupDTOList);
        return rewardGroupsDTO;
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO_ok(){
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .type("rewardValue")
                .build();
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO_No_typeField(){
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .build();
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTOvalueTooBig_ko(){
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(150))
                .type("rewardValue")
                .build();
    }
    InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTOvalueTooSmall_ko(){
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(-10))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .type("rewardValue")
                .build();
    }

    InitiativeTrxConditionsDTO creatInitiativeTrxConditionsDTODaysOfWeekIntervarlsEmpty_ko(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    InitiativeTrxConditionsDTO creatInitiativeTrxConditionsDTODaysOfWeekIntervalsNotValid_ko(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        DayOfWeekDTO.Interval interval2 = new DayOfWeekDTO.Interval();
        LocalTime t3 = LocalTime.of(6, 0, 0);
        LocalTime t4 = LocalTime.of(12, 0, 0);
        interval2.setStartTime(t3);
        interval2.setEndTime(t4);
        intervals.add(interval1);
        intervals.add(interval2);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }


    InitiativeTrxConditionsDTO creatInitiativeTrxConditionsDTOThresholdFromAndToTooSmall_ko(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(-1));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(-1));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }


    InitiativeTrxConditionsDTO creatInitiativeTrxConditionsDTOTrxRuleFromAndToTooSmall_ko(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(0L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(0L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }


    InitiativeTrxConditionsDTO creatInitiativeTrxConditionsDTOValid_ok(){
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }
    InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTOWithRewardRuleNull_ko(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOValid_ok();
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.DISCOUNT);
        initiativeRewardAndTrxRulesDTO.setRewardRule(null);
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }
    InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTOWithTrxRuleNull_ko(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO_ok();
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.DISCOUNT);
        initiativeRewardAndTrxRulesDTO.setRewardRule(initiativeRewardRuleDTO);
        initiativeRewardAndTrxRulesDTO.setTrxRule(null);
        return initiativeRewardAndTrxRulesDTO;
    }

    InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTO_ok(){
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO_ok();
        initiativeRewardAndTrxRulesDTO.setRewardRule(initiativeRewardRuleDTO);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = creatInitiativeTrxConditionsDTOValid_ok();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    private DayOfWeekDTO.DayConfig createDayConfigWithWrongIntervalsItems(){
        DayOfWeekDTO.DayConfig dayConfig = new DayOfWeekDTO.DayConfig();

        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);

        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        intervals.add( new DayOfWeekDTO.Interval(null, LocalTime.of(6,0,0)));
        intervals.add( new DayOfWeekDTO.Interval(LocalTime.of(7,0,0), LocalTime.of(22,0,0)));

        dayConfig.setDaysOfWeek(dayOfWeeks);
        dayConfig.setIntervals(intervals);
        return dayConfig;
    }

    private DayOfWeekDTO.Interval createIntervalWithEndTimeNull(){
        return new DayOfWeekDTO.Interval(LocalTime.of(10, 0, 0), null);
    }

}
