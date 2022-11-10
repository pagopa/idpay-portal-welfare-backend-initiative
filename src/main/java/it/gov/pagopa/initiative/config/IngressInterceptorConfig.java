package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.controller.filter.LoginThreadLocal;
import it.gov.pagopa.initiative.controller.filter.SessionLoginHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IngressInterceptorConfig {

    @Bean
    public FilterRegistrationBean<SessionLoginHeaderFilter> sessionLoginHeaderFilterRegistrationBean(LoginThreadLocal loginThreadLocal) {
        FilterRegistrationBean<SessionLoginHeaderFilter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionLoginHeaderFilter(loginThreadLocal));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
