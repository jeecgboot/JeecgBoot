package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ClientConfig;
import org.jeecg.modules.im.entity.SignIn;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.UserInfo;
import org.jeecg.modules.im.entity.query_helper.QSignIn;
import org.jeecg.modules.im.mapper.SignInMapper;
import org.jeecg.modules.im.service.ClientConfigService;
import org.jeecg.modules.im.service.SignInService;
import org.jeecg.modules.im.service.UserInfoService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 签到 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-10
 */
@Service
@Slf4j
public class SignInServiceImpl extends BaseServiceImpl<SignInMapper, SignIn> implements SignInService {
    @Autowired
    private SignInMapper signInMapper;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;

    @Override
    public IPage<SignIn> pagination(MyPage<SignIn> page, QSignIn q) {
        return signInMapper.pagination(page,q);
    }

    @Override
    public SignIn findByDateOfUser(Date date, Integer userId) {
        return signInMapper.findByDateOfUser(ToolDateTime.getStartTimeOfDate(date), ToolDateTime.getEndTimeOfDate(date),userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> sign() {
        User user = getCurrentUser();
        ClientConfig clientConfig = clientConfigService.get();
        if (!clientConfig.getSignIn()) {
            return fail("签到功能未开启");
        }
        UserInfo userInfo = userInfoService.findBasicByUserId(user.getId());
        if (userInfo.getSignInDay() >= clientConfig.getSignInMaxDay() && clientConfig.getSignInMaxDay() > 0) {
            return fail("已达到累计签到天数，请申请提现后再签到！");
        }
        if (findByDateOfUser(new Date(), user.getId()) != null) {
            return fail("今日已签到");
        }
        try {
            String rewardConfig = clientConfig.getSignInReward();
            //奖励
            Integer reward;
            //重置连续签到天数
            boolean clean = false;
            //开启连续签到
            if (clientConfig.getSignInContinueDay() > 0) {
                String[] rewards = StringUtils.split(rewardConfig, ",");
                reward = Integer.parseInt(rewards[userInfo.getContinueSignInDay()]);
                //连续签到天数达到或超过指定天数
                if (userInfo.getContinueSignInDay() >= clientConfig.getSignInContinueDay()) {
                    clean = true;
                }
            } else {
                reward = Integer.parseInt(rewardConfig);
            }
            SignIn signIn = new SignIn();
            signIn.setUserId(user.getId());
            signIn.setReward(reward);
            if(!save(signIn)){
                return fail();
            }
            userInfo.setSignInDay(userInfo.getSignInDay()+1);
            userInfo.setContinueSignInDay(userInfo.getContinueSignInDay()+1);
            if(clean){
                userInfo.setContinueSignInDay(0);
            }
            if(!userService.updateById(user)){
                return fail();
            }
            //调用充值服务进行充值

            return success();
        }catch (Exception e){
            log.error("签到异常：user={},e={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
}
