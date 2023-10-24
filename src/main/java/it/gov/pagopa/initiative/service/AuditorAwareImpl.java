package it.gov.pagopa.initiative.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String UNDEFINED = "UNDEFINED";

    @Autowired
    private HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        String method = request.getMethod();

        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
            String organizationUserId = request.getHeader("organization-user-id");
            return Optional.ofNullable(organizationUserId);
        }
        return Optional.of(UNDEFINED);
    }
}