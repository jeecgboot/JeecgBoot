package com.xxl.job.admin.framework.util;

import com.xxl.job.admin.framework.constant.Consts;
import com.xxl.job.admin.business.model.XxlJobGroup;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * jobGroup permission util
 *
 * @author xuxueli 2025-08-24
 */
public class JobGroupPermissionUtil {

    /**
     * check if has jobgroup permission
     */
    public static boolean hasJobGroupPermission(LoginInfo loginInfo, int jobGroup){
        if (XxlSsoHelper.hasRole(loginInfo, Consts.ADMIN_ROLE).isSuccess()) {
            return true;
        } else {
            List<String> jobGroups = (loginInfo.getExtraInfo()!=null && loginInfo.getExtraInfo().containsKey("jobGroups"))
                    ? StringTool.split(loginInfo.getExtraInfo().get("jobGroups"), ",") :new ArrayList<>();
            return jobGroups.contains(String.valueOf(jobGroup));
        }
    }

    /**
     * valid jobGroup permission
     */
    public static LoginInfo validJobGroupPermission(HttpServletRequest request, int jobGroup) {
        Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
        if (!(loginInfoResponse.isSuccess() && hasJobGroupPermission(loginInfoResponse.getData(), jobGroup))) {
            throw new RuntimeException(I18nUtil.getString("system_permission_limit") + "[username="+ loginInfoResponse.getData().getUserName() +"]");
        }
        return loginInfoResponse.getData();
    }

    /**
     * filter jobGroupList by permission
     */
    public static List<XxlJobGroup> filterJobGroupByPermission(HttpServletRequest request, List<XxlJobGroup> jobGroupListTotal){
        Response<LoginInfo>  loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);

        if (XxlSsoHelper.hasRole(loginInfoResponse.getData(), Consts.ADMIN_ROLE).isSuccess()) {
            return jobGroupListTotal;
        } else {
            List<String> jobGroups = (loginInfoResponse.getData().getExtraInfo()!=null
                    && loginInfoResponse.getData().getExtraInfo().get("jobGroups")!=null
            )
                    ? StringTool.split(loginInfoResponse.getData().getExtraInfo().get("jobGroups"), ",")
                    :new ArrayList<>();

            return jobGroupListTotal
                    .stream()
                    .filter(jobGroup -> jobGroups.contains(String.valueOf(jobGroup.getId())))
                    .toList();
        }
    }

}
