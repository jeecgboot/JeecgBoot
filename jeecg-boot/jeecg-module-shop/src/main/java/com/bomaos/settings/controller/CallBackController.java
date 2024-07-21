package com.bomaos.settings.controller;

import com.alibaba.fastjson.JSONObject;
import com.bomaos.common.core.web.BaseController;
import com.bomaos.settings.entity.ShopSettings;
import com.bomaos.settings.service.ShopSettingsService;
import com.bomaos.website.entity.Website;
import com.bomaos.website.service.WebsiteService;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.callback.AppSubscribeBean;
import com.zjiecode.wxpusher.client.bean.callback.BaseCallBackReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 说明：接收来自WxPusher服务的回调
 * 当用户扫码关注的时候会触发
 * 作者：zjiecode
 * 时间：2019-10-05
 */
@RestController
@RequestMapping("/wxpusher")
public class CallBackController extends BaseController {

    @Autowired
    private ShopSettingsService shopSettingsService;

    @Autowired
    private WebsiteService websiteService;

    @PostMapping("/callback")
    public void callback(@RequestBody BaseCallBackReq callBackReq) {
        if (BaseCallBackReq.ACTION_APP_SUBSCRIBE.equalsIgnoreCase(callBackReq.getAction())) {
            AppSubscribeBean appSubscribeBean = JSONObject.parseObject(JSONObject.toJSONString(callBackReq.getData()), AppSubscribeBean.class);
            if (!StringUtils.isEmpty(appSubscribeBean.getExtra())) {
                ShopSettings shopSettings = shopSettingsService.getById(1);
                Website website = websiteService.getById(1);
                if (StringUtils.isEmpty(shopSettings.getWxpushUid())) {
                    ShopSettings shopSettings1 = new ShopSettings();
                    shopSettings1.setId(1);
                    shopSettings1.setWxpushUid(appSubscribeBean.getUid());
                    if (shopSettingsService.updateById(shopSettings1)) {
                        //扫码以后，发送一条消息给用户
                        Message message = new Message();
                        message.setContent("关注" + website.getWebsiteName() + "成功！您将在这里收到发卡平台订单交易是的实时通知！");
                        message.setContentType(Message.CONTENT_TYPE_TEXT);
                        message.setUid(appSubscribeBean.getUid());
                        message.setAppToken(shopSettings.getAppToken());
                        WxPusher.send(message);
                    }
                } else {
                    //扫码以后，发送一条消息给用户
                    Message message = new Message();
                    message.setContent("重新关注" + website.getWebsiteName() + "公众号、您将在这里收到发卡平台订单交易的实时通知！");
                    message.setContentType(Message.CONTENT_TYPE_TEXT);
                    message.setUid(appSubscribeBean.getUid());
                    message.setAppToken(shopSettings.getAppToken());
                    WxPusher.send(message);
                }
            } else {
                //无参数二维码（默认二维码）
            }
        }
    }
}
