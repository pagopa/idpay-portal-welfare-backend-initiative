package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.controller.filter.HeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IngressInterceptorConfig {

    @Bean
    public FilterRegistrationBean<HeaderFilter> sessionLoginHeaderFilterRegistrationBean() {
        FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HeaderFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
