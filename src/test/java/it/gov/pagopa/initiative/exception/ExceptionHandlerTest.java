package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

class ExceptionHandlerTest {

    private RestResponseExceptionHandler controllerAdvice = new RestResponseExceptionHandler();

    @Test
    void raisedInitiativeException(){
        ErrorDTO errorDTO = new ErrorDTO(InitiativeConstants.Exception.NotFound.CODE, MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE, anyString()));
        InitiativeException e = new InitiativeException(
                InitiativeConstants.Exception.NotFound.CODE,
                MessageFormat.format(InitiativeConstants.Exception.NotFound.INITIATIVE_LIST_BY_ORGANIZATION_MESSAGE, anyString()),
                HttpStatus.NOT_FOUND);
        ResponseEntity<ErrorDTO> errorDTOResponseEntity = controllerAdvice.handleInitiativeException(e);
        assertThat(errorDTOResponseEntity).isNotNull();
        assertThat(errorDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorDTOResponseEntity.getBody()).isEqualTo(errorDTO);
    }

}
