package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 参数
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Param.TABLE_NAME)
public class Param extends BaseModel<Param> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_param";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String value;

    private String info;

    private Long tsCreate;


    public enum Name {
        //短信验证码
        verify_code_length,//长度
        sms_way,//通道，多个用,隔开，第一个为启用
        verify_code_invalid_minutes,//有效时间，单位：分
        //阿里云短信
        ali_sms_access_key_id,//ak
        ali_sms_access_key_secret,//as
        ali_sms_tpl_code,//模板编号
        ali_sms_tpl_sign,//签名
        ali_sms_region_id,
        //腾讯防水墙
        tencent_captcha_on,
        tencent_captcha_app_id,
        tencent_captcha_app_secret,
        //短信验证码登录
        enable_sms_code_login,
        //测试
        is_api_test,
        is_back_test,
    }
    public static final String sms_way_aliyun = "aliyun";//阿里云短信
    public static final String sms_way_juhe = "juhe";//聚合短信


}
