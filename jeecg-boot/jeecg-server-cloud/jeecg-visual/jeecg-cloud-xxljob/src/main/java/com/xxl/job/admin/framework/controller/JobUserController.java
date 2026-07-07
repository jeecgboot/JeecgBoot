package com.xxl.job.admin.framework.controller;

import com.xxl.job.admin.framework.constant.Consts;
import com.xxl.job.admin.business.mapper.XxlJobGroupMapper;
import com.xxl.job.admin.framework.mapper.XxlJobUserMapper;
import com.xxl.job.admin.business.model.XxlJobGroup;
import com.xxl.job.admin.framework.model.XxlJobUser;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.CollectionTool;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.crypto.Sha256Tool;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * job user controller
 *
 * @author xuxueli 2019-05-04 16:39:50
 */
@Controller
@RequestMapping("/user")
public class JobUserController {

    @Resource
    private XxlJobUserMapper xxlJobUserMapper;
    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;

    @RequestMapping
    @XxlSso(role = Consts.ADMIN_ROLE)
    public String index(Model model) {

        // 执行器列表
        List<XxlJobGroup> groupList = xxlJobGroupMapper.findAll();
        model.addAttribute("groupList", groupList);

        return "business/user.list";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @XxlSso(role = Consts.ADMIN_ROLE)
    public Response<PageModel<XxlJobUser>> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
                                                    @RequestParam(required = false, defaultValue = "10") int pagesize,
                                                    @RequestParam String username,
                                                    @RequestParam int role) {

        // page list
        List<XxlJobUser> list = xxlJobUserMapper.pageList(offset, pagesize, username, role);
        int list_count = xxlJobUserMapper.pageListCount(offset, pagesize, username, role);

        // filter
        if (list!=null && !list.isEmpty()) {
            for (XxlJobUser item: list) {
                item.setPassword(null);
            }
        }

        // package result
        PageModel<XxlJobUser> pageModel = new PageModel<>();
        pageModel.setData(list);
        pageModel.setTotal(list_count);

        return Response.ofSuccess(pageModel);
    }

    @RequestMapping("/insert")
    @ResponseBody
    @XxlSso(role = Consts.ADMIN_ROLE)
    public Response<String> insert(XxlJobUser xxlJobUser) {

        // valid username
        if (StringTool.isBlank(xxlJobUser.getUsername())) {
            return Response.ofFail(I18nUtil.getString("system_please_input")+I18nUtil.getString("user_username") );
        }
        xxlJobUser.setUsername(xxlJobUser.getUsername().trim());
        if (!(xxlJobUser.getUsername().length()>=4 && xxlJobUser.getUsername().length()<=20)) {
            return Response.ofFail(I18nUtil.getString("system_length_limit")+"[4-20]" );
        }
        // valid password
        if (StringTool.isBlank(xxlJobUser.getPassword())) {
            return Response.ofFail(I18nUtil.getString("system_please_input")+I18nUtil.getString("user_password") );
        }
        xxlJobUser.setPassword(xxlJobUser.getPassword().trim());
        if (!(xxlJobUser.getPassword().length()>=4 && xxlJobUser.getPassword().length()<=20)) {
            return Response.ofFail(I18nUtil.getString("system_length_limit")+"[4-20]" );
        }
        // md5 password
        String passwordHash = Sha256Tool.sha256(xxlJobUser.getPassword());
        xxlJobUser.setPassword(passwordHash);

        // check repeat
        XxlJobUser existUser = xxlJobUserMapper.loadByUserName(xxlJobUser.getUsername());
        if (existUser != null) {
            return Response.ofFail( I18nUtil.getString("user_username_repeat") );
        }

        // write
        xxlJobUserMapper.save(xxlJobUser);
        return Response.ofSuccess();
    }

    @RequestMapping("/update")
    @ResponseBody
    @XxlSso(role = Consts.ADMIN_ROLE)
    public Response<String> update(HttpServletRequest request, XxlJobUser xxlJobUser) {

        // avoid opt login seft
        Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
        if (loginInfoResponse.getData().getUserName().equals(xxlJobUser.getUsername())) {
            return Response.ofFail(I18nUtil.getString("user_update_loginuser_limit"));
        }

        // valid password
        if (StringTool.isNotBlank(xxlJobUser.getPassword())) {
            xxlJobUser.setPassword(xxlJobUser.getPassword().trim());
            if (!(xxlJobUser.getPassword().length()>=4 && xxlJobUser.getPassword().length()<=20)) {
                return Response.ofFail(I18nUtil.getString("system_length_limit")+"[4-20]" );
            }
            // md5 password
            String passwordHash = Sha256Tool.sha256(xxlJobUser.getPassword());
            xxlJobUser.setPassword(passwordHash);
        } else {
            xxlJobUser.setPassword(null);
        }

        // write
        xxlJobUserMapper.update(xxlJobUser);
        return Response.ofSuccess();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @XxlSso(role = Consts.ADMIN_ROLE)
    public Response<String> delete(HttpServletRequest request, @RequestParam("ids[]") List<Integer> ids) {

        // valid
        if (CollectionTool.isEmpty(ids) || ids.size()!=1) {
            return Response.ofFail(I18nUtil.getString("system_please_choose") + I18nUtil.getString("system_one") + I18nUtil.getString("system_data"));
        }

        // avoid opt login seft
        Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
        if (ids.contains(Integer.parseInt(loginInfoResponse.getData().getUserId()))) {
            return Response.ofFail(I18nUtil.getString("user_update_loginuser_limit"));
        }

        xxlJobUserMapper.delete(ids.get(0));
        return Response.ofSuccess();
    }

    /*@RequestMapping("/updatePwd")
    @ResponseBody
    public Response<String> updatePwd(HttpServletRequest request,
                                     @RequestParam("password") String password,
                                     @RequestParam("oldPassword") String oldPassword){

        // valid
        if (oldPassword==null || oldPassword.trim().isEmpty()){
            return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("change_pwd_field_oldpwd"));
        }
        if (password==null || password.trim().isEmpty()){
            return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("change_pwd_field_oldpwd"));
        }
        password = password.trim();
        if (!(password.length()>=4 && password.length()<=20)) {
            return Response.ofFail(I18nUtil.getString("system_length_limit")+"[4-20]" );
        }

        // md5 password
        String oldPasswordHash = Sha256Tool.sha256(oldPassword);
        String passwordHash = Sha256Tool.sha256(password);

        // valid old pwd
        Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
        XxlJobUser existUser = xxlJobUserMapper.loadByUserName(loginInfoResponse.getData().getUserName());
        if (!oldPasswordHash.equals(existUser.getPassword())) {
            return Response.ofFail(I18nUtil.getString("change_pwd_field_oldpwd") + I18nUtil.getString("system_invalid"));
        }

        // write new
        existUser.setPassword(passwordHash);
        xxlJobUserMapper.update(existUser);

        return Response.ofSuccess();
    }*/

}
