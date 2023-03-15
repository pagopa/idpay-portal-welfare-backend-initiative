package it.gov.pagopa.initiative.config;

import it.gov.pagopa.initiative.controller.filter.HeaderFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.mockito.Mockito.doReturn;

@ContextConfiguration(classes = {IngressInterceptorConfig.class})
@ExtendWith(SpringExtension.class)
class IngressInterceptorConfigTest {
    @Autowired
    private IngressInterceptorConfig ingressInterceptorConfig;

    @Disabled
    void testSessionLoginHeaderFilterRegistrationBean() {
        FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<>();
        HeaderFilter headerFilter = new HeaderFilter();
        String url = "/*";
        registrationBean.setFilter(headerFilter);
        registrationBean.addUrlPatterns(url);

        doReturn(registrationBean.getFilter()).when(ingressInterceptorConfig)
        .sessionLoginHeaderFilterRegistrationBean().getFilter();

        ingressInterceptorConfig.sessionLoginHeaderFilterRegistrationBean();

        Assertions.assertEquals(registrationBean.getFilter(),
                ingressInterceptorConfig.sessionLoginHeaderFilterRegistrationBean().getFilter());
        Assertions.assertEquals(registrationBean.getUrlPatterns(),
                ingressInterceptorConfig.sessionLoginHeaderFilterRegistrationBean().getUrlPatterns());
        assertTrue(ingressInterceptorConfig.sessionLoginHeaderFilterRegistrationBean().equals(registrationBean));
    }
}

