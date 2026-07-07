package com.xxl.job.admin.framework.web.xxlsso;

import com.xxl.job.admin.framework.constant.Consts;
import com.xxl.job.admin.framework.mapper.XxlJobUserMapper;
import com.xxl.job.admin.framework.model.XxlJobUser;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.sso.core.store.LoginStore;
import com.xxl.tool.core.MapTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Simple LoginStore
 *
 * 1、store by database；
 * 2、If you have higher performance requirements, it is recommended to use RedisLoginStore；
 *
 * @author xuxueli 2025-08-03
 */
@Component
public class SimpleLoginStore implements LoginStore {


    @Resource
    private XxlJobUserMapper xxlJobUserMapper;


    @Override
    public Response<String> set(LoginInfo loginInfo) {

        // parse token-signature
        String token_sign = loginInfo.getSignature();

        // write token by UserId
        int ret = xxlJobUserMapper.updateToken(Integer.parseInt(loginInfo.getUserId()), token_sign);
        return ret > 0 ? Response.ofSuccess() : Response.ofFail("token set fail");
    }

    @Override
    public Response<String> update(LoginInfo loginInfo) {
        return Response.ofFail("not support");
    }

    @Override
    public Response<String> remove(String userId) {
        // delete token-signature
        int ret = xxlJobUserMapper.updateToken(Integer.parseInt(userId), "");
        return ret > 0 ? Response.ofSuccess() : Response.ofFail("token remove fail");
    }

    /**
     * check through DB query
     */
    @Override
    public Response<LoginInfo> get(String userId) {

        // load login-user
        XxlJobUser user = xxlJobUserMapper.loadById(Integer.parseInt(userId));
        if (user == null) {
            return Response.ofFail("userId invalid.");
        }

        // parse role
        List<String> roleList = user.getRole()==1? List.of(Consts.ADMIN_ROLE):null;

        // parse jobGroup permission
        Map<String, String> extraInfo = MapTool.newMap(
                "jobGroups", user.getPermission()
        );

        // build LoginInfo
        LoginInfo loginInfo = new LoginInfo(userId, user.getToken());
        loginInfo.setUserName(user.getUsername());
        loginInfo.setRoleList(roleList);
        loginInfo.setExtraInfo(extraInfo);

        return Response.ofSuccess(loginInfo);
    }

}
