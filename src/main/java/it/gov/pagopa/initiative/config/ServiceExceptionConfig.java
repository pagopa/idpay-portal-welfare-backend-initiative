package it.gov.pagopa.initiative.config;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.initiative.exception.custom.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class ServiceExceptionConfig {
    @Bean
    public Map<Class<? extends ServiceException>, HttpStatus> serviceExceptionMapper() {
        Map<Class<? extends ServiceException>, HttpStatus> exceptionMap = new HashMap<>();

        // BadRequest
        exceptionMap.put(InitiativeRequiredLanguageException.class,HttpStatus.BAD_REQUEST);
        exceptionMap.put(DeleteInitiativeException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(AdminPermissionException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(InitiativeStatusNotValidException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(AutomatedCriteriaNotValidException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(ValidationWizardException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(InvalidRewardRuleException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(InvalidRefundRuleException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(InitiativeFamilyUnitCompositionException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(InitiativeDateInvalidException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(InitiativeYearValueException.class, HttpStatus.BAD_REQUEST);

        //Forbidden
        exceptionMap.put(OrgPermissionException.class, HttpStatus.FORBIDDEN);

        // NotFound
        exceptionMap.put(InitiativeNotFoundException.class, HttpStatus.NOT_FOUND);

        // InternalServerError
        exceptionMap.put(InitiativeLogoException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(EncryptInvocationException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(OnboardingInvocationException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(DecryptInvocationException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(InitiativeNoRankingException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(RankingInvocationException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(IOBackEndInvocationException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exceptionMap.put(CommandProducerException.class, HttpStatus.INTERNAL_SERVER_ERROR);

        return exceptionMap;
    }
}
