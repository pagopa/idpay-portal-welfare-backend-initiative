package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.dto.InitiativeBeneficiaryRuleDTO;
import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.trx.InitiativeTrxConditionsDTO;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.AutomatedCriteria;
import it.gov.pagopa.initiative.model.FilterOperatorEnumModel;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
        if (InitiativeConstants.Role.OPE_BASE.equals(role)){
            if (initiative.getStatus().equals(InitiativeConstants.Status.IN_REVISION) || initiative.getStatus().equals(InitiativeConstants.Status.TO_CHECK) || initiative.getStatus().equals(InitiativeConstants.Status.APPROVED)){
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
        if (InitiativeConstants.Role.OPE_BASE.equals(role)){
            throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role),
                        HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    @Validated(value = ValidationOnGroup.class)
    public void checkAutomatedCriteriaOrderDirectionWithRanking(Initiative initiative, List<AutomatedCriteria> automatedCriteriaList) {
        InitiativeGeneral general = initiative.getGeneral();
        if (Boolean.TRUE.equals(general.getRankingEnabled())){
            for(AutomatedCriteria automatedCriteria : automatedCriteriaList){
                String code = automatedCriteria.getCode();
                FilterOperatorEnumModel operator = automatedCriteria.getOperator();
                AutomatedCriteria.OrderDirection orderDirection = automatedCriteria.getOrderDirection();
                if(ISEE.equals(code)) {
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
        }
    }

    @Override
    public void validateAllWizardSteps(InitiativeDTO initiativeDTO) {
        Set<ConstraintViolation<InitiativeAdditionalDTO>> violationsAdditional = validator.validate(initiativeDTO.getAdditionalInfo(), ValidationOnGroup.class);
        Set<ConstraintViolation<InitiativeGeneralDTO>> violationsGeneral = validator.validate(initiativeDTO.getGeneral(), ValidationOnGroup.class);
        Set<ConstraintViolation<InitiativeBeneficiaryRuleDTO>> violationsBeneficiry = validator.validate(initiativeDTO.getBeneficiaryRule(), ValidationOnGroup.class);
        Set<ConstraintViolation<InitiativeRewardRuleDTO>> violationsReward = validator.validate(initiativeDTO.getRewardRule(), ValidationOnGroup.class);
        Set<ConstraintViolation<InitiativeTrxConditionsDTO>> violationsTrx = validator.validate(initiativeDTO.getTrxRule(), ValidationOnGroup.class);
        if(!violationsAdditional.isEmpty() ||
                !violationsGeneral.isEmpty() ||
                !violationsBeneficiry.isEmpty() ||
                !violationsReward.isEmpty() ||
                !violationsTrx.isEmpty()){
            Set<String> s = Stream.of(
                    violationsAdditional.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION, violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet()),
                            violationsGeneral.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION, violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet()),
                            violationsBeneficiry.stream().map(violation -> String.format(STRING_FORMAT_VIOLATION, violation.getPropertyPath(), violation.getMessage())).collect(Collectors.toSet()),
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

}
