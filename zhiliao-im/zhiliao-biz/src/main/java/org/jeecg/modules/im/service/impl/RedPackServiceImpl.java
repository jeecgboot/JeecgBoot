package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.util.ToolPassword;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QRedPack;
import org.jeecg.modules.im.mapper.RedPackMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jivesoftware.smack.packet.id.UuidStanzaIdSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 红包 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
@Slf4j
public class RedPackServiceImpl extends BaseServiceImpl<RedPackMapper, RedPack> implements RedPackService {
    @Autowired
    private RedPackMapper redPackMapper;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserSettingService userSettingService;
    @Resource
    private XMPPService xmppService;

    @Override
    public IPage<RedPack> pagination(MyPage<RedPack> page, QRedPack q) {
        return redPackMapper.pagination(page, q);
    }

    //发送单聊红包
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> send(RedPack redPack) {
        try {
            User user = getCurrentUser();
            //判断用户是否设置了支付密码
            if (isEmpty(user.getPayPassword())) {
                return fail("未设置支付密码");
            }
            if(!ToolPassword.checkPassword(user.getPaySalt(), user.getPayPassword(), redPack.getPayPwd())){
                return fail("支付密码错误");
            }
            ClientConfig config = clientConfigService.get();
            if (!config.getAllowRedPack()) {
                return fail("红包功能未启用");
            }
            if (redPack.getAmount().doubleValue() <= 0) {
                return fail("红包金额不能小于0");
            }
            if (redPack.getAmount().doubleValue() > config.getRedPackMax() && config.getRedPackMax() > 0) {
                return fail("红包金额上限" + config.getRedPackMax());
            }
            UserSetting userSetting = userSettingService.findByUserId(user.getId());
            if(!userSetting.getAllowSendRedPack()){
                return fail("你的红包功能已被停用");
            }
            UserInfo userInfo = userInfoService.findBasicByUserId(user.getId());
            if (userInfo.getBalance()< redPack.getAmount()) {
                return fail("余额不足，请充值");
            }
            User toUser = userService.findById(redPack.getToUserId());
            //对方不存在，或者不是用户
            if (toUser == null || toUser.getType()> User.Type.business.getCode()) {
                return fail("接收方不存在");
            }
            //普通、口令
            if (!redPack.getType().equals(RedPack.Type.COMMON.getCode())&&!redPack.getType().equals(RedPack.Type.COMMAND.getCode())) {
                return fail("红包类型错误");
            }
            if(isEmpty(redPack.getWords())){
                redPack.setWords(null);
            }
            String stanzaId = UuidStanzaIdSource.INSTANCE.getNewStanzaId();
            redPack.setUserId(user.getId());
            redPack.setCount(1);
            redPack.setLeftAmount(redPack.getAmount());
            redPack.setTsCreate(getTs());
            redPack.setTsValid(ToolDateTime.getDateByDatePlusDays(new Date(), 1).getTime());
            if (!save(redPack)) {
                return fail("发送失败，请稍后再试");
            }
            //更新余额与冻结余额
            userInfo.setBalance(userInfo.getBalance()- redPack.getAmount());
            userInfo.setBalanceFreeze(userInfo.getBalanceFreeze()+ redPack.getAmount());
            userInfoService.updateById(userInfo);
            //发送单聊红包消息
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(user.getId());
            messageBean.setUserName(user.getNickname());
            messageBean.setToUserId(toUser.getId());
            messageBean.setToUserName(toUser.getNickname());
            messageBean.setContent(JSONObject.toJSONString(redPack));
            messageBean.setStanzaId(stanzaId);
            messageBean.setType(MsgType.redPack.getType());

            xmppService.sendMsgToOne(messageBean);
            return success();
        }catch (Exception e) {
            e.printStackTrace();
            log.error("发送单聊红包异常：redPack={},e={}", redPack, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail("发送失败，请稍后再试");
        }
    }

    @Override
    public Result<Object> sendMuc(RedPack redPack) {
        return null;
    }
}
