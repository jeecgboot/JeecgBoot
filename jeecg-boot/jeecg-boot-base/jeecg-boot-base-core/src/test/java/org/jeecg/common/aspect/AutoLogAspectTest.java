package org.jeecg.common.aspect;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.jeecg.modules.base.service.BaseCommonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AutoLogAspectTest {

    @Mock
    private BaseCommonService commonService;

    private AutoLogAspect aspect;

    @Before
    public void setUp() {
        aspect = new AutoLogAspect(commonService);
    }

    @Test
    public void testIsLoggingArgument() {
        assertFalse(aspect.isLoggingArgument(mock(BindingResult.class)));
        assertFalse(aspect.isLoggingArgument(mock(ServletRequest.class)));
        assertFalse(aspect.isLoggingArgument(mock(ServletResponse.class)));
        assertFalse(aspect.isLoggingArgument(mock(MultipartFile.class)));
        assertFalse(aspect.isLoggingArgument(mock(ShiroHttpServletRequest.class)));
        assertTrue(aspect.isLoggingArgument("test"));
        assertTrue(aspect.isLoggingArgument(null));
    }

}