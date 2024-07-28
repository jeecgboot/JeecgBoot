package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.utils.UserAgentGetter;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.LoginRecord;
import com.shop.mapper.LoginRecordMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 登录日志服务实现类
 * 2018-12-24 16:10
 */
@Service
public class LoginRecordService extends ServiceImpl<LoginRecordMapper, LoginRecord> {

    public PageResult<LoginRecord> listPage(PageParam<LoginRecord> page) {
        List<LoginRecord> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    public List<LoginRecord> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    public void saveAsync(String username, Integer type, String comments, HttpServletRequest request) {
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setUsername(username);
        loginRecord.setOperType(type);
        loginRecord.setComments(comments);
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        loginRecord.setOs(agentGetter.getOS());
        loginRecord.setDevice(agentGetter.getDevice());
        loginRecord.setBrowser(agentGetter.getBrowser());
        loginRecord.setIp(agentGetter.getIp());
        saveAsync(loginRecord);
    }

    public void saveAsync(String username, HttpServletRequest request) {
        saveAsync(username, LoginRecord.TYPE_LOGIN, null, request);
    }

    @Async
    public void saveAsync(LoginRecord loginRecord) {
        baseMapper.insert(loginRecord);
    }

}
