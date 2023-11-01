package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 公告
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Notice.TABLE_NAME)
public class Notice extends BaseModel<Notice> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_notice";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 置顶时间
     */
    private Long tsPin;

    /**
     * 隐藏
     */
    private Boolean isShow;

    /**
     * 浏览量
     */
    private Integer viewTimes;

    private Long tsCreate;

}
