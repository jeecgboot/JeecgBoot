package org.jeecg.modules.system.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.vo.SysUserOnlineVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 在线用户
 * @Author: chenli
 * @Date: 2020-06-07
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sys/online")
@Slf4j
public class SysUserOnlineController {

    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private BaseCommonService baseCommonService;

    /**
     * 获取在线用户列表（使用Sa-Token）
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<Page<SysUserOnlineVO>> list(@RequestParam(name="username", required=false) String username,
                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        List<SysUserOnlineVO> onlineList = new ArrayList<>();
        
        try {
            // 使用Sa-Token获取所有在线用户的session ID列表
            List<String> sessionIdList = StpUtil.searchSessionId("", 0, -1, false);
            
            for (String sessionId : sessionIdList) {
                try {
                    // 获取session
                    SaSession session = StpUtil.getSessionBySessionId(sessionId);
                    if (session == null) {
                        continue;
                    }
                    
                    // 从session中获取用户信息
                    LoginUser loginUser = (LoginUser) session.get("loginUser");
                    if (loginUser != null && !"_reserve_user_external".equals(loginUser.getUsername())) {
                        // 用户名筛选
                        if (oConvertUtils.isEmpty(username) || loginUser.getUsername().contains(username)) {
                            SysUserOnlineVO online = new SysUserOnlineVO();
                            BeanUtils.copyProperties(loginUser, online);
                            
                            // 获取该用户的token（loginId现在是username）
                            try {
                                String token = StpUtil.getTokenValueByLoginId(loginUser.getUsername());
                                online.setToken(token);
                            } catch (Exception e) {
                                log.debug("获取用户token失败: {}", e.getMessage());
                            }
                            
                            onlineList.add(online);
                        }
                    }
                } catch (Exception e) {
                    // 旧的Session数据可能导致反序列化失败，记录debug级别日志即可
                    log.debug("获取session失败（可能是旧数据）: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("获取在线用户列表失败: {}", e.getMessage(), e);
        }
        
        Collections.reverse(onlineList);

        Page<SysUserOnlineVO> page = new Page<SysUserOnlineVO>(pageNo, pageSize);
        int count = onlineList.size();
        List<SysUserOnlineVO> pages = new ArrayList<>();
        // 计算当前页第一条数据的下标
        int currId = pageNo > 1 ? (pageNo - 1) * pageSize : 0;
        for (int i = 0; i < pageSize && i < count - currId; i++) {
            pages.add(onlineList.get(currId + i));
        }
        page.setSize(pageSize);
        page.setCurrent(pageNo);
        page.setTotal(count);
        // 计算分页总页数
        page.setPages(count % 10 == 0 ? count / 10 : count / 10 + 1);
        page.setRecords(pages);

        Result<Page<SysUserOnlineVO>> result = new Result<Page<SysUserOnlineVO>>();
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /**
     * 强退用户（使用Sa-Token）
     */
    @RequestMapping(value = "/forceLogout",method = RequestMethod.POST)
    public Result<Object> forceLogout(@RequestBody SysUserOnlineVO online) {
        try {
            // 验证参数
            if (oConvertUtils.isEmpty(online.getToken())) {
                return Result.error("Token不能为空！");
            }
            
            // 使用Sa-Token通过token强制退出登录
            StpUtil.logoutByTokenValue(online.getToken());
            
            // 清空用户的缓存信息（如果有用户名）
            if (oConvertUtils.isNotEmpty(online.getUsername())) {
                redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, online.getUsername()));
            }
            
            // 记录日志
            String username = oConvertUtils.isNotEmpty(online.getUsername()) ? online.getUsername() : "未知用户";
            baseCommonService.addLog("强制: " + username + " 退出成功！", CommonConstant.LOG_TYPE_1, null);
            log.info("强制 {} 退出成功！", username);
            
            return Result.ok("退出登录成功！");
        } catch (Exception e) {
            log.error("强制退出失败: {}", e.getMessage(), e);
            return Result.error("退出登录失败：" + e.getMessage());
        }
    }
}
