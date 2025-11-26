package org.jeecg.common.util;

import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.enums.DySmsEnum;
import org.jeecg.config.JeecgSmsTemplateConfig;
import org.jeecg.config.tencent.JeecgTencent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 腾讯发送短信
 * @author: wangshuai
 * @date: 2025/11/4 19:27
 */
public class TencentSms {

    private final static Logger logger = LoggerFactory.getLogger(TencentSms.class);

    /**
     * 发送腾讯短信
     *
     * @param phone
     * @param templateParamJson
     * @param tencent
     * @param dySmsEnum
     * @return
     */
    public static boolean sendTencentSms(String phone, JSONObject templateParamJson, JeecgTencent tencent, DySmsEnum dySmsEnum) {
        //获取客户端链接
        SmsClient client = getSmsClient(tencent);
        //构建腾讯云短信发送请求
        SendSmsRequest req = buildSendSmsRequest(phone, templateParamJson, dySmsEnum, tencent);
        try {
            //发送短信
            SendSmsResponse resp = client.SendSms(req);
            // 处理响应
            SendStatus[] statusSet = resp.getSendStatusSet();
            if (statusSet != null && statusSet.length > 0) {
                SendStatus status = statusSet[0];
                if ("Ok".equals(status.getCode())) {
                    logger.info("短信发送成功，手机号：{}", phone);
                    return true;
                } else {
                    logger.error("短信发送失败，手机号：{}，错误码：{}，错误信息：{}",
                            phone, status.getCode(), status.getMessage());
                }
            }
        } catch (TencentCloudSDKException e) {
            logger.error("短信发送失败{}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取sms客户端
     *
     * @param tencent 腾讯云配置
     * @return SmsClient对象
     */
    private static SmsClient getSmsClient(JeecgTencent tencent) {
        Credential cred = new Credential(tencent.getSecretId(), tencent.getSecretKey());
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        //指定接入地域域名*/
        httpProfile.setEndpoint(tencent.getEndpoint());
        //实例化一个客户端配置对象
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        //实例化要请求产品的client对象，第二个参数是地域信息
        return new SmsClient(cred, tencent.getRegion(), clientProfile);
    }

    /**
     * 构建腾讯云短信发送请求
     *
     * @param phone             手机号码
     * @param templateParamJson 模板参数JSON对象
     * @param dySmsEnum         短信枚举配置
     * @param tencent           腾讯云配置
     * @return 构建好的SendSmsRequest对象
     */
    private static SendSmsRequest buildSendSmsRequest(
            String phone,
            JSONObject templateParamJson,
            DySmsEnum dySmsEnum,
            JeecgTencent tencent) {

        SendSmsRequest req = new SendSmsRequest();

        // 1. 设置短信应用ID
        String sdkAppId = tencent.getSdkAppId();
        req.setSmsSdkAppId(sdkAppId);
        // 2. 设置短信签名
        String signName = getSmsSignName(dySmsEnum);
        req.setSignName(signName);
        // 3. 设置模板ID
        String templateId = getSmsTemplateId(dySmsEnum);
        req.setTemplateId(templateId);
        // 4. 设置模板参数
        String[] templateParams = extractTemplateParams(templateParamJson);
        req.setTemplateParamSet(templateParams);
        // 5. 设置手机号码
        String[] phoneNumberSet = { phone };
        req.setPhoneNumberSet(phoneNumberSet);

        logger.debug("构建短信请求完成 - 应用ID: {}, 签名: {}, 模板ID: {}, 手机号: {}",
                sdkAppId, signName, templateId, phone);

        return req;
    }

    /**
     * 获取短信签名名称
     *
     * @param dySmsEnum 腾讯云对象
     */
    private static String getSmsSignName(DySmsEnum dySmsEnum) {
        JeecgSmsTemplateConfig baseConfig = SpringContextUtils.getBean(JeecgSmsTemplateConfig.class);
        String signName = dySmsEnum.getSignName();

        if (StringUtils.isNotEmpty(baseConfig.getSignature())) {
            logger.debug("yml中读取签名名称: {}", baseConfig.getSignature());
            signName = baseConfig.getSignature();
        }

        return signName;
    }

    /**
     * 获取短信模板ID
     *
     * @param dySmsEnum 腾讯云对象
     */
    private static String getSmsTemplateId(DySmsEnum dySmsEnum) {
        JeecgSmsTemplateConfig baseConfig = SpringContextUtils.getBean(JeecgSmsTemplateConfig.class);
        String templateCode = dySmsEnum.getTemplateCode();

        if (StringUtils.isNotEmpty(baseConfig.getSignature())) {
            Map<String, String> smsTemplate = baseConfig.getTemplateCode();
            if (smsTemplate.containsKey(templateCode) &&
                    StringUtils.isNotEmpty(smsTemplate.get(templateCode))) {
                templateCode = smsTemplate.get(templateCode);
                logger.debug("yml中读取短信模板ID: {}", templateCode);
            }
        }
        return templateCode;
    }

    /**
     * 从JSONObject中提取模板参数（按原始顺序）
     *
     * @param templateParamJson 模板参数
     */
    private static String[] extractTemplateParams(JSONObject templateParamJson) {
        if (templateParamJson == null || templateParamJson.isEmpty()) {
            return new String[0];
        }
        List<String> params = new ArrayList<>();
        for (String key : templateParamJson.keySet()) {
            Object value = templateParamJson.get(key);
            if (value != null) {
                params.add(value.toString());
            } else {
                // 处理null值
                params.add("");
            }
        }
        logger.debug("提取模板参数: {}", params);
        return params.toArray(new String[0]);
    }
}
