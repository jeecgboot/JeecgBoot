package org.jeecg.modules.jmreport.desreport.render.utils;

import org.jeecg.common.api.CommonAPI;
import org.avalonframework.annotation.Inject;

import java.util.regex.Pattern;

public class SafeFreemarkerMethod {
    
    private static final Pattern SAFE_EXPRESSION_PATTERN = Pattern.compile("^[0-9+\\-*/().\\s]+$");
    
    public String safeCompute(Object value) {
        if (value == null) return "";
        String str = value.toString();
        if (SAFE_EXPRESSION_PATTERN.matcher(str).matches()) {
            return str;
        } else {
            CommonAPI.logError("Forbidden expression detected", new SecurityException("Forbidden expression"));
            return "";
        }
    }
}