package com.xxl.job.admin.framework.web.error;

import org.springframework.boot.web.error.ErrorPage;
import org.springframework.boot.web.error.ErrorPageRegistrar;
import org.springframework.boot.web.error.ErrorPageRegistry;
import org.springframework.stereotype.Component;

/**
 * error page
 */
@Component
public class WebErrorPageRegistrar implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage errorPage = new ErrorPage("/errorpage");
        registry.addErrorPages(errorPage);
    }
}
