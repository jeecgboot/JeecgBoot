package org.jeecg.modules.jmreport.desreport.render.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class FreeMarkerUtilsTest {
    
    @Test
    public void testRender() {
        FreeMarkerUtils freeMarkerUtils = new FreeMarkerUtils();
        Map<String, Object> var1 = new java.util.HashMap<>();
        String field = "test";
        Object var8 = "1 + 2 * 3";
        Object result = freeMarkerUtils.render(var1, field, var8);
        assertEquals("7", result);
    }
}