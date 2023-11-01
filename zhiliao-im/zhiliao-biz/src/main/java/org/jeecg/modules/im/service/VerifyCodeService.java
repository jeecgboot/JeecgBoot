package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.VerifyCode;
import org.jeecg.modules.im.entity.query_helper.QVerifyCode;

/**
 * <p>
 * 短信发送 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface VerifyCodeService extends IService<VerifyCode> {
    IPage<VerifyCode> pagination(MyPage<VerifyCode> page, QVerifyCode q);

    Result<Object> sendByMobile(String mobile, String type) ;
    Result<Object> sendByEmail(String email, String type) ;


    /**
     * 使用阿里云短信发送
     * @param mobile
     * @param code
     * @return
     */
    Result<Object> aliyunSend(String mobile,String type,String code) throws Exception;

    /**
     * 根据手机号和业务类型查找最后一条
     * @param mobile
     * @param type
     * @return
     */
    VerifyCode findLatestByMobileAndType(String mobile,String type);
    /**
     * 根据邮箱和业务类型查找最后一条
     * @param email
     * @param type
     * @return
     */
    VerifyCode findLatestByEmailAndType(String email,String type);

    //生成短信验证码
    String generateCode();
    //校验手机验证码
    Result<Object> verifyByMobile(String mobile, String verifyCode,String type);
    //校验邮箱验证码
    Result<Object> verifyByEmail(String email, String verifyCode,String type);
}
