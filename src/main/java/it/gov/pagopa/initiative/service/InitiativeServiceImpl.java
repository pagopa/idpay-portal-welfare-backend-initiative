package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.event.InitiativeProducer;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.mapper.InitiativeModelToDTOMapper;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class InitiativeServiceImpl implements InitiativeService {

    @Autowired
    private InitiativeRepository initiativeRepository;

    @Autowired
    private InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @Autowired
    private InitiativeProducer initiativeProducer;

    public List<Initiative> retrieveInitiativeSummary(String organizationId) {
        List<Initiative> initiatives = initiativeRepository.retrieveInitiativeSummary(organizationId);
        if(initiatives.isEmpty()){
            throw new InitiativeException(
                    InitiativeConstants.Exception.NotFound.CODE,
                    String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE, organizationId),
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
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public Initiative getInitiativeBeneficiaryView(String initiativeId) {
        return initiativeRepository.retrieveInitiativeBeneficiaryView(initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public void updateInitiativeGeneralInfo(String organizationId, String initiativeId, Initiative initiativeInfoModel) {
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        //Check Initiative Status
        isInitiativeAllowedThenThrows(initiative);
        initiative.setGeneral(initiativeInfoModel.getGeneral());
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateInitiativeAdditionalInfo(String organizationId, String initiativeId, Initiative initiativeAdditionalInfo){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        isInitiativeAllowedThenThrows(initiative);
        initiative.setAdditionalInfo(initiativeAdditionalInfo.getAdditionalInfo());
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateInitiativeBeneficiary(String organizationId, String initiativeId, InitiativeBeneficiaryRule initiativeBeneficiaryRuleModel){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        //Check Initiative Status
        isInitiativeAllowedThenThrows(initiative);
        initiative.setBeneficiaryRule(initiativeBeneficiaryRuleModel);
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateTrxAndRewardRules(String organizationId, String initiativeId, Initiative rewardAndTrxRules) {
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        //Check Initiative Status
        isInitiativeAllowedThenThrows(initiative);
        initiative.setRewardRule(rewardAndTrxRules.getRewardRule());
        initiative.setTrxRule(rewardAndTrxRules.getTrxRule());
        initiative.setUpdateDate(LocalDateTime.now());
        this.initiativeRepository.save(initiative);
    }

    @Override
    public void updateInitiativeRefundRules(String organizationId, String initiativeId, Initiative refundRule, boolean changeInitiativeStatus){
        Initiative initiative = this.initiativeRepository.findByOrganizationIdAndInitiativeId(organizationId, initiativeId)
                .orElseThrow(() -> new InitiativeException(
                        InitiativeConstants.Exception.NotFound.CODE,
                        String.format(InitiativeConstants.Exception.NotFound.INITIATIVE_BY_INITIATIVE_ID_MESSAGE, initiativeId),
                        HttpStatus.NOT_FOUND));
        //Check Initiative Status
        isInitiativeAllowedThenThrows(initiative);
        initiative.setRefundRule(refundRule.getRefundRule());
        initiative.setUpdateDate(LocalDateTime.now());
        if (changeInitiativeStatus) {
            initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        }
        this.initiativeRepository.save(initiative);
        //FIXME Test d'integrazione con RuleEngine. Invio Iniziativa al RuleEngine. Da spostare nella sezione di pubblicazione
        if (changeInitiativeStatus) {
            sendInitiativeInfoToRuleEngine(initiativeModelToDTOMapper.toInitiativeDTO(initiative));
        }
    }

    @Override
    public void sendInitiativeInfoToRuleEngine(InitiativeDTO initiativeDTO) {
        initiativeProducer.sendPublishInitiative(initiativeDTO);
    }

    private void isInitiativeAllowedThenThrows(Initiative initiative){
        if(Arrays.asList(InitiativeConstants.Status.Validation.ALLOWED_STATES_OF_EDITABLE_INITIATIVES_ARRAY).contains(initiative.getStatus())){
            return;
        }
        throw new InitiativeException(
                InitiativeConstants.Exception.BadRequest.CODE,
                String.format(InitiativeConstants.Exception.BadRequest.INITIATIVE_BY_INITIATIVE_ID_UNPROCESSABLE_FOR_STATUS_NOT_VALID, initiative.getInitiativeId()),
                HttpStatus.BAD_REQUEST);
    }

}
