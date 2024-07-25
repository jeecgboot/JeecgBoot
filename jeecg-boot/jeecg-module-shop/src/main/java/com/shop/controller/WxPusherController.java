package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shop.common.core.web.BaseController;
import com.shop.common.core.web.JsonResult;
import com.shop.entity.ShopSettings;
import com.shop.service.ShopSettingsService;
import com.zjiecode.wxpusher.client.HttpUtils;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.CreateQrcodeReq;
import com.zjiecode.wxpusher.client.bean.CreateQrcodeResp;
import com.zjiecode.wxpusher.client.bean.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信订单通知
 * 2021-07-04 16:31:11
 */
@Controller
@RequestMapping("/wxpusher")
public class WxPusherController extends BaseController {

    @Autowired
    private ShopSettingsService shopSettingsService;

    @RequiresPermissions("settings:wxpusher:view")
    @RequestMapping("/send")
    public String view() {
        return "settings/wxpusher.html";
    }

    @RequestMapping("/getWxpushCode")
    @ResponseBody
    public JsonResult getWxpushCode() {

        ShopSettings shopSettings = shopSettingsService.getById(1);

        if (StringUtils.isEmpty(shopSettings.getAppToken())) {
            return JsonResult.error("您好，wxpusher 的 token为空！");
        }

        CreateQrcodeReq createQrcodeReq = new CreateQrcodeReq();
        createQrcodeReq.setAppToken(shopSettings.getAppToken()); // 必填，应用的appTOken
        createQrcodeReq.setExtra(getLoginUserId().toString());// 必填，携带的参数
        createQrcodeReq.setValidTime(3600);// 可选，二维码有效时间，默认1800 s，最大30天，单位是s
        Result<CreateQrcodeResp> tempQrcode = WxPusher.createAppTempQrcode(createQrcodeReq);

        if (tempQrcode.getCode() == 1000) {
            // 根据查询指定UID用户
            Map<String, Object> params = new HashMap();
            params.put("appToken", shopSettings.getAppToken());
            params.put("page", 1);
            params.put("pageSize", 1);
            params.put("uid", shopSettings.getWxpushUid());
            params.put("type", 0);

            Result result = HttpUtils.get(params, "/api/fun/wxuser/v2");
            Map parseObject = JSON.parseObject(JSONObject.toJSONString(result.getData()));
            Integer total = (Integer) parseObject.get("total"); // 0 未关注  1 已关注

            if (total == 0) {
                ShopSettings shopSettings1 = new ShopSettings();
                shopSettings1.setId(1);
                shopSettings1.setWxpushUid("");
                shopSettingsService.updateById(shopSettings1);
            }

            /**
             *  这个情况是在扫码关注后 又取消关注，想再重新关注 生成的二维码
             */
            if (StringUtils.isEmpty(shopSettings.getWxpushUid()) || total == 0) {
                Map<String, String> map = new HashMap<>();
                map.put("code", "1");
                map.put("url", tempQrcode.getData().getUrl());
                return JsonResult.ok("成功").setData(map);
            } else {
                Object records = parseObject.get("records");
                String s = records.toString();
                String substring = s.substring(1, s.length() - 1);
                Map jsonObject = JSON.parseObject(substring);
                String headImg = jsonObject.get("headImg").toString();  // 头像

                Map<String, String> map = new HashMap<>();
                map.put("code", "2");
                map.put("headImg", headImg);
                return JsonResult.ok("成功").setData(map);
            }
        }
        return null;
    }

}
