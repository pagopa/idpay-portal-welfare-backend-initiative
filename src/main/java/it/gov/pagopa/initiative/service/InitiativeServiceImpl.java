package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitiativeServiceImpl implements InitiativeService {

    @Autowired
    private InitiativeRepository initiativeRepository;

    public List<Initiative> retrieveInitiativeSummary(String organizationId) {
        return initiativeRepository.retrieveInitiativeSummary(organizationId);
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
                        String.format(InitiativeConstants.Exception.NOT_FOUND, initiativeId),
                        InitiativeConstants.Exception.NOT_FOUND_MESSAGE,
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public Initiative getInitiativeBeneficiaryView(String initiativeId) {
        return initiativeRepository.retrieveInitiativeBeneficiaryView(initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        String.format(InitiativeConstants.Exception.NOT_FOUND, initiativeId),
                        InitiativeConstants.Exception.NOT_FOUND_MESSAGE,
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel) {
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        String.format(InitiativeConstants.Exception.NOT_FOUND, initiativeId),
                        InitiativeConstants.Exception.NOT_FOUND_MESSAGE,
                        HttpStatus.NOT_FOUND));
        initiative.setGeneral(initiativeInfoModel.getGeneral());
        initiative.setAdditionalInfo(initiativeInfoModel.getAdditionalInfo());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateInitiativeBeneficiary(String organizationId, String initiativeId, Initiative initiativeBeneficiaryRuleModel){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        String.format(InitiativeConstants.Exception.NOT_FOUND, initiativeId),
                        InitiativeConstants.Exception.NOT_FOUND_MESSAGE,
                        HttpStatus.NOT_FOUND));
        initiative.setBeneficiaryRule(initiativeBeneficiaryRuleModel.getBeneficiaryRule());
        this.initiativeRepository.save(initiative);
    }


}
