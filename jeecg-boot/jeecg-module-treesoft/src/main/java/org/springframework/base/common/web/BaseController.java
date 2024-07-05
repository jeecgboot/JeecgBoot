package org.springframework.base.common.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.base.common.utils.DateUtil;
import org.springframework.base.system.persistence.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text)  {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }
            
            @Override
            public String getAsText()  {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text)  {
                setValue(DateUtil.parseDate(text));
            }
        });
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text)  {
                Date date = DateUtil.parseDate(text);
                setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }
    
    public <T> Page<T> getPage(HttpServletRequest request) {
        int pageNo = NumberUtils.toInt(request.getParameter("page"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("rows"), 20);
        String orderBy = StringUtils.trimToEmpty(request.getParameter("sort"));
        String order = StringUtils.defaultIfBlank(request.getParameter("order"), "asc");
        return new Page<>(pageNo, pageSize, orderBy, order);
    }
    
    public <T> Map<String, Object> getEasyUIData(Page<T> page) {
        Map<String, Object> map = new HashMap<>();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        return map;
    }
}
