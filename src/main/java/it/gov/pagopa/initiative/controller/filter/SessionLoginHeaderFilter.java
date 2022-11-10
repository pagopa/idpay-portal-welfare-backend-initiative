package it.gov.pagopa.initiative.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SessionLoginHeaderFilter implements Filter {
    private final LoginThreadLocal loginThreadLocal;

    public SessionLoginHeaderFilter(LoginThreadLocal loginThreadLocal) {
        this.loginThreadLocal = loginThreadLocal;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, String> user = new HashMap<>();
        user.put("organizationUserId", ((HttpServletRequest) request).getHeader("organizationUserId"));
        user.put("organizationUserRole", ((HttpServletRequest) request).getHeader("organizationUserRole"));
        ThreadLocal<Map<String, String>> myThreadLocal = loginThreadLocal.getMyThreadLocal();
        myThreadLocal.set(user);
        chain.doFilter(request, response);
        myThreadLocal.remove();
    }

    // For Body Request
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
//        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
//
//        try {
//            filterChain.doFilter(requestWrapper, responseWrapper);
//        } finally {
//
//            String requestBody = new String(requestWrapper.getContentAsByteArray());
//            String responseBody = new String(responseWrapper.getContentAsByteArray());
//            // Do not forget this line after reading response content or actual response will be empty!
//            responseWrapper.copyBodyToResponse();
//
//            // Write request and response body, headers, timestamps etc. to log files
//
//        }
//    }


// IOUtils.toString(request.getReader());
// IOUtils.toString(request.getInputStream());
// StreamUtils.copyToByteArray(request.getInputStream()).toString();

}
