package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.AutomatedCriteria;
import it.gov.pagopa.initiative.model.FilterOperatorEnumModel;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import it.gov.pagopa.initiative.utils.validator.ValidationOnGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
@Validated
public class InitiativeValidationServiceImpl implements InitiativeValidationService {

    private static final String ISEE = "ISEE";
    private final InitiativeRepository initiativeRepository;

    public InitiativeValidationServiceImpl(
            InitiativeRepository initiativeRepository
    ) {
        this.initiativeRepository = initiativeRepository;
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

}
