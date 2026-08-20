package org.jeecg.modules.jmreport.desreport.render.utils;

import org.avalonframework.annotation.Inject;

import java.util.Map;

public class AviatorEvaluator {
    
    @Inject
    private SafeFreemarkerMethod safeFreemarkerMethod;
    
    public Object execute(String field, Object var8) {
        try {
            return safeFreemarkerMethod.safeCompute(var8);
        } catch (Exception e) {
            throw new RuntimeException("Error executing Aviator expression", e);
        }
    }
}