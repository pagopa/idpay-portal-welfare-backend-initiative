/*
package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.controller.filter.HeaderFilter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {IngressInterceptorConfig.class})
@ExtendWith(SpringExtension.class)
class IngressInterceptorConfigTest {
    @Autowired
    private IngressInterceptorConfig ingressInterceptorConfig;

    @Test
    void testSessionLoginHeaderFilterRegistrationBean() {
        FilterRegistrationBean<HeaderFilter> registrationBean1 = new FilterRegistrationBean<>();
        registrationBean1.setFilter(new HeaderFilter());
        registrationBean1.addUrlPatterns("/");
        assertEquals(ingressInterceptorConfig.sessionLoginHeaderFilterRegistrationBean(), registrationBean1);
    }
}

*/
