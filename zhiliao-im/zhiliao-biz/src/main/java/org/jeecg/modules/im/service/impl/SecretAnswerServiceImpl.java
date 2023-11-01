package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretAnswer;
import org.jeecg.modules.im.entity.SecretAnswer;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QSecretAnswer;
import org.jeecg.modules.im.mapper.SecretAnswerMapper;
import org.jeecg.modules.im.mapper.SecretAnswerMapper;
import org.jeecg.modules.im.service.SecretAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 密保答案 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-02
 */
@Service
public class SecretAnswerServiceImpl extends BaseServiceImpl<SecretAnswerMapper, SecretAnswer> implements SecretAnswerService {
    @Autowired
    private SecretAnswerMapper mapper;
    @Resource
    private UserService userService;

    @Override
    public IPage<SecretAnswer> pagination(MyPage<SecretAnswer> page, QSecretAnswer q) {
        return mapper.pagination(page,q);
    }

    @Override
    public Result<Object> check(String account, String questions) {
        try {
            Integer userId;
            if(StringUtils.isNotBlank(account)){
                User user = userService.findByAccount(account);
                if(user==null){
                    user = userService.findByUsername(account);
                }
                if(user==null){
                    user = userService.findByMobile(account);
                }
                if(user==null){
                    return fail("用户不存在");
                }
                if(user.getTsLocked()>0){
                    return fail("用户被禁用");
                }
                userId = user.getId();
            }else{
                userId = getCurrentUserId();
            }
            JSONArray arr = JSONArray.parseArray(questions);
            if (arr.size() < 2) {
                return fail("请回答至少两个问题");
            }
            LambdaQueryWrapper<SecretAnswer> wrapper = new LambdaQueryWrapper<>();
            for (Object o : arr) {
                wrapper.clear();
                wrapper.eq(SecretAnswer::getUserId, userId);
                wrapper.eq(SecretAnswer::getId, ((JSONObject) o).getInteger("id"));
                wrapper.eq(SecretAnswer::getAnswer, ((JSONObject) o).getString("answer"));
                if (getOne(wrapper) == null) {
                    return fail("一个答案错误");
                }
            }
            return success("验证密保问题成功");
        }catch (Exception e){
            log.error("验证密保问题异常,e={0}",e);
            return fail("验证密保问题出错");
        }
    }

}
