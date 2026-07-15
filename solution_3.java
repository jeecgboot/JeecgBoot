package org.jeecg.modules.jmreport.desreport.render.utils;

import org.avalonframework.annotation.Inject;

import java.util.Map;

public class FreemarkerMethod {
    
    @Inject
    private SafeFreemarkerMethod safeFreemarkerMethod;
    
    public Object compute(Map<String, Object> var1, String field, Object var8) {
        return safeFreemarkerMethod.safeCompute(var8);
    }
}