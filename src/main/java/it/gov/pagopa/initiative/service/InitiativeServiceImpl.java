package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InitiativeServiceImpl implements InitiativeService {

    @Autowired
    private InitiativeRepository initiativeRepository;

    public List<Initiative> retrieveInitiativeSummary(String organizationId) {
        List<Initiative> initiatives = initiativeRepository.retrieveInitiativeSummary(organizationId);
        if(initiatives.isEmpty()){
            throw new InitiativeException(
                    InitiativeConstants.Exception.NotFound.CODE,
                    MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE, organizationId),
                    HttpStatus.NOT_FOUND);
        }
        return initiatives;
    }

    @Override
    public Initiative insertInitiative(Initiative initiative) {
        if (StringUtils.isBlank(initiative.getStatus())) {
            initiative.setStatus(InitiativeConstants.Status.DRAFT);
        }
        return initiativeRepository.insert(initiative);
    }

    @Override
    public Initiative getInitiative(String organizationId, String initiativeId) {
        return initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE, organizationId),
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public Initiative getInitiativeBeneficiaryView(String initiativeId) {
        return initiativeRepository.retrieveInitiativeBeneficiaryView(initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel) {
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_ORGANIZATION_ID_MESSAGE, organizationId, initiativeId),
                        HttpStatus.NOT_FOUND));
        initiative.setGeneral(initiativeInfoModel.getGeneral());
        initiative.setAdditionalInfo(initiativeInfoModel.getAdditionalInfo());
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule initiativeBeneficiaryRuleModel){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_ORGANIZATION_ID_MESSAGE, organizationId, initiativeId),
                        HttpStatus.NOT_FOUND));
        initiative.setBeneficiaryRule(initiativeBeneficiaryRuleModel);
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules) {
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_ORGANIZATION_ID_MESSAGE, organizationId, initiativeId),
                        HttpStatus.NOT_FOUND));
        initiative.setRewardRule(rewardAndTrxRules.getRewardRule());
        initiative.setTrxRule(rewardAndTrxRules.getTrxRule());
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
    }


}
