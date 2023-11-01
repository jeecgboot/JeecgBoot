package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.entity.SecretAnswer;
import org.jeecg.modules.im.entity.SecretQuestion;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.SecretAnswerService;
import org.jeecg.modules.im.service.SecretQuestionService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 密保答案
 */
@Slf4j
@RestController
@RequestMapping("/a/secretAnswer")
public class SecretAnswerCtrl extends BaseApiCtrl {
    @Resource
    private SecretQuestionService secretQuestionService;
    @Resource
    private SecretAnswerService secretAnswerService;
    @Resource
    private UserService userService;

    /**
     * 设置密保问题答案
     * 删除旧的
     * 保存新的
     * @return
     */
    @PostMapping("/set")
    public Result<Object> save(@RequestParam("json") String json){
        try {
            JSONArray arr = JSONArray.parseArray(json);
            if (arr.size() != 3) {
                return fail("请选择三个问题");
            }
            Integer questionId;
            String answer;
            SecretQuestion question;
            SecretAnswer a;
            List<SecretAnswer> answers = new ArrayList<>();
            for (Object o : arr) {
                questionId = ((JSONObject) o).getIntValue("questionId");
                answer = ((JSONObject) o).getString("answer");
                question = secretQuestionService.getById(questionId);
                if (question == null) {
                    return fail("密保问题不存在");
                }
                if (StringUtils.isBlank(answer)) {
                    return fail("答案不能为空");
                }
                a = new SecretAnswer();
                a.setAnswer(answer);
                a.setQuestion(question.getContent());
                a.setUserId(getCurrentUserId());
                a.setTsCreate(getTs());
                answers.add(a);
            }
            //删除已存在的密保答案
            LambdaQueryWrapper<SecretAnswer> wrapper = new LambdaQueryWrapper<SecretAnswer>();
            wrapper.eq(SecretAnswer::getUserId, getCurrentUserId());
            if (secretAnswerService.list(wrapper).size() > 0) {
                secretAnswerService.remove(wrapper);
            }
            if(secretAnswerService.saveBatch(answers)){
                return success(secretAnswerService.list(wrapper));
            }
            return fail();
        }catch (Exception e){
            log.error("设置密保问题异常,e={0}",e);
            return fail("设置密保问题出错");
        }
    }

    /**
     * 查询我的密保问题及答案
     * @return
     */
    @PostMapping("/findMy")
    public Result<Object> findMy(){
        LambdaQueryWrapper<SecretAnswer> wrapper = new LambdaQueryWrapper<SecretAnswer>();
        wrapper.eq(SecretAnswer::getUserId, getCurrentUserId());
        return success(secretAnswerService.list(wrapper));
    }
    /**
     * 查询我的密保问题及答案
     * @return
     */
    @NoNeedUserToken
    @PostMapping("/findByAccount")
    public Result<Object> findByAccount(@RequestParam("account") String account){
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
        LambdaQueryWrapper<SecretAnswer> wrapper = new LambdaQueryWrapper<SecretAnswer>();
        wrapper.eq(SecretAnswer::getUserId, user.getId());
        return success(secretAnswerService.list(wrapper));
    }
    /**
     * 验证密保
     * @return
     */
    @NoNeedUserToken
    @PostMapping("/check")
    public Result<Object> check(@RequestParam("json")String json,@RequestParam("account")String account){
        return secretAnswerService.check(account,json);
    }
}
