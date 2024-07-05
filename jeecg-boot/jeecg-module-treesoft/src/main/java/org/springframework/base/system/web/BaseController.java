package org.springframework.base.system.web;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;

public class BaseController {
    public static String versionType = "01";
    public static String versionNumber = "试用版";

    public BaseController() {
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            public void setAsText(String text) {
                this.setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            public String getAsText() {
                Object value = this.getValue();
                return value != null ? value.toString() : "";
            }
        });
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            public void setAsText(String text) {
                this.setValue(DateUtils.parseDate(text));
            }
        });
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            public void setAsText(String text) {
                Date date = DateUtils.parseDate(text);
                this.setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }

    public <T> Page<T> getPage(HttpServletRequest request) {
        int pageNo = 1;
        int pageSize = 30;
        String orderBy = "";
        String order = "asc";
        if (StringUtil.isNotEmpty(request.getParameter("page"))) {
            pageNo = Integer.valueOf(request.getParameter("page"));
        }

        if (StringUtil.isNotEmpty(request.getParameter("rows"))) {
            pageSize = Integer.valueOf(request.getParameter("rows"));
        }

        if (StringUtil.isNotEmpty(request.getParameter("sort"))) {
            orderBy = request.getParameter("sort").toString();
        }

        if (StringUtil.isNotEmpty(request.getParameter("order"))) {
            order = request.getParameter("order").toString();
        }

        return new Page(pageNo, pageSize, orderBy, order);
    }

}
