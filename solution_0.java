package org.jeecg.modules.jmreport.desreport.render.utils;

import org.jeecg.common.api.CommonAPI;
import org.avalonframework.annotation.Inject;

import java.util.Map;

public class FreeMarkerUtils {
    
    @Inject
    private SafeFreemarkerMethod safeFreemarkerMethod;
    
    public Object render(Map<String, Object> var1, String field, Object var8) {
        try {
            return safeFreemarkerMethod.safeCompute(var8);
        } catch (Exception e) {
            CommonAPI.logError("Error rendering FreeMarker template", e);
            return null;
        }
    }
}