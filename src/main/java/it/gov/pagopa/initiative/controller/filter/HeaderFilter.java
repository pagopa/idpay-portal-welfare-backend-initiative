package it.gov.pagopa.initiative.controller.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class HeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = httpRequest.getHeader(key);
                log.debug("Header [{}]: {}", key, value);
            }
        }
        RequestContextHolder.currentRequestAttributes().setAttribute("organizationUserId", httpRequest.getHeader("organization-user-id"), RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.currentRequestAttributes().setAttribute("organizationUserRole", httpRequest.getHeader("organization-user-role"), RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.currentRequestAttributes().setAttribute("organizationUserName", httpRequest.getHeader("organization-user-name"), RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.currentRequestAttributes().setAttribute("organizationUserFamilyName", httpRequest.getHeader("organization-user-family-name"), RequestAttributes.SCOPE_REQUEST);
        chain.doFilter(request, response);
    }

}
