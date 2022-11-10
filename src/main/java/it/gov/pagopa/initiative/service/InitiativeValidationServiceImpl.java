package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.exception.InitiativeException;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class InitiativeValidationServiceImpl implements InitiativeValidationService {

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
        if (InitiativeConstants.Role.OPE_BASE.equals(role)){
            throw new InitiativeException(
                        InitiativeConstants.Exception.BadRequest.CODE,
                        String.format(InitiativeConstants.Exception.BadRequest.PERMISSION_NOT_VALID, role),
                        HttpStatus.BAD_REQUEST
            );
        }
    }

}
