package org.jeecg.modules.system.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.modules.redis.client.JeecgRedisClient;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.controller.SysUserController;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * 系统用户单元测试
 */
@WebMvcTest(SysUserController.class)
public class SysUserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISysUserService sysUserService;

    @MockBean
    private ISysDepartService sysDepartService;

    @MockBean
    private ISysUserRoleService sysUserRoleService;

    @MockBean
    private ISysUserDepartService sysUserDepartService;

    @MockBean
    private ISysDepartRoleUserService departRoleUserService;

    @MockBean
    private ISysDepartRoleService departRoleService;

    @MockBean
    private RedisUtil redisUtil;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    @MockBean
    private BaseCommonService baseCommonService;

    @MockBean
    private ISysUserAgentService sysUserAgentService;

    @MockBean
    private ISysPositionService sysPositionService;

    @MockBean
    private ISysUserTenantService userTenantService;

    @MockBean
    private JeecgRedisClient jeecgRedisClient;

    @MockBean
    private JeecgBaseConfig jeecgBaseConfig;
    /**
     * 测试地址：实际使用时替换成你自己的地址
     */
    private final String BASE_URL = "/sys/user/";

    /**
     * 测试用例：查询记录
     */
    @Test
    public void testQuery() throws Exception{
        // 请求地址
        String url = BASE_URL + "list";

        Page<SysUser> sysUserPage = new Page<>();
        SysUser sysUser = new SysUser();
        sysUser.setUsername("admin");
        List<SysUser> users = new ArrayList<>();
        users.add(sysUser);
        sysUserPage.setRecords(users);
        sysUserPage.setCurrent(1);
        sysUserPage.setSize(10);
        sysUserPage.setTotal(1);

        given(this.sysUserService.queryPageList(any(), any(), any(), any())).willReturn(Result.OK(sysUserPage));

        String result = mockMvc.perform(get(url)).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(result);
        Assertions.assertEquals("admin", jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(0).getString("username"));
    }

    /**
     * 测试用例：新增
     */
    @Test
    public void testAdd() throws Exception {
        // 请求地址
        String url = BASE_URL + "add" ;

        JSONObject params = new JSONObject();
        params.put("username", "wangwuTest");
        params.put("password", "123456");
        params.put("confirmpassword","123456");
        params.put("realname", "单元测试");
        params.put("activitiSync", "1");
        params.put("userIdentity","1");
        params.put("workNo","0025");

        String result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(params.toJSONString()))
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(result);
        Assertions.assertTrue(jsonObject.getBoolean("success"));
    }


    /**
     * 测试用例：修改
     */
    @Test
    public void testEdit() throws Exception {
        // 数据Id
        String dataId = "1331795062924374018";
        // 请求地址
        String url = BASE_URL + "edit";

        JSONObject params = new JSONObject();
        params.put("username", "wangwuTest");
        params.put("realname", "单元测试1111");
        params.put("activitiSync", "1");
        params.put("userIdentity","1");
        params.put("workNo","0025");
        params.put("id",dataId);

        SysUser sysUser = new SysUser();
        sysUser.setUsername("admin");

        given(this.sysUserService.getById(any())).willReturn(sysUser);

        String result = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(params.toJSONString()))
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(result);
        Assertions.assertTrue(jsonObject.getBoolean("success"));
    }


    /**
     * 测试用例：删除
     */
    @Test
    public void testDelete() throws Exception {
        // 数据Id
        String dataId = "1331795062924374018";
        // 请求地址
        String url = BASE_URL + "delete" + "?id=" + dataId;
        String result = mockMvc.perform(delete(url)).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(result);
        Assertions.assertTrue(jsonObject.getBoolean("success"));
    }
}
