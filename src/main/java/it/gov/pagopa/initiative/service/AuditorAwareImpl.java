package it.gov.pagopa.initiative.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String UNDEFINED = "UNDEFINED";

    @Autowired
    private HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        String method = request.getMethod();

        switch (method){
            case "POST", "PUT", "DELETE":
                String organizationUserId = request.getHeader("organization-user-id");
                return Optional.ofNullable(organizationUserId);
            default:
                return Optional.of(UNDEFINED);
        }
    }

}