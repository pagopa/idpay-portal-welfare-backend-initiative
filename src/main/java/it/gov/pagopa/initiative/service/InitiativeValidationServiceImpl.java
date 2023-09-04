package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.trx.InitiativeTrxConditionsDTO;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.AutomatedCriteria;
import it.gov.pagopa.initiative.model.FilterOperatorEnumModel;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardValue;
import it.gov.pagopa.initiative.model.rule.trx.Threshold;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import it.gov.pagopa.initiative.utils.validator.ValidationApiEnabledGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Validated
public class InitiativeValidationServiceImpl implements InitiativeValidationService {

    private static final String ISEE = "ISEE";
    private static final String STRING_FORMAT_VIOLATION = "[%s - %s]";
    private final InitiativeRepository initiativeRepository;
    private final Validator validator;

    public InitiativeValidationServiceImpl(
            InitiativeRepository initiativeRepository,
            Validator validator
    ) {
        this.initiativeRepository = initiativeRepository;
        this.validator = validator;
    }

    @Override
    public Initiative getInitiative(String organizationId, String initiativeId, String role){
        Initiative initiative = initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(organizationId, initiativeId, true)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        if (InitiativeConstants.Role.PAGOPA_ADMIN.equals(role)){
            if (InitiativeConstants.Status.PUBLISHED.equals(initiative.getStatus()) || initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION) || initiative.getStatus().equals(InitiativeConstants.Status.TO_CHECK) || initiative.getStatus().equals(InitiativeConstants.Status.APPROVED)){
                return initiative;
            }else {
                throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role),
                        HttpStatus.BAD_REQUEST
                );
            }
        }else{
            return initiative;
        }
    }

    @Override
    public void checkPermissionBeforeInsert(String role) {
        log.debug("[CHECK PERMISSION] role: {}", role);
        if (InitiativeConstants.Role.PAGOPA_ADMIN.equals(role)){
            throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role),
                        HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    @Validated(value = ValidationApiEnabledGroup.class)
    public void checkAutomatedCriteria(Initiative initiative, List<AutomatedCriteria> automatedCriteriaList) {
        InitiativeGeneral general = initiative.getGeneral();
        checkIseeTypes(automatedCriteriaList);
        checkIseeCriteriaForNF(initiative, automatedCriteriaList);

        if (Boolean.TRUE.equals(general.getRankingEnabled())){
            boolean checkIsee = false;
            for(AutomatedCriteria automatedCriteria : automatedCriteriaList){
                String code = automatedCriteria.getCode();
                FilterOperatorEnumModel operator = automatedCriteria.getOperator();
                AutomatedCriteria.OrderDirection orderDirection = automatedCriteria.getOrderDirection();
                if(ISEE.equals(code)) {
                    checkIsee=true;
                    if (orderDirection == null) {
                        throw new InitiativeException(
                                InitiativeConstants.Exception.BadRequest.CODE,
                                InitiativeConstants.Exception.BadRequest.INITIATIVE_BENEFICIARY_RANKING_ENABLED_AUTOMATED_CRITERIA_ORDER_OPERATION_MISSING_NOT_VALID,
                                HttpStatus.BAD_REQUEST
                        );
                    } else if (FilterOperatorEnumModel.EQ.equals(operator)) {
                        throw new InitiativeException(
                                InitiativeConstants.Exception.BadRequest.CODE,
                                InitiativeConstants.Exception.BadRequest.INITIATIVE_BENEFICIARY_RANKING_ENABLED_AUTOMATED_CRITERIA_ORDER_OPERATION_ISEE_EQ_OP_NOT_VALID,
                                HttpStatus.BAD_REQUEST
                        );
                    }
                }
            }
            if(!checkIsee){
                throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        InitiativeConstants.Exception.BadRequest.INITIATIVE_BENEFICIARY_RANKING_ENABLED_AUTOMATED_CRITERIA_ISEE_MISSING_NOT_VALID,
                        HttpStatus.BAD_REQUEST
                );
            }
        }
    }

    private void checkIseeTypes(List<AutomatedCriteria> automatedCriteriaList){
        for(AutomatedCriteria automatedCriteria : automatedCriteriaList){
            if(automatedCriteria.getCode().equals(ISEE) && CollectionUtils.isEmpty(automatedCriteria.getIseeTypes())){
                throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        InitiativeConstants.Exception.BadRequest.ISEE_TYPES_NOT_VALID,
                        HttpStatus.BAD_REQUEST);
            }
        }
    }
    private void checkIseeCriteriaForNF(Initiative initiative, List<AutomatedCriteria> automatedCriteriaList){
        if(InitiativeGeneral.BeneficiaryTypeEnum.NF.equals(initiative.getGeneral().getBeneficiaryType()) &&
                automatedCriteriaList.stream().noneMatch(a -> a.getCode().equals(ISEE))){
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    InitiativeConstants.Exception.BadRequest.INITIATIVE_BENEFICIARY_TYPE_NF_ENABLED_AUTOMATED_CRITERIA_ISEE_MISSING_NOT_VALID,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validateAllWizardSteps(InitiativeDTO initiativeDTO) {
        Set<ConstraintViolation<InitiativeAdditionalDTO>> violationsAdditional = validator.validate(initiativeDTO.getAdditionalInfo(), ValidationApiEnabledGroup.class);
        Set<ConstraintViolation<InitiativeGeneralDTO>> violationsGeneral = validator.validate(initiativeDTO.getGeneral(), ValidationApiEnabledGroup.class);
        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violationsBeneficiary =
                validator.validate(initiativeDTO.getBeneficiaryRule(), ValidationApiEnabledGroup.class);
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violationsReward = validator.validate(initiativeDTO.getRewardRule(), ValidationApiEnabledGroup.class);
        Set<ConstraintViolation<InitiativeTrxConditionsDTO>> violationsTrx = validator.validate(initiativeDTO.getTrxRule(), ValidationApiEnabledGroup.class);
        if(!violationsAdditional.isEmpty() ||
                !violationsGeneral.isEmpty() ||
                !violationsBeneficiary.isEmpty() ||
                !violationsReward.isEmpty() ||
                !violationsTrx.isEmpty()){
            Set<String> s = Stream.of(
                    violationsAdditional.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION, violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet()),
                            violationsGeneral.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION, violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet()),
                            violationsBeneficiary.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION,
                                    violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet()),
                            violationsReward.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION, violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet()),
                            violationsTrx.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION, violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet())
                    )
                    .flatMap(Set::stream)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet());
            String violations = s.toString();
            throw new InitiativeException(InitiativeConstants.Exception.BadRequest.CODE, String.format(InitiativeConstants.Exception.BadRequest.WIZARD_VALIDATION, violations), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void checkRewardRuleAbsolute(Initiative initiative) {
        InitiativeRewardRule rewardRule = initiative.getRewardRule();
        if (rewardRule instanceof RewardValue rewardValue &&
                RewardValue.RewardValueTypeEnum.ABSOLUTE.equals(rewardValue.getRewardValueType())) {
            Threshold threshold = initiative.getTrxRule().getThreshold();
            if (threshold==null || threshold.getFrom()==null || threshold.getFrom().compareTo(rewardValue.getRewardValue()) < 0){
                throw new InitiativeException(InitiativeConstants.Exception.BadRequest.CODE,
                        InitiativeConstants.Exception.BadRequest.REWARD_TYPE, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public void checkRefundRuleDiscountInitiative(String initiativeRewardType, InitiativeRefundRule refundRule){
        if (InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.DISCOUNT.name().equals(initiativeRewardType) &&
                (refundRule.getAccumulatedAmount() != null || refundRule.getTimeParameter() == null)){
                throw new InitiativeException(InitiativeConstants.Exception.BadRequest.CODE,
                        InitiativeConstants.Exception.BadRequest.REFUND_RULE_INVALID, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void checkBeneficiaryTypeAndFamilyUnit(Initiative initiative) {
        if (InitiativeGeneral.BeneficiaryTypeEnum.NF.equals(initiative.getGeneral().getBeneficiaryType()) &&
                (initiative.getGeneral().getFamilyUnitComposition() == null ||
                        !List.of(InitiativeConstants.FamilyUnitCompositionConstant.INPS,
                                InitiativeConstants.FamilyUnitCompositionConstant.ANPR).contains(initiative.getGeneral().getFamilyUnitComposition()))) {
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    InitiativeConstants.Exception.BadRequest.INITIATIVE_GENERAL_FAMILY_COMPOSITION_MESSAGE,
                    HttpStatus.BAD_REQUEST);
        }
        if (!InitiativeGeneral.BeneficiaryTypeEnum.NF.equals(initiative.getGeneral().getBeneficiaryType()) &&
                initiative.getGeneral().getFamilyUnitComposition() != null) {
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    InitiativeConstants.Exception.BadRequest.INITIATIVE_GENERAL_FAMILY_COMPOSITION_WRONG_BENEFICIARY_TYPE,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void checkStartDateAndEndDate(Initiative initiative) {
        if (initiative.getGeneral().getStartDate().isBefore(LocalDate.now()) || initiative.getGeneral().getEndDate().isBefore(LocalDate.now())) {
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    InitiativeConstants.Exception.BadRequest.INITIATIVE_GENERAL_START_DATE_END_DATE_WRONG,
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void checkFieldYearLengthAndValues(List<AutomatedCriteria> initiativeBeneficiaryRuleModel) {

        if (initiativeBeneficiaryRuleModel.stream().anyMatch(a -> "BIRTHDATE".equals(a.getCode()) && "year".equalsIgnoreCase(a.getField()) &&
                !(a.getValue().matches("\\d{4}") && Integer.parseInt(a.getValue()) >= Year.now().minusYears(150).getValue() && Integer.parseInt(a.getValue()) <= Year.now().getValue() ) )){
            throw new InitiativeException(
                    InitiativeConstants.Exception.BadRequest.CODE,
                    InitiativeConstants.Exception.BadRequest.INITIATIVE_BENEFICIARY_FIELD_YEAR_VALUE_WRONG,
                    HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public void checkReward(Initiative initiative) {
        InitiativeRewardRule rewardRule = initiative.getRewardRule();
        if (rewardRule instanceof RewardValue rewardValue &&
                RewardValue.RewardValueTypeEnum.PERCENTAGE.equals(rewardValue.getRewardValueType()) &&
                rewardValue.getRewardValue().intValue()>100){
            throw new InitiativeException(InitiativeConstants.Exception.BadRequest.CODE, InitiativeConstants.Exception.BadRequest.REWARD_TYPE, HttpStatus.BAD_REQUEST);
        }
    }
}
