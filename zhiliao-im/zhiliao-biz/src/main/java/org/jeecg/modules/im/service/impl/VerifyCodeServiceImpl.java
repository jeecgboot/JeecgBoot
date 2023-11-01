package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.VerifyCode;
import org.jeecg.modules.im.entity.query_helper.QVerifyCode;
import org.jeecg.modules.im.mapper.VerifyCodeMapper;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.VerifyCodeService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 短信发送 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class VerifyCodeServiceImpl extends BaseServiceImpl<VerifyCodeMapper, VerifyCode> implements VerifyCodeService {
    @Autowired
    private VerifyCodeMapper verifyCodeMapper;
    @Resource
    private ParamService paramService;
    @Resource
    private UserService userService;

    @Override
    public IPage<VerifyCode> pagination(MyPage<VerifyCode> page, QVerifyCode q) {
        return verifyCodeMapper.pagination(page, q);
    }

    @Override
    public Result<Object> sendByMobile(String mobile, String type) {
        //判断发送频率
        VerifyCode verifyCode = findLatestByMobileAndType(mobile, type);
        if(isTest()){
            if (verifyCode != null) {
                int second = ToolDateTime.getDateSecondSpace(verifyCode.getTsCreate(), getTs());
                if (second < 60) {
                    return fail("操作过于频繁", 60 - second);
                }
            }
        }
        String code = generateCode();
        try {
            if (paramService.getByName(Param.Name.sms_way) == null) {
                return fail("短信通道未启用");
            }
            //判断短信通道配置第一项
            switch (StringUtils.split(paramService.getByName(Param.Name.sms_way), ",")[0]) {
                case Param.sms_way_aliyun: {
                    return aliyunSend(mobile, type, code);
                }
                default:
                    throw new BusinessException("短信通道未启用");
            }
        } catch (Exception e) {
            log.error("发送短信验证码异常：mobile={},type={},e={}", mobile, type, e);
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
    @Override
    public Result<Object> sendByEmail(String email, String type) {
        //判断发送频率
        VerifyCode verifyCode = findLatestByEmailAndType(email, type);
        if(!isTest()){
            if (verifyCode != null) {
                int second = ToolDateTime.getDateSecondSpace(verifyCode.getTsCreate(), getTs());
                if (second < 60) {
                    return fail("操作过于频繁", 60 - second);
                }
            }
        }
        String code = generateCode();
        try {
            return fail();
        } catch (Exception e) {
            log.error("发送邮箱验证码异常：email={},type={},e={}", email, type, e);
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    public Result<Object> aliyunSend(String mobile, String type, String code) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", paramService.getByName(Param.Name.ali_sms_access_key_id), paramService.getByName(Param.Name.ali_sms_access_key_secret));
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", paramService.getByName(Param.Name.ali_sms_region_id));
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", paramService.getByName(Param.Name.ali_sms_tpl_sign));
        request.putQueryParameter("TemplateCode", paramService.getByName(Param.Name.ali_sms_tpl_code));
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(Kv.by("code", code)));
        try {
            VerifyCode s = new VerifyCode();
            s.setCode(code);
            s.setMobile(mobile);
            s.setType(type);
            s.setWay(Param.sms_way_aliyun);
            s.setTsCreate(getTs());
            if (!isTest()) {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println(response.getData());
                JSONObject jsonObject = JSONObject.parseObject(response.getData());
                if (jsonObject.getString("Message").equals("OK") && jsonObject.getString("Code").equals("OK")) {
                    if (!save(s)) {
                        return fail();
                    }
                    return success();
                } else {
                    return fail("验证码发送失败,请重试");
                }
            } else {
                if (!save(s)) {
                    return fail();
                }
                return success((Object) code);
            }
        } catch (Exception e) {
            log.error("阿里云发送验证码异常：mobile={},type={},code={},e={}", mobile, type, code, e);
            throw new Exception();
        }
    }

    @Override
    public VerifyCode findLatestByMobileAndType(String mobile, String type) {
        return verifyCodeMapper.findLatestByMobileAndType(mobile, type);
    }
    @Override
    public VerifyCode findLatestByEmailAndType(String email, String type) {
        return verifyCodeMapper.findLatestByEmailAndType(email, type);
    }

    @Override
    public String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < Integer.parseInt(paramService.getByName(Param.Name.verify_code_length,"6")); i++) {
            code.append((int) (Math.random() * 9));
        }
        return code.toString();
    }

    @Override
    public Result<Object> verifyByMobile(String mobile, String verifyCode,String type) {
        //校验短信验证码
        VerifyCode code = findLatestByMobileAndType(mobile, type);
        if (code == null || !equals(code.getCode(), verifyCode)) {
            return fail("验证码错误");
        }
        if (ToolDateTime.getDateSecondSpace(code.getTsCreate(), getTs()) > Integer.parseInt(paramService.getByName(Param.Name.verify_code_invalid_minutes, 15 * 60 + ""))) {
            return fail("验证码失效,请重新获取");
        }
        return success();
    }
    @Override
    public Result<Object> verifyByEmail(String email, String verifyCode,String type) {
        //校验短信验证码
        VerifyCode code = findLatestByEmailAndType(email, type);
        if (code == null || !equals(code.getCode(), verifyCode)) {
            return fail("验证码错误");
        }
        if (ToolDateTime.getDateSecondSpace(code.getTsCreate(), getTs()) > Integer.parseInt(paramService.getByName(Param.Name.verify_code_invalid_minutes, 15 * 60 + ""))) {
            return fail("验证码失效,请重新获取");
        }
        return success();
    }
}
