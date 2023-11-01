package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 每日数据统计
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_statistic")
public class Statistic extends BaseModel<Statistic> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String date;

    private Long tsCreate;

    private Long tsUpdate;

    /**
     * 注册用户
     */
    private Integer userRegister;

    /**
     * 登录用户
     */
    private Integer userLogin;

    /**
     * 好友添加
     */
    private Integer friendAdd;

    /**
     * 群组创建
     */
    private Integer mucCreate;

    /**
     * 单聊消息
     */
    private Integer msgSend;

    /**
     * 动态发布
     */
    private Integer activity;

    /**
     * 群聊消息
     */
    private Integer mucMsgSend;

}
